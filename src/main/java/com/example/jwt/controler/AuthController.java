package com.example.jwt.controler;


import com.example.jwt.entities.User;
import com.example.jwt.model.JwtRequest;
import com.example.jwt.model.JwtResponse;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
//@RequestMapping("/api/v2/auth/")
@Tag(name = "AuthController", description = "Api for Authentication")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userDao;


    private Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
    {
            this.doAuthenticate(request.getEmail(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

      Optional<User> user = userDao.findByEmail(request.getEmail());

      User usr = user.get();



        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .userId(usr.getUserId().toString())
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
                manager.authenticate(authentication);
        }catch (BadCredentialsException e){
                throw new BadCredentialsException("Invalid Username or Password !!");
        }


    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler(){

        return "Credentials Invalid !!";
    }

//    @PostMapping("/create-user")
//    public User createUser(@RequestBody User user){
//        return userService.createUser(user);
//    }


}
