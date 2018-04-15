package it.fanciullini.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@Data
public class ConfigBean {

    @Value("${payment.warning.threshold}")
    private Integer paymentWarningThreshold;

    @Value("${notification.warning.threshold}")
    private Integer notificationWarningThreshold;

    @Value("${telegram.bot.token}")
    private String token = "558377851:AAHCVXIq8VaWzhamShUhzSV5iEoNL9P0XUc";

    @Value("${smtp.config.host}")
    private String smtpHost;

    @Value("${smtp.config.sender}")
    private String sender;

    @Value("${smtp.config.password}")
    private String password;

    @Value("${smtp.config.senderanag}")
    private String senderName;

    @Value("${smtp.config.port}")
    private Integer smtpPort;

    @Value("${smtp.dry.run}")
    private Boolean dryRun;

    @Value("${http.login.url}")
    private String loginFormUrl = "https://www.netflix.com/it/login";

    @Value("${http.login.action.url}")
    private String loginActionUrl = "https://www.netflix.com/it/login";

    @Value("${http.your.account.page}")
    private String yourAccountPage = "https://www.netflix.com/YourAccount";

    @Value("${http.billing.activity.page}")
    private String billingActivityPage = "https://www.netflix.com/BillingActivity";

}
