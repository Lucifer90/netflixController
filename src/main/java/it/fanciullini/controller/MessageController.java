package it.fanciullini.controller;

import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.data.entity.User;
import it.fanciullini.data.service.UserService;
import it.fanciullini.module.TelegramBot;
import it.fanciullini.utility.AuthToken;
import it.fanciullini.utility.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

@Controller
@RequestMapping(value = "/api")
public class MessageController {

    @Autowired
    private AuthToken authToken;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TelegramBotWrapper telegramBotWrapper;

    @RequestMapping(value="/send", method = RequestMethod.POST)
    public String send(@RequestParam String username, @RequestParam String message,
                             ModelMap model, final RedirectAttributes redirectAttrs) {
        User senderUser = userService.findByUserName(authToken.getSession());
        User poorBoy = userService.findByUserName(username);
        String messaggioPrivato = mailService.sendWarning(poorBoy, senderUser, message);
        if (!StringUtils.isEmpty(message)) {
            telegramBotWrapper.sendBotMessage(poorBoy.getTelegramId(),
                    message.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " "));
        }
        return "redirect:/home/welcome";
    }


}
