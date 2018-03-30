package it.fanciullini.data.service;

import it.fanciullini.data.entity.PaymentsLog;
import it.fanciullini.data.entity.User;
import it.fanciullini.data.repo.UserRepository;
import it.fanciullini.response.UsersResponse;
import it.fanciullini.utility.Roles;
import org.hibernate.exception.DataException;
import org.hibernate.service.spi.ServiceException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static it.fanciullini.utility.Utils.calculateDifference;

@Service
public class UserService
{


    @Value("${notification.warning.threshold}")
    private Integer notificationWarningThreshold;

    @Autowired
    UserRepository usersRepository;

    @Autowired
    PaymentsLogService paymentsLogService;

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
            if(role.hasPermission(usr.getRole()) && !usr.getHidden()) {
                UsersResponse user = new UsersResponse(usr);
                user.setImportTotal(paymentsLogService.importTotalByUser(usr));
                user.setPassword("");
                user.setMail("");
                usersToReturn.add(user);
            }
        }
        return usersToReturn;
    }

    public User getPayer(PaymentsLog paymentsLog){
        DateTime lastWarning = new DateTime(paymentsLog.getPaymentDate());
        Long timeDiff = calculateDifference(lastWarning);
        if (timeDiff <= notificationWarningThreshold * 60 * 60 * 1000 ){
            return paymentsLog.getUser();
        }
        return null;
    }

    public User findByTelegramId(Long telegramId){
        return usersRepository.findByTelegramId(telegramId);
    }

    public User findByNameAndSurname(String name, String surname){
        return usersRepository.findByNameAndSurname(name, surname);
    }
    public User save(User user){
        return usersRepository.save(user);
    }

}
