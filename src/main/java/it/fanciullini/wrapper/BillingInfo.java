package it.fanciullini.wrapper;

import lombok.Data;

import java.util.Date;

@Data
public class BillingInfo {

    private Double cost;
    private Date nextPayment;
    private Double bonusRemnants;

}
