package it.fanciullini.data.repo;

import it.fanciullini.data.entity.CommunicationLog;
import it.fanciullini.data.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunicationLogRepository extends PagingAndSortingRepository<CommunicationLog, Long>
{
    @Query("SELECT cl FROM CommunicationLog cl WHERE receiver = :receiver ORDER BY submissionDate DESC")
    public List<CommunicationLog> findByReceiverOrderBySubmissionDateDesc(@Param("receiver") User receiver);

}
