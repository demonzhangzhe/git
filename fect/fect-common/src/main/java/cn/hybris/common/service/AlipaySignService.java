package cn.hybris.common.service;

/**
 * 微信报文验证签名和生成签名
 * 
 * @author Qu Dihuai
 *
 */
public interface AlipaySignService {
	/**
	 * 对报文进行验签.
	 * 
	 * @param xml
	 * @return true表示成功，false表示失败
	 * @throws Exception
	 */
	boolean check(String xml);

	/**
	 * 对报文进行签名.
	 * 
	 * @param xml
	 * @return 签名后的报文
	 * @throws Exception
	 */
	String sign(String xml);
}
