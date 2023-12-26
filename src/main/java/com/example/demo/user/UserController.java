package com.example.demo.user;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @PostMapping("/reg")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO){
        if(userService.existsByLoginOrEmail(userDTO.login(), userDTO.email())){
            return new ResponseEntity<>("Such user already exist",HttpStatus.FOUND);
        }else{
            userService.createUser(userDTO);
            return new ResponseEntity<>("Successfully created new user", HttpStatus.OK);
        }
    }
}
