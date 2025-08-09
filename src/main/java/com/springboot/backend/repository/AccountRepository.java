package com.springboot.backend.repository;

import com.springboot.backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository {


//    @Modifying
//    @Query(
//            value = "insert into tbl_account (account_id,user_id)" +
//                    " VALUES (:account_id,:user_id)", nativeQuery = true)
//    @Transactional
//    void addAccount(@Param("account_id")Integer account_id, @Param("user_id")Integer user_id);


//    @Query("select a from Account a where a.user.id = ?1")
//    Account findAccountByUserID(String accountNumber);

//    @Query("from Account a where  a.user_id = :user_id")
//    Account findAccountById(@Param("user_id") long user_id);
}
