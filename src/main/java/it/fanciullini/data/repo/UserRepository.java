package it.fanciullini.data.repo;

import it.fanciullini.data.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>
{

    public User save(User user);

    Optional<User> findById(Long id);

    User findByTelegramId(Long telegramId);

    List<User> findByUsername(String username);

    List<User> findByUsernameAndPassword(String userName, String password);

    User findByNameAndSurname(String name, String surname);


    @Query("SELECT user FROM User user")
    List<User> getList();

}
