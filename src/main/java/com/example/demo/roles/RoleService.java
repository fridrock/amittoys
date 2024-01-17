package com.example.demo.roles;

import com.example.demo.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
        //here we create default roles
        var r1 = new Role("USER");
        var r2 = new Role("ADMIN");
        this.roleRepository.save(r1);
        this.roleRepository.save(r2);
    }
    //TODO make custom exception
    public Role getRoleByName(String role) throws Exception{
        return this.roleRepository.getRole(role).orElseThrow(()->new Exception("No such role"));
    }
}
