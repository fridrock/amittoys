package com.example.demo.user;
import com.example.demo.user.dto.AuthDTO;
import com.example.demo.user.dto.CreateDTO;
import com.example.demo.utils.JwtTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    @PostMapping("/reg")
    public ResponseEntity<String> createUser(@RequestBody CreateDTO userDTO){
        if(userService.existsByLoginOrEmail(userDTO.login(), userDTO.email())){
            return new ResponseEntity<>("Such user already exist",HttpStatus.FOUND);
        }else{
            userService.createUser(userDTO);
            var username = userDTO.login()==null?userDTO.email():userDTO.login();
            //generating token
            var ud = userDetailsService.loadUserByUsername(username);
            String token = jwtTokenUtils.generateToken(ud);
            //authenticate user
            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(username, userDTO.password());
            authenticationManager.authenticate(authReq);
            return ResponseEntity.ok(token);
        }
    }
    @PostMapping("/auth")
    public ResponseEntity<String> authUser(@RequestBody AuthDTO authDTO){
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());
        try{
            authenticationManager.authenticate(authReq);
        }catch(BadCredentialsException e){
            return new ResponseEntity<>("Wrong credentials", HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(authDTO.username());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }
}
