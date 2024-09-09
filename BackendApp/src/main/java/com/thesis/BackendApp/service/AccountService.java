package com.thesis.BackendApp.service;

import com.thesis.BackendApp.model.Account;

import java.security.KeyPair;
import java.util.List;

public interface AccountService {
    Account addAccount(Long useId, Account account);

    List<Account> findAllAccounts();

    Account updateAccount(Long userId, Account account);

    Account findAccountById(Long id);

    Account deleteAccount(Long userId, Long id);

    Account approveAccount(Long userId, Account account);

    Account rejectAccount(Long userId, Account account);

    List<Account> findByStatus(String status);


    Account findAccountByPublicKey(String publicKey);

    KeyPair generateKeyPairForAccount();
}
