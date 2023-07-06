package com.example.contacts.interfaces;

import com.example.contacts.entities.User;

public interface UserServiceInterface {
    User findByUsername(String username);
    User saveUser(User user);
    boolean isUsernameTaken(String username);
}
