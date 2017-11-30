package it.fanciullini.service;

import it.fanciullini.model.User;

import java.util.List;

public interface UserService {
   void save(User user);

   List<User> list();

   List<User> findByUsernameAndPassword(String username, String password);
}
