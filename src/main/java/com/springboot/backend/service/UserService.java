package com.springboot.backend.service;

import com.springboot.backend.model.User;
import com.springboot.backend.request.UpdatePasswordRequest;
import com.springboot.backend.request.UpdateUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {

    User registerUser(User user);
    User authenticateUser(User user);

    List<User> getALlUsers();
    User getUserByID(long id);
    User updateUser(UpdateUserRequest updateUserRequest, long id);

    User updatePassword(UpdatePasswordRequest updatePasswordRequest, long id);
    void deleteUserByID(long id);

   // ResponseEntity<?> saveUser(User user);
    ResponseEntity<?> confirmEmail(String confirmationToken);


}

