package it.fanciullini.job;


import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.data.entity.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;

@Component
public class Parser {


    @Scheduled(fixedRate = 43200000, initialDelay = 5000) //ms
    public void scheduleTaskWithInitialDelay() {

    }

}
