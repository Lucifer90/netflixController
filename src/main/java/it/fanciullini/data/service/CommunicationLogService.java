package it.fanciullini.data.service;

import it.fanciullini.data.entity.CommunicationLog;
import it.fanciullini.data.entity.User;
import it.fanciullini.data.repo.CommunicationLogRepository;
import it.fanciullini.utility.TypeCommunication;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static it.fanciullini.utility.Utils.calculateDifference;

@Service
public class CommunicationLogService {

    @Value("${notification.warning.threshold}")
    private Integer notificationWarningThreshold;

    @Autowired
    private CommunicationLogRepository communicationLogRepository;

    public void insertCommunicationTrace(User receiver, User sender, String subject, String content){
        CommunicationLog communicationLog = new CommunicationLog();
        communicationLog.setTypeCommunication(TypeCommunication.MAIL);
        communicationLog.setReceiver(receiver);
        communicationLog.setSender(sender);
        communicationLog.setSubject(subject);
        communicationLog.setContent(content);
        communicationLog.setSubmissionDate(new Date());
        try {
            this.save(communicationLog);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public String haveAlreadyBeenWarned(User receiver){
        List<CommunicationLog> communicationLogList = new ArrayList<>();
        try {
            communicationLogList = communicationLogRepository.findByReceiverOrderBySubmissionDateDesc(receiver);
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        if (communicationLogList.size() > 0){
            DateTime lastWarning = new DateTime(communicationLogList.get(0).getSubmissionDate());
            Long timeDiff = calculateDifference(lastWarning);
            if (timeDiff <= notificationWarningThreshold * 60 * 60 * 1000 ){
                return timeDiff.toString();
            }
        }
        return "";
    }

    private void save(CommunicationLog communicationLog){
        communicationLogRepository.save(communicationLog);
    }

}
