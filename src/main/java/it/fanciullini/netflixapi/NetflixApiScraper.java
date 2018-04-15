package it.fanciullini.netflixapi;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class NetflixApiScraper {
    final String USER_AGENT = "\"Mozilla/5.0 (Windows NT\" +\n" +
            "          \" 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2\"";

    @Value("${http.login.url}")
    private String loginFormUrl = "https://www.netflix.com/it/login";

    @Value("${http.login.action.url}")
    private String loginActionUrl = "https://www.netflix.com/it/login";

    @Value("${http.your.account.page}")
    private String yourAccountPage = "https://www.netflix.com/YourAccount";

    @Value("${http.billing.activity.page}")
    private String billingActivityPage = "https://www.netflix.com/BillingActivity";

    @Value("${smtp.config.sender}")
    private String username;

    @Value("${smtp.config.password}")
    private String password;

    private Map<String, String> loginCookies = new HashMap<>();

    public void login() throws IOException {
        HashMap<String, String> cookies = new HashMap<>();
        HashMap<String, String> formData = new HashMap<>();

        Connection.Response loginForm = Jsoup.connect(loginFormUrl)
                .method(Connection.Method.GET)
                .userAgent(USER_AGENT)
                .execute();
        Document loginDoc = loginForm.parse(); // this is the document that contains response html
        cookies.putAll(loginForm.cookies()); // save the cookies, this will be passed on to next request

        String authToken = loginDoc.getElementsByAttributeValue("name", "authURL").val();
        formData.put("email", username);
        formData.put("password", password);
        formData.put("rememberMe", "true");
        formData.put("flow", "websiteSignUp");
        formData.put("mode", "login");
        formData.put("action", "loginAction");
        formData.put("withFields", "email,password,rememberMe,nextPage,showPassword");
        formData.put("authURL", authToken);
        formData.put("nextPage", "");
        formData.put("showPassowrd", "");

        Connection.Response homePage = Jsoup.connect(loginActionUrl)
                .cookies(cookies)
                .data(formData)
                .method(Connection.Method.POST)
                .userAgent(USER_AGENT)
                .execute();

        this.loginCookies = homePage.cookies();
    }

    public Document getYourAccountPage() throws IOException {
        return browse(yourAccountPage);
    }

    public Document getBillingActivityPage() throws IOException {
        return browse(billingActivityPage);
    }

    private Document browse(String url) throws IOException {
        return Jsoup.connect(url)
                .cookies(loginCookies)
                .get();
    }


}
