package it.fanciullini.data.repo;

import it.fanciullini.data.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>
{

    public User save(User user);

    User findById(Long id);

    List<User> findByUsername(String username);

    List<User> findByUsernameAndPassword(String userName, String password);

    @Query("SELECT user FROM User user")
    List<User> getList();

}
