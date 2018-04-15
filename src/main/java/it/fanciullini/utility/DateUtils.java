package it.fanciullini.utility;

import it.fanciullini.data.entity.PaymentsLog;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static StatusEnum checkPayedStatus(PaymentsLog paymentsLog, int paymentWarningThreshold){
        StatusEnum currentStatus = paymentsLog.getPayed();
        Date paymentDate = paymentsLog.getPaymentDate();
        if (currentStatus==StatusEnum.PAYED) {
            return StatusEnum.PAYED;
        } else if (paymentDate.after(calculateThreshold(paymentWarningThreshold))){
            return StatusEnum.TOBEPAYED;
        } else if (paymentDate.before(new Date())){
            return StatusEnum.EXPIRED;
        } else {
            return StatusEnum.INPAYMENT;
        }
    }

    public static Date calculateThreshold(int paymentWarningThreshold){
        return calculateThreshold(new Date(), paymentWarningThreshold);
    }

    public static Date calculateThreshold(Date dateInstance, int paymentWarningThreshold){
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateInstance);
        cal.add(Calendar.DATE, + paymentWarningThreshold);
        return cal.getTime();
    }
}
