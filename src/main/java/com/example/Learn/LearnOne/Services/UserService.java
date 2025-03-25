package com.example.Learn.LearnOne.Services;





import com.example.Learn.LearnOne.Entity.Users;
import com.example.Learn.LearnOne.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Users> authenticate(String email, String password) {
        Optional<Users> user = userRepository.findByEmail(email); // Fetch user by email
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            logger.info("User authenticated successfully: {}", email);
            return user; // Return the Optional containing the User
        }
        logger.warn("Authentication failed for email: {}", email);
        return Optional.empty(); // Return an empty Optional if authentication fails
    }


    public void registerUser(Users user) {
        // Enhanced null checks
        if (user == null) {
            throw new IllegalArgumentException("User object is null");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Debug logging
        logger.info("Registering user - Email: {}, Password: {}",
                user.getEmail(),
                user.getPassword() != null ? "provided" : "null");

        // Existing validation
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        // Password encoding
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Default role
        if (user.getRole() == null) {
            user.setRole(Users.Role.USER);
        }

        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getEmail());
    }



    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<Users> findById(Long id) {
        return userRepository.findById(id);
    }
}
