package com.CreatorEconomyApplication.model.entity;

import java.util.List;

import com.CreatorEconomyApplication.model.enums.UserStatus;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @NotBlank(message = "Username is required")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Email(message = "Invalid email address")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role = "USER"; // Default role

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Content> contents;

    // Constructor
    public User() {}
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public boolean checkPassword(String inputPassword) {
        return BCrypt.verifyer().verify(inputPassword.toCharArray(), password.toCharArray()).verified;
    }
    public void changePassword(String newPassword) {
        // Add validation logic here
        this.password = newPassword;
    }
    
    // Public getters and setters (except password)
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    
    public List<Content> getContents() { return contents; }
    public void setContents(List<Content> contents) { this.contents = contents; }
}
