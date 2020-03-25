package cn.hybris.common.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.xml.security.Init;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xpath.XPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cn.hybris.common.service.AlipaySignService;
import cn.hybris.common.util.KeyReader;
import cn.hybris.common.util.OfflineResolver;

/**
 * 支付宝签名与验签服务实现类
 * 
 * @author Qu Dihuai
 *
 */
@Service("alipaySignService")
public class AlipaySignServiceImpl implements AlipaySignService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Value("${sign.alipay.public-key}")
	private String alipayPublicKey; // 支付宝的公钥

	@Value("${sign.alipay.private-key}")
	private String psbcPrivateKey; // 私钥

	@Value("${sign.alipay.private-key-password}")
	private String psbcPrivateKeyPassword; // 私钥密码

	private PublicKey publicKey;

	private PrivateKey privateKey;

	private static DocumentBuilderFactory dbf;
	static {
		// 初始化Document
		dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		dbf.setValidating(false);
		dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);

		/**
		 * 初始化Security
		* 
		* 这里用静态块初始化一些加解密用到的东西，可以避免抛出如下的异常 java.lang.NullPointerException at
		* org.apache.xml.security.algorithms.JCEMapper.translateURItoJCEID(Unknown
		* Source) at
		* org.apache.xml.security.algorithms.MessageDigestAlgorithm.getDigestInstance(Unknown
		* Source)
		 */
		Init.init();
		try {
			Constants.setSignatureSpecNSprefix("ds"); // 设置Signature标签的前缀,这个前缀是不影响签名校验结果的。
		} catch (final XMLSecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@PostConstruct
	public void initial() {
		this.publicKey = getAlipayPubKey();
		this.privateKey = getPrivateKey();
	}

	@Override
	public boolean check(final String xml) {
		try {
			// 转换
			Document doc = this.getDocFromString(xml);
			// 进行验签
			return check(doc, publicKey);
		} catch (final Exception e) {
			throw new RuntimeException("签名无效", e);
		}
	}

	@Override
	public String sign(final String xml) {
		try {
			// 转换
			Document doc = this.getDocFromString(xml);
			// 签名
			return sign(doc, privateKey);
		} catch (final Exception e) {
			throw new RuntimeException("进行签名时出错！", e);
		}
	}

	/**
	 * 验签.
	 * 
	 * @param doc
	 * @param pubKey
	 * @return true表示成功，false表示失败
	 */
	private boolean check(final Document doc, final PublicKey pubKey) {
		if (pubKey == null) {
			log.error("支付宝的公钥为空！");
			throw new RuntimeException("支付宝的公钥为空！");
		}

		try {
			Element nscontext = XMLUtils.createDSctx(doc, "ds", Constants.SignatureSpecNS);
			Element signElement = (Element) XPathAPI.selectSingleNode(doc, "//ds:Signature[1]", nscontext);

			if (signElement == null) {
				log.error("签名不能为空！");
				throw new RuntimeException("签名不能为空！");
			}

			XMLSignature signature = new XMLSignature(signElement, doc.getDocumentURI());
			return signature.checkSignatureValue(pubKey);
		} catch (final Exception e) {
			throw new RuntimeException("验证签名时发生异常!", e);
		}
	}

	/**
	 * 签名.
	 * 
	 * @param doc
	 * @param privateKey
	 * @return 签名后的字符串
	 */
	private String sign(final Document doc, final PrivateKey privateKey) {
		if (privateKey == null) {
			log.error("私钥为空！");
			return null;
		}

		try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			XMLSignature sig = new XMLSignature(doc, doc.getDocumentURI(), XMLSignature.ALGO_ID_SIGNATURE_RSA);
			sig.getSignedInfo().addResourceResolver(new OfflineResolver());

			Node messageNode = doc.getElementsByTagName("message").item(0);
			messageNode.appendChild(sig.getElement());

			Transforms transforms = new Transforms(doc);
			transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
			sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

			// 签名
			sig.sign(privateKey);

			// 将签名好的XML文档写出
			XMLUtils.outputDOM(doc, os);
			return os.toString("UTF-8");
		} catch (final Exception e) {
			throw new RuntimeException("签名时出错", e);
		}
	}

	/**
	 * 得到Document
	 * 
	 * @param xml xml文件
	 * @return Document
	 * @throws Exception
	 */
	private Document getDocFromString(final String xml) throws Exception {
		final byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
		try (final InputStream bais = new ByteArrayInputStream(bytes)) {
			return dbf.newDocumentBuilder().parse(bais);
		}
	}

	/**
	 * 
	 * 得到支付宝的公钥.
	 * 
	 * <p>
	 * 支付宝提供给合作伙伴的公钥
	 * </p>
	 * 
	 * @return 公钥
	 * @throws Exception
	 */
	private PublicKey getAlipayPubKey() {
		String filename = this.getCrtPath(alipayPublicKey);

		KeyReader keyReader = new KeyReader();
		PublicKey pubKey = null;
		try {
			pubKey = (PublicKey) keyReader.fromCerStoredFile(filename);
			log.info("PublicKey => 加密：{}", new String(Base64.encodeBase64(pubKey.getEncoded())));
		} catch (final Exception e) {
			throw new RuntimeException("获取支付宝公钥时出错！", e);
		}

		return pubKey;
	}

	/**
	 * 
	 * 得到自己的私钥.
	 * 
	 * <p>
	 * 使用私钥对报文进行签名
	 * </p>
	 * 
	 * @return 私钥
	 * @throws Exception
	 */
	private PrivateKey getPrivateKey() {
		String filename = this.getCrtPath(psbcPrivateKey);

		KeyReader keyReader = new KeyReader();
		PrivateKey priKey = null;
		try {
			priKey = keyReader.readPrivateKeyfromPKCS12StoredFile(filename, psbcPrivateKeyPassword);
			log.info("PrivateKey => 加密：{}", new String(Base64.encodeBase64(priKey.getEncoded())));
		} catch (Exception e) {
			throw new RuntimeException("获取自己的私钥时出错！", e);
		}

		return priKey;
	}

	private String getCrtPath(final String fileName) {
		
		Resource resource = new ClassPathResource(fileName); 
		try {
			File sourceFile = resource.getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
}
