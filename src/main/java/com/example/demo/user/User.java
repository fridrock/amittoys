package com.example.demo.user;

import com.example.demo.roles.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "login")
    private String login;
    @Column(name = "password_hash")
    private String passwordHash;
    @OneToMany
    List<Role> roles;
}
