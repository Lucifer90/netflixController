package it.fanciullini.controller;

import it.fanciullini.model.User;
import it.fanciullini.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BaseController {

	@Autowired
	private UserService userService;

	private static int counter = 0;
	private static final String VIEW_INDEX = "index";
	private static final String VIEW_WELCOME_PAGE = "welcome";
	private static final String LOGIN_ERROR = "login_error";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(ModelMap model) {

		model.addAttribute("message", "Welcome");
		model.addAttribute("counter", ++counter);

		// Spring uses InternalResourceViewResolver and return back index_fanciu.jsp
		return VIEW_INDEX;

	}

	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public String welcomeName(@PathVariable String name, ModelMap model) {

		model.addAttribute("message", "Welcome " + name);
		model.addAttribute("counter", ++counter);
		return VIEW_INDEX;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String firstLogin(@RequestParam String username, @RequestParam String password, ModelMap model) {

		List<User> user = userService.findByUsernameAndPassword(username, password);
		if (user.size()==1){
			User selectedUser = user.get(0);
			selectedUser.setPassword("");
			model.addAttribute("user", selectedUser.getName());
			return VIEW_WELCOME_PAGE;
		} else {
			return LOGIN_ERROR;
		}
	}

}