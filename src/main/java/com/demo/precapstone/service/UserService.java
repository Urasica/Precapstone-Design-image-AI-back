package com.demo.precapstone.service;

import com.demo.precapstone.dao.User;
import com.demo.precapstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(String userName, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(userName, encodedPassword);
        userRepository.save(user);
    }
}