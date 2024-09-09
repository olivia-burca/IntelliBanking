package com.thesis.BackendApp.controller;

import com.thesis.BackendApp.converter.AccountConverter;
import com.thesis.BackendApp.dtos.AccountDTO;
import com.thesis.BackendApp.dtos.AccountsDto;
import com.thesis.BackendApp.service.AccountService;
import com.thesis.BackendApp.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountConverter accountConverter;


    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {

        List<Account> accounts = accountService.findAllAccounts();
        AccountsDto accountsDto = new AccountsDto(accountConverter.convertModelsToDTOs(accounts));
        return new ResponseEntity<>(accountsDto.getAccounts(), HttpStatus.OK);
    }



    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDTO> getAccountById (@PathVariable("id") Long id) {

        Account account = accountService.findAccountById(id);
        AccountDTO accountDto = accountConverter.convertModelToDto(account);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);

    }

    @GetMapping("/accounts/publicKey")
    public ResponseEntity<AccountDTO> getAccountByPublicKey (@RequestParam("publicKey") String publicKey) {

        Account account = accountService.findAccountByPublicKey(publicKey.replace(' ','+'));
        AccountDTO accountDto = accountConverter.convertModelToDto(account);
        return new ResponseEntity<>(accountDto, HttpStatus.OK);

    }

    @PostMapping("/accounts/add")
    public ResponseEntity<AccountDTO> addAccount(@RequestParam("userId") Long userId, @RequestBody AccountDTO account) {

        Account newAccount = accountService.addAccount(userId, accountConverter.convertDtoToModel(account));
        return new ResponseEntity<>(accountConverter.convertModelToDto(newAccount), HttpStatus.CREATED);

    }

    @PutMapping("/accounts/update")
    public ResponseEntity<AccountDTO> updateAccount(@RequestParam("userId") Long userId, @RequestBody AccountDTO account) {

        Account updateAccount = accountService.updateAccount(userId, accountConverter.convertDtoToModel(account));
        return new ResponseEntity<>(accountConverter.convertModelToDto(updateAccount), HttpStatus.OK);
    }

    @PutMapping("/accounts/delete/{id}")
    public ResponseEntity<AccountDTO> deleteAccount(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {

        Account account = accountService.deleteAccount(userId, id);
        return new ResponseEntity<>(accountConverter.convertModelToDto(account), HttpStatus.OK);
    }

    @PutMapping("/accounts/approve")
    public ResponseEntity<AccountDTO> approveAccount(@RequestParam("userId") Long userId, @RequestBody AccountDTO account) {

        Account updateAccount = accountService.approveAccount(userId, accountConverter.convertDtoToModel(account));
        return new ResponseEntity<>(accountConverter.convertModelToDto(updateAccount), HttpStatus.OK);

    }

    @PutMapping("/accounts/reject")
    public ResponseEntity<AccountDTO> rejectAccount(@RequestParam("userId") Long userId, @RequestBody AccountDTO account) {

        Account updateAccount = accountService.rejectAccount(userId, accountConverter.convertDtoToModel(account));
        return new ResponseEntity<>(accountConverter.convertModelToDto(updateAccount), HttpStatus.OK);
    }

    @GetMapping("/accounts/status/{status}")
    public ResponseEntity<List<AccountDTO>> getAccountsByStatus(@PathVariable("status") String status) {
        List<Account> accounts = accountService.findByStatus(status);
        AccountsDto accountsDto = new AccountsDto(accountConverter.convertModelsToDTOs(accounts));
        return new ResponseEntity<>(accountsDto.getAccounts(), HttpStatus.OK);
    }




}

