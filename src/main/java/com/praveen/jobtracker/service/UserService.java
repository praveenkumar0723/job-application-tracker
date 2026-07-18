package com.praveen.jobtracker.service;

import com.praveen.jobtracker.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    void deleteUser(Long id);

    User findByEmail(String email);

    User updateProfile(User user);

    String changePassword(String email,
                          String currentPassword,
                          String newPassword,
                          String confirmPassword);

}