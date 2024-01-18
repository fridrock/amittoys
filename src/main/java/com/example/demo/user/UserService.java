package com.example.demo.user;

import com.example.demo.roles.Role;
import com.example.demo.roles.RoleRepository;
import com.example.demo.roles.RoleService;
import com.example.demo.user.dto.AuthDTO;
import com.example.demo.user.dto.CreateDTO;
import com.example.demo.user.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final PasswordEncoder encoder;
    private final RoleService roleService;
    private final UserRepository userRepository;
    public User findByLogin(String login) throws UserNotFoundException{
        Optional<User> founded = userRepository.findByLogin(login);
        return founded.orElseThrow(()-> new UserNotFoundException("User with login:"+login+" not found"));
    }
    public User findByEmail(String email) throws UserNotFoundException{
        Optional<User> founded = userRepository.findByEmail(email);
        return founded.orElseThrow(()->new UserNotFoundException("User with email:"+email+" not found"));
    }

    public boolean existsByLoginOrEmail(String login, String email){
        return userRepository.findByLoginOrEmail(login, email).isPresent();
    }
    public User createUser(CreateDTO userDTO){
        User newUser = new User();
        newUser.setLogin(userDTO.login());
        newUser.setEmail(userDTO.email());
        newUser.setPasswordHash(encoder.encode(userDTO.password()));
        //TODO remove this role choose
        String userRoleName = newUser.getLogin().startsWith("a")?"ADMIN":"USER";
        Role role;
        try{
            role = roleService.getRoleByName(userRoleName);
            newUser.setRoles(List.of(role));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        userRepository.save(newUser);
        return newUser;
    }
}
