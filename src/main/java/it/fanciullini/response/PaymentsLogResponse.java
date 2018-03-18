package it.fanciullini.response;

import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.utility.StatusEnum;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.util.Calendar;
import java.util.Date;

@Data
@PropertySource("classpath: application.properties")
public class PaymentsLogResponse extends PaymentsLog {

    private String pagante;

    public PaymentsLogResponse(PaymentsLog paymentsLog, Integer paymentWarningThreshold){
        super(paymentsLog);
        this.pagante = paymentsLog.getUser().getName()+" "+paymentsLog.getUser().getSurname();
        this.setPayed(checkPayedStatus(paymentWarningThreshold));
    }

    private StatusEnum checkPayedStatus(int paymentWarningThreshold){
        StatusEnum currentStatus = this.getPayed();
        Date paymentDate = this.getPaymentDate();
        if (currentStatus==StatusEnum.PAYED) {
            return StatusEnum.PAYED;
        } else if (paymentDate.after(this.calculateThreshold(paymentWarningThreshold))){
            return StatusEnum.TOBEPAYED;
        } else if (paymentDate.before(new Date())){
            return StatusEnum.EXPIRED;
        } else {
            return StatusEnum.INPAYMENT;
        }
    }

    private Date calculateThreshold(int paymentWarningThreshold){
        Date dateInstance = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateInstance);
        cal.add(Calendar.DATE, + paymentWarningThreshold);
        return cal.getTime();
    }


}
