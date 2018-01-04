package it.fanciullini.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity(name = "PaymentsLog")
@Table(name = "payments_log")
public class PaymentsLog {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "payment_date")
    private Date paymentDate;

    @Column(name = "start_service_period")
    private Date startServicePeriod;

    @Column(name = "end_service_period")
    private Date endServicePeriod;

    @Column(name = "payed")
    private Boolean payed;
}
