package it.fanciullini.job;
import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.data.entity.User;
import it.fanciullini.data.service.PaymentsLogService;
import it.fanciullini.data.service.UserService;
import it.fanciullini.response.PaymentsLogResponse;
import it.fanciullini.utility.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;



@Component
public class Parser {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentsLogService paymentsLogService;

    @Autowired
    private MailService mailService;


    private static final Logger logger = LoggerFactory.getLogger(Parser.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void scheduleTaskWithFixedRate() {}

    public void scheduleTaskWithFixedDelay() {}

    @Scheduled(fixedRate = 43200000, initialDelay = 5000) //ms
    public void scheduleTaskWithInitialDelay() {
        logger.info("Fixed Rate Task with Initial Delay :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        PaymentsLog paymentsLog = paymentsLogService.getFirstFuturePayment();
        User poorBoy = userService.getPayer(paymentsLog);
        User senderUser = userService.findByUserName("Luciferino");
        String message = mailService.sendWarning(poorBoy, senderUser, paymentsLog);
    }

    public void scheduleTaskWithCronExpression() {}
}


