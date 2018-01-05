package it.fanciullini.data.repo;

import it.fanciullini.data.entity.PaymentsLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PaymentsLogRepository extends PagingAndSortingRepository<PaymentsLog, Long>
{

    public PaymentsLog save(PaymentsLog paymentsLog);

    @Query("SELECT paymentsLog FROM PaymentsLog paymentsLog")
    public List<PaymentsLog> getList();

}
