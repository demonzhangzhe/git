/**
 * 
 */
package cn.hybris.core.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.hybris.core.dao.PersonDao;
import cn.hybris.core.entity.model.PersonModel;
import cn.hybris.core.service.PersonService;

/**
 * @author Qu Dihuai
 *
 */
@Service("personService")
public class PersonServiceImpl implements PersonService {
	@Resource
	private PersonDao personDao;

	@Override
	public List<PersonModel> findPersons() {
		return personDao.selectList(null);
	}
}
