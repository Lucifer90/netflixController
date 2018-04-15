package it.fanciullini.response;

import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.utility.DateUtils;
import it.fanciullini.utility.StatusEnum;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Data
@PropertySource("classpath: application.properties")
public class PaymentsLogResponse extends PaymentsLog implements Serializable {

    private String pagante;

    public PaymentsLogResponse(PaymentsLog paymentsLog, Integer paymentWarningThreshold){
        super(paymentsLog);
        this.pagante = paymentsLog.getUser().getName()+" "+paymentsLog.getUser().getSurname();
        this.setPayed(DateUtils.checkPayedStatus(paymentsLog, paymentWarningThreshold));
    }

}
