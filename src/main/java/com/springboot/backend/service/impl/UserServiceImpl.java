package com.springboot.backend.service.impl;

import com.springboot.backend.config.EmailHelper;
import com.springboot.backend.exception.BadRequestException;
import com.springboot.backend.exception.ResourceNotFoundException;
import com.springboot.backend.model.Area;
import com.springboot.backend.model.ConfirmationToken;
import com.springboot.backend.model.Device;
import com.springboot.backend.model.User;
import com.springboot.backend.repository.AccountLogRepository;
import com.springboot.backend.repository.ConfirmationTokenRepository;
import com.springboot.backend.repository.FieldRepository;
import com.springboot.backend.repository.UserRepository;
import com.springboot.backend.request.UpdateAreaRequest;
import com.springboot.backend.request.UpdateDeviceRequest;
import com.springboot.backend.request.UpdatePasswordRequest;
import com.springboot.backend.request.UpdateUserRequest;
import com.springboot.backend.service.FieldService;
import com.springboot.backend.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Autowired
    private EmailHelper emailHelper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountLogRepository accountLogRepository;
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private FieldService fieldService;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;


//    @Autowired
//    private AccountRepository accountRepository;
    @Autowired
    @Lazy
    public PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getALlUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByID(long id) {
        return userRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("User", "ID",id));
    }

    public User findByEmail(User user){
        return userRepository.findUserByEmail(user.getUserEmail());
    }

    public User authenticateUser(User user) {
        User userExists = userRepository.findByUserEmailIgnoreCase(user.getUserEmail());
        if (userExists == null) {
            throw new BadRequestException("Account not found.");
        }
        String password = user.getPassword();
       /* if (!passwordEncoder.matches(password, userExists.getPassword())) {
            throw new BadRequestException("Password incorrect");
        }*/
//        if (!userExists.getEnabled()) {
//            throw new BadRequestException("The user is not enabled.");
//        }

        System.out.println("log in success");
        return userExists;
    }

    @Override
    public User registerUser(User user) {
        User insertUser = new User();
        LocalDateTime date;

        insertUser.setFirstname(user.getFirstname());
        insertUser.setLastname(user.getLastname());
        insertUser.setUserEmail(user.getUserEmail());
        insertUser.setPassword(user.getPassword());
        //insertUser.setPassword(passwordEncoder.encode(user.getPassword()));
/*
        if (user.getFirstname().isEmpty() || user.getLastname().isEmpty() || user.getUserEmail().isEmpty() || user.getPassword().isEmpty()) {
            throw new BadRequestException("Fill all the fields");
        }*/

        //insertUser.setFields(user.getFields());


        userRepository.save(insertUser);

  /*      ConfirmationToken confirmationToken = new ConfirmationToken(insertUser);

        confirmationTokenRepository.save(confirmationToken);

        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(user.getUserEmail());
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText
                ("Hello " + user.getFirstname()+
                "\n \nYou account activation is underway."+
                " \nPlease activate your account by clicking on the link below,\n" +"http://localhost:8080/api/v1/confirm-account?token="+confirmationToken.getConfirmationToken()+
                "\n \nBest Regards;" +
                "\nMoroka Team");

        registrationEmail.setFrom("support@nimbusengineering.co.bw");
        this.emailHelper.sendEmail(registrationEmail);

        System.out.println("Confirmation Token: " + confirmationToken.getConfirmationToken());*/
        return insertUser;
    }

    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token != null) {
            User user = userRepository.findByUserEmailIgnoreCase(token.getUserEntity().getUserEmail());

            if (user!=null){
                user.setIsEnabled(true);
                user.setIsLocked(false);
                userRepository.save(user);
                return ResponseEntity.ok("Email verified successfully!" );
            }
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }
    @Override
    public User updateUser(UpdateUserRequest updateUserRequest, long id) {
        User userExists = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("user", "Id", id));

        userExists.setFirstname(updateUserRequest.getFirstname());
        userExists.setLastname(updateUserRequest.getLastname());

        userRepository.save(userExists);

        return userExists;
    }
    @Override
    public User updatePassword(UpdatePasswordRequest updatePasswordRequest, long id) {
        User userExists = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User", "Id", id));

        userExists.setPassword(updatePasswordRequest.getPassword());

        return userRepository.save(userExists);
    }

    @Override
    public void deleteUserByID(long id) {
        //check if user exists with that ID
        userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User","Id",id));
        userRepository.deleteById(id);
    }


}
