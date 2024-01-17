package com.example.demo.roles;

import jakarta.persistence.*;
import lombok.Getter;

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
}
