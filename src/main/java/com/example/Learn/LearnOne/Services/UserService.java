package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Users;
import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Repository.UserRepository;
import com.example.Learn.LearnOne.Repository.VoterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VoterRepository voterRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, VoterRepository voterRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.voterRepository = voterRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Users> authenticate(String email, String password) {
        Optional<Users> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            user.get().setLastLogin(LocalDateTime.now());
            userRepository.save(user.get());
            logger.info("User authenticated successfully: {}", email);
            return user;
        }
        logger.warn("Authentication failed for email: {}", email);
        return Optional.empty();
    }

    public void registerUser(Users user) {
        if (user == null || user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid user or password");
        }

        logger.info("Registering user - Email: {}", user.getEmail());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Users.Role.USER);
        }

        Optional<Voter> existingVoter = voterRepository.findByFullName(user.getFullName());
        if (existingVoter.isPresent()) {
            Voter voter = existingVoter.get();
            voter.setUser(user);
            if (user.getVoter() == null) {
                user.setVoter(new ArrayList<>());
            }
            user.getVoter().add(voter);
            logger.info("Linked existing Voter with fullName: {} to User: {}", voter.getFullName(), user.getEmail());
        }

        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getEmail());
    }

    public List<Users> filterUsers(String email, Users.Role role, String constituency, Boolean isVolunteer, Boolean isWelfareBeneficiary, String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.searchUsers(keyword);
        }
        if (email != null && !email.isEmpty()) {
            return userRepository.findByEmail(email).map(List::of).orElse(List.of());
        }
        if (role != null) {
            return userRepository.findByRole(role);
        }
        if (constituency != null && !constituency.isEmpty()) {
            return userRepository.findByConstituency(constituency);
        }
        if (isVolunteer != null && isVolunteer) {
            return userRepository.findByVolunteerTrue(); // Updated method name
        }
        if (isWelfareBeneficiary != null && isWelfareBeneficiary) {
            return userRepository.findByWelfareBeneficiaryTrue(); // Updated method name
        }
        return userRepository.findAll();
    }

    public Users updateUser(Long id, Users updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setFullName(updatedUser.getFullName());
            user.setEmail(updatedUser.getEmail());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setAddress(updatedUser.getAddress());
            user.setConstituency(updatedUser.getConstituency());
            user.setRole(updatedUser.getRole());
            user.setIssueDescription(updatedUser.getIssueDescription());
            user.setIssueStatus(updatedUser.getIssueStatus());
            user.setSubmittedAt(updatedUser.getSubmittedAt());
            user.setVolunteer(updatedUser.getVolunteer()); // Updated getter
            user.setDonationType(updatedUser.getDonationType());
            user.setDonationDate(updatedUser.getDonationDate());
            user.setWelfareBeneficiary(updatedUser.getWelfareBeneficiary()); // Updated getter
            user.setLastWelfareSupportDate(updatedUser.getLastWelfareSupportDate());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<Users> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}