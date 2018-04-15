package it.fanciullini.controller;

import it.fanciullini.data.service.UserService;
import it.fanciullini.module.TelegramBot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@Controller
public class TelegramBotWrapper
{

    @Value("${telegram.bot.token}")
    private String token = "558377851:AAHCVXIq8VaWzhamShUhzSV5iEoNL9P0XUc";

    @Autowired
    private UserService userService;

    private TelegramBot telegramBot;

    private static Logger logger = LogManager.getLogger();

    @PostConstruct
    public void init(){
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            telegramBot = new TelegramBot(token, userService);
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            logger.error("Ther is already one telegram bot server on!");
        }
    }

    public void sendBotMessage(Long chatId, String message){
        if (chatId != null) {
            telegramBot.sendNotification(chatId, message);
        }
    }

}
