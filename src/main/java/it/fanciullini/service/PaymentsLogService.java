package it.fanciullini.service;

import it.fanciullini.model.PaymentsLog;

import java.util.List;

public interface PaymentsLogService {
    void save(PaymentsLog paymentsLog);
    List<PaymentsLog> list();
}
