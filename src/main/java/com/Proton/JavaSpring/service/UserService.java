package com.Proton.JavaSpring.service;

import com.Proton.JavaSpring.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();

    User getByUsername(String userName);

    long saveUser(User user);


    User getUserByEmail(String email);
}
