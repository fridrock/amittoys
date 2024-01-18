package com.example.demo.roles;

import com.example.demo.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Table(name="roles")
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="role")
    private String role;
    public Role(){}
    public Role(String role){
        this.role = role;
    }
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
