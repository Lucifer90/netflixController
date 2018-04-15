package it.fanciullini.job;


import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.data.entity.User;
import it.fanciullini.netflixapi.NetflixScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;

@Component
public class Scraper {

    @Autowired
    private NetflixScraperService netflixScraperService;

    //@Scheduled(fixedRate = 43200000, initialDelay = 5000) //ms
    @Scheduled(fixedRate = 43200000, initialDelay = 0) //ms
    public void scheduleTaskWithInitialDelay() {
        netflixScraperService.areWeFine();
    }

}
