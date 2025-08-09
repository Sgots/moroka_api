package com.springboot.backend.repository;

import com.springboot.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserRepository extends JpaRepository <User, Long> {
    User getUserById(Long userId);
    User findByUserEmailIgnoreCase(String emailId);

    @Query("FROM User u WHERE u.userEmail= :userEmail")
    User findUserByEmail(@Param("userEmail") String email);

    @Query("FROM User u WHERE u.userEmail= :userEmail AND u.password= :userPassword")
    User findByEmailAndPassword(@Param("userEmail") String email,
                                @Param("userPassword") String password);

    @Modifying
    @Query("UPDATE User u SET u.firstname = :firstname, u.lastname = :lastname, u.userEmail = :userEmail WHERE u.id = :id")
    void updateUserDetails(@Param("id") long id, @Param("firstname") String firstname, @Param("lastname") String lastname, @Param("userEmail") String userEmail);


}
