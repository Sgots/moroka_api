package com.springboot.backend.service.impl;

import com.springboot.backend.exception.ResourceNotFoundException;
import com.springboot.backend.model.Account;
import com.springboot.backend.model.User;
import com.springboot.backend.repository.AccountRepository;
import com.springboot.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl{

//    @Autowired
//    private AccountRepository accountRepository;
//
//    public AccountServiceImpl(AccountRepository accountRepository) {
//        super();
//        this.accountRepository = accountRepository;
//    }
//
//    @Override
//    public Account addAccount(Account account) {
//        return accountRepository.save(account);
//    }
//
//    @Override
//    public Account findById(long id) {
//        return findById(id);
//    }
//
//    @Override
//    public List<Account> getAccounts() {
//        return accountRepository.findAll();
//    }
//
//    @Override
//    public Account getAccountByUserID(long id) {
//        return accountRepository.findById((int) id).orElseThrow(()->
//                new ResourceNotFoundException("User", "ID",id));
//    }
//
//    @Override
//    public void deleteAccount(long id) {
//
//        accountRepository.findById((int) id).orElseThrow(() ->
//                new ResourceNotFoundException("User","Id",id));
//        accountRepository.deleteById((int) id);
//
//    }
}
