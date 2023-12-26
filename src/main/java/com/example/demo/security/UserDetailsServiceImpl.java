package com.example.demo.security;

import com.example.demo.user.UserService;
import com.example.demo.user.exceptions.UserNotFoundException;
import com.example.demo.utils.UserDetailsAdapter;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    //have two options of finding user, 1) find by login, 2) find by email
    @Override
    public UserDetails loadUserByUsername(String username) {
        final String emailRegexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        final Pattern pattern = Pattern.compile(emailRegexp);
        if(pattern.matcher(username).matches()){
            try{
                return new UserDetailsAdapter(userService.findByEmail(username));
            }catch(UserNotFoundException e){
                System.out.println(e.getMessage());
            }
        }else{
            try{
                return new UserDetailsAdapter(userService.findByLogin(username));
            }catch (UserNotFoundException e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
}
