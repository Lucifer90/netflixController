package it.fanciullini.data.entity;

import it.fanciullini.utility.StatusEnum;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity(name = "PaymentsLog")
@Table(name = "payments_log")
public class PaymentsLog implements Serializable
{

    public PaymentsLog(){}

    public PaymentsLog(PaymentsLog paymentsLog){
        this.id = paymentsLog.getId();
        this.user = paymentsLog.getUser();
        this.quantity = paymentsLog.getQuantity();
        this.paymentDate = paymentsLog.getPaymentDate();
        this.startServicePeriod = paymentsLog.getStartServicePeriod();
        this.endServicePeriod = paymentsLog.getEndServicePeriod();
        this.payed = paymentsLog.getPayed();
    }

    public PaymentsLog(Long id, User user, Double quantity, Date paymentDate,
                       Date startServicePeriod, Date endServicePeriod, StatusEnum payed){
        this.id = id;
        this.user = user;
        this.quantity = quantity;
        this.paymentDate = paymentDate;
        this. startServicePeriod = startServicePeriod;
        this.endServicePeriod = endServicePeriod;
        this.payed = payed;
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "start_service_period")
    private Date startServicePeriod;

    @Column(name = "end_service_period")
    private Date endServicePeriod;

    @Column(name = "payed")
    private StatusEnum payed;
}
