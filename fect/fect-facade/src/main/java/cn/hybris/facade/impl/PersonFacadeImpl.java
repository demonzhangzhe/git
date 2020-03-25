package cn.hybris.facade.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hybris.common.service.AlipaySignService;
import cn.hybris.common.stereotype.Facade;
import cn.hybris.core.entity.model.PersonModel;
import cn.hybris.core.service.PersonService;
import cn.hybris.facade.PersonFacade;

@Facade("personFacade")
public class PersonFacadeImpl implements PersonFacade
{
	@Resource
	private PersonService personService;

	@Resource
	private AlipaySignService alipaySignService;

	@Override
	public List<String> getPersonNames() {
		final List<PersonModel> persons = personService.findPersons();
		alipaySignService.check("");
		return persons.stream().map(PersonModel::getName).collect(Collectors.toList());
	}

}
