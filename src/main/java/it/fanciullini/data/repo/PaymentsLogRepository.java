package it.fanciullini.data.repo;

import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.utility.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentsLogRepository extends PagingAndSortingRepository<PaymentsLog, Long>
{

    public PaymentsLog save(PaymentsLog paymentsLog);

    @Query("SELECT SUM (paymentsLog.quantity) FROM PaymentsLog paymentsLog WHERE username = :username AND payed = :statusEnum")
    public Double importTotalByUser(@Param("username") String username, @Param("statusEnum") StatusEnum statusEnum);

    @Query("SELECT paymentsLog FROM PaymentsLog paymentsLog ORDER BY paymentDate DESC")
    public Page<PaymentsLog> getList(Pageable pageRequest);

}
