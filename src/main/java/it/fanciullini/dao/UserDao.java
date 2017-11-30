package it.fanciullini.dao;

import it.fanciullini.model.User;

import java.util.List;

public interface UserDao {
   void save(User user);
   List<User> list();
   List<User> getUserByUsernameAndPassword(String username, String password);
}
