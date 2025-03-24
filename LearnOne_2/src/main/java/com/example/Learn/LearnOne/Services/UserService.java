package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.User;
import com.example.Learn.LearnOne.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null; // Invalid credentials
    }
}
