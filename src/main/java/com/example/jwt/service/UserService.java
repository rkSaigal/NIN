package com.example.jwt.service;



import com.example.jwt.config.AppConstants;
import com.example.jwt.entities.Role;
import com.example.jwt.entities.User;
import com.example.jwt.exception.UserAlreadyExistsException;
import com.example.jwt.registration.RegistrationRequest;
import com.example.jwt.registration.token.VerificationToken;
import com.example.jwt.registration.token.VerificationTokenRepository;
import com.example.jwt.repository.RoleRepo;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;


    private final VerificationTokenRepository tokenRepository;



    public List<User> getUser(){

        return userRepository.findAll();
    }

//    public User createUser(User user)
//    {
////        user.setUserId(UUID.randomUUID().toString());
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return userRepository.save(user);
//    }


    @Override
    public User registerUser(RegistrationRequest request) {
        Optional<User> user = this.findByEmail(request.email());
        if (user.isPresent()){
            throw new UserAlreadyExistsException(
                    "User with email "+request.email() + " already exists");
        }
        var newUser = new User();
//        newUser.setFirstName(request.firstName());
//        newUser.setLastName(request.lastName());
        newUser.setMobileNo(request.mobileNo());
        newUser.setUserName(request.userName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        //roles
        Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();

        newUser.getRoles().add(role);

//        newUser.setRole(request.role());
        return userRepository.save(newUser);
    }



    @Override
    public Optional<User>   findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = (User) token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            tokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }















}
