package it.fanciullini.service;

import it.fanciullini.dao.PaymentsLogDao;
import it.fanciullini.model.PaymentsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentsLogServiceImp implements PaymentsLogService {

    @Autowired
    private PaymentsLogDao paymentsLogDao;

    @Transactional
    public void save(PaymentsLog paymentsLog) {
        paymentsLogDao.save(paymentsLog);
    }

    @Transactional
    public List<PaymentsLog> list() {
        return paymentsLogDao.list();
    }
}
