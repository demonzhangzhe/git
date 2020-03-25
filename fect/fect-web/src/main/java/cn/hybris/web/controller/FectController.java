package cn.hybris.web.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hybris.facade.PersonFacade;


/**
 * @author Qu Dihuai
 *
 */
@RequestMapping("/fect")
@RestController
public class FectController
{
	@Resource
	private PersonFacade personFacade;

	@GetMapping("/welcome")
	public String getNames()
	{
		return "Hello World";
	}
}
