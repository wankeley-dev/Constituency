package com.example.Learn.LearnOne.Repository;

import com.example.Learn.LearnOne.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByFullName(String fullName);
    Optional<Users> findByPhoneNumber(String phoneNumber);
    List<Users> findByRole(Users.Role role);
    List<Users> findByConstituency(String constituency);
    List<Users> findByIssueDescriptionIsNotNull();
    List<Users> findByIssueStatus(Users.IssueStatus issueStatus);
    List<Users> findByDonationTypeIsNotNull();
    List<Users> findByVolunteerTrue(); // Updated method name
    List<Users> findByWelfareBeneficiaryTrue(); // Updated method name;
    List<Users> findByLastWelfareSupportDateAfter(LocalDateTime date);

    @Query("SELECT u FROM Users u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.constituency) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Users> searchUsers(@Param("keyword") String keyword);
}