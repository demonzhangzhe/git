/**
 * 
 */
package cn.hybris.core.service;

import java.util.List;

import cn.hybris.core.entity.model.PersonModel;

/**
 * @author Qu Dihuai
 *
 */
public interface PersonService {

	List<PersonModel> findPersons();
}
