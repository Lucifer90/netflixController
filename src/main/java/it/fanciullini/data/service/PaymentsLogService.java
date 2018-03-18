package it.fanciullini.data.service;

import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.data.repo.PaymentsLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentsLogService
{
    @Autowired
    private PaymentsLogRepository paymentsLogRepository;

    public List<PaymentsLog> getList(){
        return paymentsLogRepository.getList();
    }

    public Double importTotalByUser(String username) {
        Double total = paymentsLogRepository.importTotalByUser(username);
        return total != null ? total : new Double(0);
    }

    public PaymentsLog save(PaymentsLog paymentsLog){
        return paymentsLogRepository.save(paymentsLog);
    }
}
