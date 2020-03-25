package cn.hybris.common.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 密钥工具类
 * 
 * <p>来自支付宝的Demo
 */
public class KeyReader {
	private static final Logger log = LoggerFactory.getLogger(KeyReader.class);

	/**
	 * 读取私钥
	 * 
	 * @param keyStr
	 * @param base64Encoded
	 * @param algorithmName
	 * @return 私钥
	 * @throws InvalidKeySpecException
	 */
	public PrivateKey readPrivateKey(String keyStr, boolean base64Encoded, String algorithmName) throws InvalidKeySpecException {
		return (PrivateKey) readKey(keyStr, false, base64Encoded, algorithmName);
	}

	/**
	 * 读取公钥
	 * 
	 * @param keyStr
	 * @param base64Encoded
	 * @param algorithmName
	 * @return 公钥
	 * @throws InvalidKeySpecException
	 */
	public PublicKey readPublicKey(String keyStr, boolean base64Encoded, String algorithmName) throws InvalidKeySpecException {
		return (PublicKey) readKey(keyStr, true, base64Encoded, algorithmName);
	}

	/**
	 * 读取密钥，X509EncodedKeySpec的公钥与PKCS8EncodedKeySpec都可以读取，密钥内容可以为非base64编码过的。
	 * 
	 * @param keyStr
	 * @param isPublicKey
	 * @param base64Encoded
	 * @param algorithmName
	 * @return 密钥
	 * @throws InvalidKeySpecException
	 */
	private Key readKey(String keyStr, boolean isPublicKey, boolean base64Encoded, String algorithmName) throws InvalidKeySpecException {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(algorithmName);

			byte[] encodedKey = keyStr.getBytes("UTF-8");

			if (base64Encoded) {
				encodedKey = Base64.decodeBase64(encodedKey);
			}

			if (isPublicKey) {
				EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);

				return keyFactory.generatePublic(keySpec);
			} else {
				EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);

				return keyFactory.generatePrivate(keySpec);
			}
		} catch (NoSuchAlgorithmException e) {
			// 不可能发生
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 得到私钥.
	 * 
	 * @param resourceName
	 * @param password
	 * @return 私钥
	 * @throws Exception
	 */
	public PrivateKey readPrivateKeyfromPKCS12StoredFile(String resourceName, String password) throws Exception {
		try (InputStream is = new FileInputStream(resourceName)) {
			KeyStore keystore = KeyStore.getInstance("PKCS12"); // 使用默认的keyprovider，可能会有问题。
			keystore.load(is, password.toCharArray());
			Enumeration<String> enumeration = keystore.aliases();
			String alias = null;
			for (int i = 0; enumeration.hasMoreElements(); i++) {
				alias = enumeration.nextElement().toString();
				if (i >= 1) {
					log.warn("此文件中含有多个证书!");
				}
			}
			PrivateKey key = (PrivateKey) keystore.getKey(alias, password.toCharArray());
			return key;
		}
	}

	/**
	 * Base64编码X.509格式证书文件中读取公钥
	 * 
	 * @param resourceName
	 * @return 密钥
	 * @throws Exception
	 */
	public Key fromCerStoredFile(String resourceName) throws Exception {
		try (InputStream is = new FileInputStream(resourceName)) {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			Certificate certificate = cf.generateCertificate(is);
			return (Key) (certificate != null ? certificate.getPublicKey() : null);
		}
	}

}
