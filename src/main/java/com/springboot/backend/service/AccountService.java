package com.springboot.backend.service;

import com.springboot.backend.model.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    Account addAccount(Account account);

    Account findById(long id);
    List<Account> getAccounts();
    Account getAccountByUserID(long id);

    void deleteAccount(long id);

}
