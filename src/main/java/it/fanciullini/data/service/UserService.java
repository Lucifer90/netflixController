package it.fanciullini.data.service;

import it.fanciullini.data.entity.User;
import it.fanciullini.data.repo.UserRepository;
import it.fanciullini.response.UsersResponse;
import it.fanciullini.utility.Roles;
import org.hibernate.exception.DataException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService
{
    @Autowired
    UserRepository usersRepository;

    @Autowired
    PaymentsLogService paymentsLogService;

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
        try {
            List<User> userList = usersRepository.findByUsernameAndPassword(username, password);
            if (userList.size() == 1) {
                return userList;
            } else {
                throw new ServiceException("No user found");
            }
        } catch(ServiceException sex){
            throw new ServiceException(sex.getMessage());
        } catch(Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public List<User> getList(){
        return usersRepository.getList();
    }

    public List<UsersResponse> getFilteredList(Roles role){
        List<User> tmp = usersRepository.getList();
        List<UsersResponse> usersToReturn = new ArrayList<>();
        for(User usr : tmp){
            if(role.hasPermission(usr.getRole())) {
                UsersResponse user = new UsersResponse(usr);
                user.setImportTotal(paymentsLogService.importTotalByUser(usr.getUsername()));
                user.setPassword("");
                usersToReturn.add(user);
            }
        }
        return usersToReturn;
    }

    public User save(User user){
        return usersRepository.save(user);
    }

}
