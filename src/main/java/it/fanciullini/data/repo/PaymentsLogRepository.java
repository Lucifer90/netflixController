package it.fanciullini.data.repo;

import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.utility.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentsLogRepository extends PagingAndSortingRepository<PaymentsLog, Long>
{

    public PaymentsLog save(PaymentsLog paymentsLog);

    public Optional<PaymentsLog> findById(Long id);

    @Query("SELECT SUM (paymentsLog.quantity) FROM PaymentsLog paymentsLog WHERE username = :username AND payed = :statusEnum")
    public Double importTotalByUser(@Param("username") String username, @Param("statusEnum") StatusEnum statusEnum);

    @Query("SELECT paymentsLog FROM PaymentsLog paymentsLog ORDER BY paymentDate DESC")
    public Page<PaymentsLog> getList(Pageable pageRequest);

    public List<PaymentsLog> getByPayedOrderByPaymentDateAsc(StatusEnum statusEnum);

    public PaymentsLog findFirstByPayedOrderByPaymentDateDesc(StatusEnum statusEnum);

    public PaymentsLog findFirstByPayedNotOrderByPaymentDateAsc(StatusEnum statusEnum);

    @Query("SELECT paymentslog FROM PaymentsLog paymentslog " +
            "GROUP BY paymentslog.user ORDER BY SUM(paymentslog.quantity), MAX(id)")
    public List<PaymentsLog> getPayer(Pageable pageable);

}

