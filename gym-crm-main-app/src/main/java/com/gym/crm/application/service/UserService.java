package com.gym.crm.application.service;

import com.gym.crm.application.entity.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User findById(Long id);

    User findByUsername(String username);

    void update(User user);

    void delete(User user);

    void activateUser(String username, boolean isActive);

    List<User> getAll();

    void changePassword(User user, String newPassword);

    boolean isAuthenticate(String username, String password);

    String createUsername(String baseUsername);

}
