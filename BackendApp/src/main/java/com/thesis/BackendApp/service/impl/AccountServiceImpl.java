package com.thesis.BackendApp.service.impl;

import com.thesis.BackendApp.exceptions.EntityNotFoundException;
import com.thesis.BackendApp.repository.AccountRepository;
import com.thesis.BackendApp.model.*;
import com.thesis.BackendApp.service.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.*;
import java.security.*;

public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountHistoryService accountHistoryService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BlockchainService blockchainService;




    /**
     * Generates a new key pair using the Elliptic Curve Digital Signature Algorithm (ECDSA)
     *
     * @return The generated key pair
     * @throws RuntimeException if there is an error while generating the key pair
     */
    @Override
    public KeyPair generateKeyPairForAccount() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("prime192v1");
            keyPairGenerator.initialize(ecGenParameterSpec, secureRandom);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            return keyPair;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    @Transactional
    public Account addAccount(Long userId, Account account) {

        account.setStatus("APPROVE");
        account.setNextstatus("ACTIVE");
        account.setAccountStatus("OPEN");
        AppUser userWhoModified = userService.findUserById(userId);
        account.setOwner(userWhoModified);
        KeyPair keyPair = generateKeyPairForAccount();
        account.setPrivateKey(keyPair.getPrivate());
        account.setPublicKey(keyPair.getPublic());
        Account newAccount = this.accountRepository.save(account);
        Balance initialBalance = new Balance();
        initialBalance.setAccount(account);
        boolean isCrypto = false;
        boolean isFirst= true;
        for (Account acc : this.findAllAccounts()) {
            if(Objects.equals(acc.getId(), account.getId()))
                continue;
            if(acc.getCurrency().equals(account.getCurrency()) && acc.getStatus().equals("ACTIVE") ) {
                isFirst = false;
                break;
            }
        }
        for( Cryptocurrency cryptocurrency : blockchainService.getCryptocurrencies()) {
            if (account.getCurrency().equals(cryptocurrency.getSymbol()))  {
                if(isFirst) {
                    initialBalance.setAvailableDebit(BigDecimal.valueOf(5));
                } else {
                    initialBalance.setAvailableDebit(BigDecimal.valueOf(0));
                }
                isCrypto = true;
                break;
            }
        }
        if(!isCrypto) {
            if(isFirst) {
                initialBalance.setAvailableDebit(BigDecimal.valueOf(50000));
            } else {
                initialBalance.setAvailableDebit(BigDecimal.valueOf(0));
            }
        }
        initialBalance.setAvailableCredit(BigDecimal.valueOf(0));
        initialBalance.setPendingCredit(BigDecimal.valueOf(0));
        initialBalance.setPendingDebit(BigDecimal.valueOf(0));

        LocalDateTime timestamp = LocalDateTime.now();
        initialBalance.setTimestamp(timestamp);
        balanceService.addBalance(userId, initialBalance);

        AccountHistory accountHistory = new AccountHistory(newAccount.getId(),  newAccount.getFullname(), newAccount.getAddress(),   newAccount.getCurrency(), newAccount.getAccountStatus(), newAccount.getStatus(), newAccount.getNextstatus(), timestamp);
        Audit audit = new Audit(userId, userWhoModified.getUsername(),  newAccount.getId(), "ACCOUNT", "ADD", timestamp);
        auditService.addAudit(audit);
        accountHistoryService.addAccountHistory(accountHistory);

        return newAccount;
    }

    @Override
    public List<Account> findAllAccounts() {

        return this.accountRepository.findAll();
    }

    @Override
    @Transactional
    public Account updateAccount(Long userId, Account account) {

        LocalDateTime timestamp = LocalDateTime.now();
        AppUser userWhoModified = userService.findUserById(userId);
        Account oldAccount = this.findAccountById(account.getId());
        AccountHistory accountHistory = new AccountHistory(oldAccount.getId(), oldAccount.getFullname(), oldAccount.getAddress(),  oldAccount.getCurrency(), oldAccount.getAccountStatus(), oldAccount.getStatus(), oldAccount.getNextstatus(),  timestamp);
        accountHistoryService.addAccountHistory(accountHistory);
        Audit audit = new Audit(userId, userWhoModified.getUsername(), account.getId(), "ACCOUNT", "UPDATE", timestamp);
        auditService.addAudit(audit);
        account.setStatus("APPROVE");
        account.setNextstatus("ACTIVE");
        account.setOwner(oldAccount.getOwner());

        return this.accountRepository.save(account);
    }

    @Override
    public Account findAccountById(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + id + " was not found"));

    }

    @SneakyThrows
    @Override
    public Account findAccountByPublicKey(String publicKey) {

        byte[] bytePubKey  = Base64.getDecoder().decode(publicKey);
        KeyFactory factory = KeyFactory.getInstance("ECDSA", "BC");
        PublicKey publicKey2 = (ECPublicKey) factory.generatePublic(new X509EncodedKeySpec(bytePubKey));
        return accountRepository.findByPublicKey(publicKey2)
                .orElseThrow(() -> new EntityNotFoundException("Account with Public Key " + publicKey + " was not found"));
    }


    @Override
    @Transactional
    public Account deleteAccount(Long userId, Long id) {

        Account account = this.findAccountById(id);
        AppUser userWhoModified = userService.findUserById(userId);
        LocalDateTime timestamp = LocalDateTime.now();
        AccountHistory accountHistory = new AccountHistory(account.getId(), account.getFullname(), account.getAddress(), account.getCurrency(), account.getAccountStatus(), account.getStatus(), account.getNextstatus(), timestamp);
        accountHistoryService.addAccountHistory(accountHistory);
        Audit audit = new Audit(userId,userWhoModified.getUsername(), account.getId(), "ACCOUNT", "DELETE", timestamp);
        auditService.addAudit(audit);
        account.setStatus("APPROVE");
        account.setNextstatus("REMOVED");

        return accountRepository.save(account);

    }

    @Override
    @Transactional
    public Account approveAccount(Long userId, Account accountToApprove) {

        Account account = this.findAccountById(accountToApprove.getId());
        LocalDateTime timestamp = LocalDateTime.now();
        AppUser userWhoModified = userService.findUserById(userId);
        Audit audit = new Audit(userId, userWhoModified.getUsername(), account.getId(), "ACCOUNT", "APPROVE", timestamp);
        auditService.addAudit(audit);
        AccountHistory accountHistory = new AccountHistory(account.getId(), account.getFullname(), account.getAddress(),  account.getCurrency(), account.getAccountStatus(), account.getStatus(), account.getNextstatus(),  timestamp);
        accountHistoryService.addAccountHistory(accountHistory);

        if(Objects.equals(account.getNextstatus(), "ACTIVE"))
        {
            account.setStatus("ACTIVE");
            account.setNextstatus("-");
        }

        else {
            account.setStatus("REMOVED");
            account.setNextstatus("-");
            account.setAccountStatus("CLOSED");
        }
        return this.accountRepository.save(account);

    }

    @Override
    @Transactional
    public Account rejectAccount(Long userId, Account accountToApprove) {

        Account account = this.findAccountById(accountToApprove.getId());
        Account copyAccount = account;
        if(Objects.equals(account.getNextstatus(), "REMOVED")) {
            account.setStatus("ACTIVE");
            account.setNextstatus("-");
        }
        else {
            Audit lastAudit = auditService.findLastAuditByObjectIdAndObjectType(account.getId(), "ACCOUNT");
            if(Objects.equals(lastAudit.getOperation(), "UPDATE")) {
                AccountHistory history = accountHistoryService.findLastAccountHistoryByAccountId(account.getId());
                account.setFullname(history.getFullname());
                account.setAddress(history.getAddress());
                account.setAccountStatus(history.getAccountStatus());
                account.setStatus("ACTIVE");
                account.setNextstatus("-");
            }
            else {
                account.setStatus("REJECTED");
                account.setNextstatus("-");
            }
        }
        LocalDateTime timestamp = LocalDateTime.now();
        AccountHistory accountHistory = new AccountHistory(copyAccount.getId(), copyAccount.getFullname(), copyAccount.getAddress(), copyAccount.getCurrency(), copyAccount.getAccountStatus(), copyAccount.getStatus(), copyAccount.getNextstatus(), timestamp);
        accountHistoryService.addAccountHistory(accountHistory);
        AppUser userWhoModified = userService.findUserById(userId);
        Audit audit = new Audit(userId, userWhoModified.getUsername(), copyAccount.getId(), "ACCOUNT", "REJECT", timestamp);
        auditService.addAudit(audit);
        return accountRepository.save(account);

    }

    @Override
    public List<Account> findByStatus(String status) {
        List<Account> accounts =  this.accountRepository.findByStatus(status);
        return accounts;
    }

}
