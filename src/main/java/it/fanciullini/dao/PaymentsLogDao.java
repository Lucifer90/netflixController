package it.fanciullini.dao;

import it.fanciullini.model.PaymentsLog;

import java.util.List;

public interface PaymentsLogDao {
    void save(PaymentsLog paymentsLog);
    List<PaymentsLog> list();
}
