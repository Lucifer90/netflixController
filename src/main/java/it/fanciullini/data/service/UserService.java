package it.fanciullini.data.service;

import it.fanciullini.data.entity.User;
import it.fanciullini.data.repo.UserRepository;
import org.hibernate.exception.DataException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService
{
    @Autowired
    UserRepository usersRepository;

    @Transactional("txManagerAnag")
    public User findByUserName(String userName)
            throws DataException
    {
        List<User> users = usersRepository.findByUsername(userName);
        if (users.size() == 1){
            return users.get(0);
        } else {
            throw new ServiceException("Username non trovato");
        }
    }

    public List<User> findByUsernameAndPassword(String username, String password){
        List<User> userList = usersRepository.findByUsernameAndPassword(username, password);
        if (userList.size() == 1){
            return userList;
        } else {
            throw new ServiceException("No user found");
        }
    }

    public List<User> getList(){
        return usersRepository.getList();
    }

    public User save(User user){
        return usersRepository.save(user);
    }

}
