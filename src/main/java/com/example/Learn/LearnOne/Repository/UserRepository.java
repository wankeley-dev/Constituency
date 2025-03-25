package com.example.Learn.LearnOne.Repository;


import com.example.Learn.LearnOne.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByFullName(String fullName);
    Users findByRole(Users.Role role); // Fetch all users with a given role
}
