package it.fanciullini.netflixapi;

import it.fanciullini.config.ConfigBean;
import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.data.entity.User;
import it.fanciullini.data.service.PaymentsLogService;
import it.fanciullini.data.service.UserService;
import it.fanciullini.utility.DateUtils;
import it.fanciullini.utility.MailService;
import it.fanciullini.utility.StatusEnum;
import it.fanciullini.wrapper.BillingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.jsoup.nodes.Document;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Service
public class NetflixScraperService {

    @Autowired
    private NetflixApiScraper netflixApiScraper;

    @Autowired
    private PaymentsLogService paymentsLogService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigBean configBean;

    @Value("${payment.warning.threshold}")
    private Integer paymentWarningThreshold;

    private void manageNextPayment(BillingInfo billingInfo){
        bonusRemnants(billingInfo);
        getNextPaymentDetails(billingInfo);
    }

    private void bonusRemnants(BillingInfo billingInfo){
        Double toSet = 0d;
        try {
            netflixApiScraper.login();
            Document yourAccount = netflixApiScraper.getYourAccountPage();
            //NeedSome logic, but for now we cannot know the correct field.. so crack down with a porkaround
            if (StringUtils.contains(yourAccount.body().text(), "Il tuo credito di ")) {
                String response = yourAccount.body().text().split("Il tuo credito di ")[1].substring(0,4);
                String amount = response.replace(",", ".");
                toSet = Double.parseDouble(amount);
            }
        } catch (ArrayIndexOutOfBoundsException | IOException ex){
            // do nothing for now
        }
        billingInfo.setBonusRemnants(toSet);
    }

    private void getNextPaymentDetails(BillingInfo billingInfo){
        Double monthlyCost = 0d;
        Date nextPaymentDate = null;
        try {
            netflixApiScraper.login();
            Document document = netflixApiScraper.getBillingActivityPage();
            monthlyCost = monthlyCostParser(document); //This way he is parsing the first money amount.. the bonus
            nextPaymentDate = dateParser(document);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        billingInfo.setCost(monthlyCost);
        billingInfo.setNextPayment(nextPaymentDate);
    }

    private Double monthlyCostParser(Document document){
        String cost = document.getElementsByAttributeValue("data-reactid", "82").text();
        int commaPosition = cost.indexOf(",");
        cost = cost.substring(commaPosition-2, commaPosition+3).replace(",", ".");
        return Double.parseDouble(cost);
    }

    private Date dateParser(Document document){
        String data = document.getElementsByAttributeValue("data-reactid", "90").text();
        Date date = null;
        try {
            date = new SimpleDateFormat("dd MMMM yyyy", Locale.ITALIAN).parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void areWeFine(){
        BillingInfo billingInfo = new BillingInfo();
        manageNextPayment(billingInfo);

        StatusEnum statusPayment = StatusEnum.TOBEPAYED;
        if (billingInfo.getCost() > billingInfo.getBonusRemnants()){
            // Split in two payments
            statusPayment = StatusEnum.TOBEPAYED;
        } else {
            statusPayment = StatusEnum.PAYED;
        }

        PaymentsLog lastPayed = paymentsLogService.getLatestPaymentByPayementStatus(StatusEnum.PAYED);
        PaymentsLog firstUnpayed = paymentsLogService.getLatestPaymentByPayementStatusNot(StatusEnum.PAYED);
        if(DateUtils.calculateThreshold(lastPayed.getPaymentDate(), 1).after(billingInfo.getNextPayment())) {
            return;
        } else if ( firstUnpayed != null
                && firstUnpayed.getPaymentDate().toInstant().equals(billingInfo.getNextPayment().toInstant())) {
            firstUnpayed.setPayed(DateUtils.checkPayedStatus(firstUnpayed, paymentWarningThreshold));
            User senderUser = userService.findByUserName("Luciferino");
            String message = mailService.sendWarning(firstUnpayed.getUser(), senderUser, firstUnpayed);
        } else {
            PaymentsLog paymentsLog = new PaymentsLog();
            paymentsLog.setPayed(statusPayment);
            paymentsLog.setQuantity(billingInfo.getCost());
            paymentsLog.setPaymentDate(billingInfo.getNextPayment());
            paymentsLog.setStartServicePeriod(billingInfo.getNextPayment());

            int timeToAdd = (int) Math.round((billingInfo.getCost() / configBean.getStandardCost()) * paymentWarningThreshold);

            paymentsLog.setEndServicePeriod(DateUtils.calculateThreshold(billingInfo.getNextPayment(), timeToAdd));
            paymentsLog.setUser(paymentsLogService.findPayer());
            paymentsLogService.save(paymentsLog);
        }
    }

}