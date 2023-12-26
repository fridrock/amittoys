package com.example.demo.user;

import com.example.demo.user.exceptions.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    public User findByLogin(String login) throws UserNotFoundException{
        Optional<User> founded = userRepository.findByLogin(login);
        return founded.orElseThrow(()-> new UserNotFoundException("User with login:"+login+" not found"));
    }
    public User findByEmail(String email) throws UserNotFoundException{
        Optional<User> founded = userRepository.findByEmail(email);
        return founded.orElseThrow(()->new UserNotFoundException("User with email:"+email+" not found"));
    }
    public User createUser(UserDTO userDTO){
        User newUser = new User();
        newUser.setLogin(userDTO.login());
        newUser.setEmail(userDTO.email());
        newUser.setPasswordHash(encoder.encode(userDTO.password()));
        userRepository.save(newUser);
        return newUser;
    }
}
