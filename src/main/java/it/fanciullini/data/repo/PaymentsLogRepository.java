package it.fanciullini.data.repo;

import it.fanciullini.data.entity.PaymentsLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentsLogRepository extends PagingAndSortingRepository<PaymentsLog, Long>
{

    public PaymentsLog save(PaymentsLog paymentsLog);

    @Query("SELECT SUM (paymentsLog.quantity) FROM PaymentsLog paymentsLog WHERE username = :username AND payed = True")
    public Double importTotalByUser(@Param("username") String username);

    @Query("SELECT paymentsLog FROM PaymentsLog paymentsLog ORDER BY paymentDate DESC")
    public List<PaymentsLog> getList();

}
