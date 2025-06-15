package com.CreatorEconomyApplication.repository;

import com.CreatorEconomyApplication.model.entity.User;
import com.CreatorEconomyApplication.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by username
    Optional<User> findByUsername(String username);
    
    // Find user by email
    Optional<User> findByEmail(String email);
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find users by status
    List<User> findByStatus(UserStatus status);
    
    // Find users created between dates
    Page<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Find users by role
    List<User> findByRole(String role);
    
    // Search users by username or email containing keyword
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    Page<User> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Find active users
    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findActiveUsers(@Param("status") UserStatus status);
    
    // Find users with most content
    @Query("SELECT u FROM User u JOIN u.contents c GROUP BY u ORDER BY COUNT(c) DESC")
    Page<User> findUsersWithMostContent(Pageable pageable);
    
    // Count users by status
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
    Long countByStatus(@Param("status") UserStatus status);
    
    // Find users by creation date range
    @Query("SELECT u FROM User u WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate")
    List<User> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                              @Param("endDate") LocalDateTime endDate);
}