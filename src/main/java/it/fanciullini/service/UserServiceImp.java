package it.fanciullini.service;

import it.fanciullini.dao.UserDao;
import it.fanciullini.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImp implements UserService {

   @Autowired
   private UserDao userDao;

   @Transactional
   public void save(User user) {
      userDao.save(user);
   }

   @Transactional(readOnly = true)
   public List<User> list() {
      return userDao.list();
   }

   @Transactional(readOnly = true)
   public List<User> findByUsernameAndPassword(String username, String password){
      return userDao.getUserByUsernameAndPassword(username, password);
   }

}
