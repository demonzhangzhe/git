package cn.hybris.demo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 */
@RestController
public class WelcomeController
{
	/**
	 * @return Hello world
	 */
	@RequestMapping("/welcome")
	public String welcome()
	{
		return "Hello worlddemonZZ";
	}
}
