package it.fanciullini.controller;

import it.fanciullini.data.entity.User;
import it.fanciullini.data.service.PaymentsLogService;
import it.fanciullini.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/home")
public class BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private PaymentsLogService paymentsLogService;

	private static int counter = 0;
	private static final String VIEW_INDEX = "index";
	private static final String VIEW_WELCOME_PAGE = "welcome";
	private static final String LOGIN_ERROR = "login_error";


	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String firstLogin(@RequestParam String username, @RequestParam String password, ModelMap model) {

		List<User> user = userService.findByUsernameAndPassword(username, password);
		if (user.size()==1){
			User selectedUser = user.get(0);
			selectedUser.setPassword("");
			model.addAttribute("user", selectedUser.getName());
			model.addAttribute("usersList", userService.getList());
			model.addAttribute("paymentsLogList", paymentsLogService.getList());
			return VIEW_WELCOME_PAGE;
		} else {
			return LOGIN_ERROR;
		}
	}

}