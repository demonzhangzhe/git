package cn.hybris.core.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.hybris.core.entity.model.PersonModel;

/**
 * @author Qu Dihuai
 *
 */
@Mapper
public interface PersonDao extends BaseMapper<PersonModel> {

}
