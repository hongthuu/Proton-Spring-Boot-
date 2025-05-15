package com.Proton.JavaSpring.service.serviceImpl;

import com.Proton.JavaSpring.entity.User;
import com.Proton.JavaSpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {
    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }
}
