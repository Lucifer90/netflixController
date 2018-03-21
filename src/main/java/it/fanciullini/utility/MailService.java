package it.fanciullini.utility;
import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.data.entity.User;
import it.fanciullini.data.service.CommunicationLogService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static sun.security.x509.X509CertInfo.SUBJECT;

@Data
@Component
public class MailService {

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

    private Properties props;
    private Session session;
    static final String CONFIGSET = "ConfigSet";
    static final String SUBJECT = "Mail di notifica da NetflixController";
    static final String BODY = String.join(
            System.getProperty("line.separator"),
            "<h1>Netflix sta per scadere, tocca a te pagliaccio!</h1>"
    );

    @Autowired
    private CommunicationLogService communicationLogService;

    @PostConstruct
    public void init(){
        props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties.
        session = Session.getDefaultInstance(props);
    }

    private MimeMessage setMessage(String receiver, String message){
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(sender, senderName));
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            msg.setSubject(SUBJECT);

            msg.setContent(message,"text/html");
            msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return msg;
    }

    private String setMessageNested(String payer, Date expiringDate){
        String header = String.join(
                System.getProperty("line.separator"),
                "<h1>Ciao "+payer+"! Netflix sta per scadere, tocca a te pagliaccio!</h1>"
        );

        return header + "<br>" +
                "L'abbonamento scadrà il "+expiringDate+" e secondo i nostri complessi dati tocca a te pagare!";
    }

    public String sendWarning(User poorBoy, User senderUser, PaymentsLog paymentsLog){
        String receiver = poorBoy.getMail();
        String payer = poorBoy.getName()+" "+poorBoy.getSurname();
        Date expiringDate = paymentsLog.getPaymentDate();
        String mgs = setMessageNested(payer, expiringDate);
        MimeMessage mimeMessage = setMessage(receiver, mgs);
        Transport transport = null;
        try {
            transport = session.getTransport();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        try
        {
            System.out.println("Sending...");
            transport.connect(smtpHost, sender, password);
            if (!dryRun) {
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                communicationLogService.insertCommunicationTrace(poorBoy, senderUser, SUBJECT, mgs);
            }
            System.out.println("Email sent!");
        }
        catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
        }
        finally
        {
            try {
                transport.close();
            } catch (MessagingException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return mgs;
    }





}
