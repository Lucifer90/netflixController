package it.fanciullini.job;
import it.fanciullini.controller.TelegramBotWrapper;
import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.data.entity.User;
import it.fanciullini.data.service.PaymentsLogService;
import it.fanciullini.data.service.UserService;
import it.fanciullini.utility.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



@Component
public class MailAlert {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentsLogService paymentsLogService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TelegramBotWrapper telegramBotWrapper;

    private static Logger logger = LogManager.getLogger();
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");




    @Scheduled(fixedRate = 43200000, initialDelay = 5000) //ms
    public void tryLog(){
        logger.info("INFO");
        logger.debug("DEBUG");
        logger.warn("WARN");
        logger.error("ERROR");
        logger.fatal("FATAL");
    }


    @Scheduled(fixedRate = 43200000, initialDelay = 5000) //ms
    public void scheduleTaskWithInitialDelay() {
        logger.info("Fixed Rate Task with Initial Delay :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        PaymentsLog paymentsLog = paymentsLogService.getFirstFuturePayment();
        if (paymentsLog == null){
            return;
        }
        User poorBoy = userService.getPayer(paymentsLog);
        User senderUser = userService.findByUserName("Luciferino");
        String message = mailService.sendWarning(poorBoy, senderUser, paymentsLog);
        if (!StringUtils.isEmpty(message)) {
            telegramBotWrapper.sendBotMessage(poorBoy.getTelegramId(),
                    message.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " "));
        }
    }

    public void scheduleTaskWithCronExpression() {}
}


