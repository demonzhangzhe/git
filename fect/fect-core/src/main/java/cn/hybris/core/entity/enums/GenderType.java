package cn.hybris.core.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author Qu Dihuai
 *
 */
public enum GenderType {
	MALE(1, "男"),
	FEMALE(0, "女");

	@EnumValue
	private int code;
	private String desc;

	private GenderType(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	/**
	 * @return the code
	 */
	public int code() {
		return code;
	}


	/**
	 * @return the desc
	 */
	public String desc() {
		return desc;
	}
}
