package it.fanciullini.controller;

import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.data.entity.User;
import it.fanciullini.data.service.CommunicationLogService;
import it.fanciullini.data.service.PaymentsLogService;
import it.fanciullini.data.service.UserService;
import it.fanciullini.response.PaymentsLogResponse;
import it.fanciullini.utility.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/home")
public class BaseController {


	private static final Logger logger = LogManager.getLogger(BaseController.class);

	@Autowired
	private MailService mailService;

	@Autowired
	private TelegramBotWrapper telegramBotWrapper;

	@Autowired
	private UserService userService;

	@Autowired
	private PaymentsLogService paymentsLogService;

	@Autowired
	private CommunicationLogService communicationLogService;

	private static int counter = 0;
	private static final String VIEW_INDEX = "index";
	private static final String VIEW_WELCOME_PAGE = "welcome";
	private static final String LOGIN_ERROR = "login_error";
	private String session;

	private ModelMap modelStatic;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String firstLogin(@RequestParam String username, @RequestParam String password, ModelMap model) {

		List<User> user = userService.findByUsernameAndPassword(username, password);
		if (user.size()==1){
			User selectedUser = user.get(0);
			session = selectedUser.getUsername();
			return "redirect:/home/"+VIEW_WELCOME_PAGE;
		} else {
			return LOGIN_ERROR;
		}
	}

	@RequestMapping(value = "/welcome")
	private String welcome(ModelMap model, Integer page, HttpServletRequest httpServletRequest){
		if (page == null){
			page = 0;
		}
		Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
		if (parameterMap.containsKey("pagenumber")){
			page = Integer.parseInt(parameterMap.get("pagenumber")[0]);
		} else {
			page = 0;
		}
		model = loadValues(model, page);
		return VIEW_WELCOME_PAGE;
	}

	private ModelMap loadValues(ModelMap model, Integer page){
		int limit = 10;
		Pageable pageRequest = new PageRequest(page, limit);
		return loadValues(model, pageRequest);
	}

	private ModelMap loadValues(ModelMap model, Pageable pageRequest){
		User selectedUser = userService.findByUserName(session);
		selectedUser.setPassword("");
		Integer pageNumber = pageRequest.getPageNumber();
		List<PaymentsLogResponse> paymentsLogResponse = new ArrayList<>();
		try {
			paymentsLogResponse = paymentsLogService.getFilteredList(pageNumber);
		} catch(ArrayIndexOutOfBoundsException ex){
			paymentsLogResponse = paymentsLogService.getFilteredList(--pageNumber);
		}
		model.addAttribute("user", selectedUser.getName());
		model.addAttribute("usersList", userService.getFilteredList(selectedUser.getRole()));
		model.addAttribute("paymentsLogList", paymentsLogResponse);
		model.addAttribute("pageNumber", pageNumber);
		return model;
	}

	@RequestMapping(value = "/changepage", method = RequestMethod.POST)
	public String change(@RequestParam Integer pagenumber, ModelMap model, final RedirectAttributes redirectAttributes){
		if(pagenumber<0){
			model = loadValues(model, ++pagenumber);
			return "redirect:/home/"+VIEW_WELCOME_PAGE;
		}
		redirectAttributes.addAttribute("pagenumber", pagenumber);
		//model = loadValues(model, pagenumber);
		return "redirect:/home/"+VIEW_WELCOME_PAGE;
	}

	@RequestMapping(value = "/sendwarning", method = RequestMethod.POST)
	public String sendWarning(@RequestParam Long paymentId, ModelMap model){
		User senderUser = userService.findByUserName(session);
		PaymentsLog paymentsLog = paymentsLogService.findById(paymentId);
		User poorBoy = paymentsLog.getUser();
		String message = mailService.sendWarning(poorBoy, senderUser, paymentsLog);
		if (!StringUtils.isEmpty(message)) {
			telegramBotWrapper.sendBotMessage(poorBoy.getTelegramId(),
					message.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " "));
		}
		return "redirect:/home/"+VIEW_WELCOME_PAGE;
	}


}