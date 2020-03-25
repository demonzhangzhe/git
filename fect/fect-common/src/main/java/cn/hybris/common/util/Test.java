package cn.hybris.common.util;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "我的\n啊啊";
		System.out.println(str);
		str = str.replaceAll("\n", "\r\n");
		System.out.println(str);
	}

}
