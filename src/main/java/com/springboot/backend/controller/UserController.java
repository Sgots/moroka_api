package com.springboot.backend.controller;

import com.springboot.backend.exception.MessageResponse;
import com.springboot.backend.exception.NotFoundException;
import com.springboot.backend.exception.ResourceNotFoundException;
import com.springboot.backend.model.Device;
import com.springboot.backend.request.LoginRequest;
import com.springboot.backend.model.User;
import com.springboot.backend.repository.UserRepository;
import com.springboot.backend.request.UpdateDeviceRequest;
import com.springboot.backend.request.UpdatePasswordRequest;
import com.springboot.backend.request.UpdateUserRequest;
import com.springboot.backend.service.UserService;
import com.springboot.backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserServiceImpl userServiceimpl;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    public PasswordEncoder passwordEncoder;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    //create user register user REST API
    //http://localhost:8080/api/v1/register
    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@Valid @RequestBody User user) throws SQLIntegrityConstraintViolationException {
        User userExists = userRepository.findUserByEmail(user.getUserEmail());

        if (userExists!=null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.OK);
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken) {
        return userService.confirmEmail(confirmationToken);
    }

    //create user authenticate user REST API
    //http://localhost:8080/api/v1/login-user
    @PostMapping("/signin")
    public ResponseEntity<?> authenticaticateUser(@Valid @RequestBody LoginRequest user) throws Exception {
        User userExists = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userExists==null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Account not found. Please register"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(userExists);
    }

    //create update user by ID REST API
    //http://localhost:8080/api/users/1
    @PostMapping("/update-user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long userId,
                                           @RequestBody UpdateUserRequest updateUserRequest) {
        User updatedUser = userService.updateUser(updateUserRequest, userId);
        return ResponseEntity.ok(updatedUser);
    }

    //create get all users REST API
    //http://localhost:8080//api/getallusers
    @GetMapping("/get-users")
    public List<User> getAllUsers(){
        return userService.getALlUsers();
    }

    //get user by ID RESTAPI
    //http://localhost:8080/api/users/1
    @GetMapping(value="/user/{id}")
    ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User usr = userRepository.findById(id)
                .orElseThrow(()->new NotFoundException("No user with provided ID:" + id));
        return ResponseEntity.ok().body(usr);
    }

    @GetMapping("/get-user/{user_id}")
    public ResponseEntity<User> getUserWithID(@PathVariable("user_id") long user_id){
        return new ResponseEntity<User>(userService.getUserByID(user_id), HttpStatus.OK);
    }

    //create delete user by ID REST API
    //http://localhost:8080/api/users/1

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable ("id") long userId){
        User existingUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("No user with provided ID:" + userId));
        this.userRepository.delete(existingUser);
        return ResponseEntity.ok().build();
    }

}
