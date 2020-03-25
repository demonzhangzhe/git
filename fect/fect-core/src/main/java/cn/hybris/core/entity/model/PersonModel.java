package cn.hybris.core.entity.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import cn.hybris.core.entity.enums.GenderType;

/**
 * @author Qu Dihuai
 *
 */
@TableName("tb_person")
public class PersonModel implements Serializable {

	private static final long serialVersionUID = 1085830269537755014L;

	@TableId
	private String id;

	@TableField("name")
	private String name;

	@TableField("age")
	private Integer age;

	@TableField("gender")
	private GenderType gender;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the gender
	 */
	public GenderType getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(GenderType gender) {
		this.gender = gender;
	}
}
