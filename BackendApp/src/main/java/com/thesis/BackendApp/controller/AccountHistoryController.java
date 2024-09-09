package com.thesis.BackendApp.controller;

import com.thesis.BackendApp.model.AccountHistory;
import com.thesis.BackendApp.service.AccountHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AccountHistoryController {

    @Autowired
    private AccountHistoryService accountHistoryService;

    @GetMapping("/accounthistory/{accountid}")
    public ResponseEntity<List<AccountHistory>> getAccountHistoryByAccountId (@PathVariable("accountid") Long accountId) {
        List<AccountHistory> accountHistory = accountHistoryService.findByAccountId(accountId);
        return new ResponseEntity<>(accountHistory, HttpStatus.OK);
    }

    @GetMapping("/accounthistory/timestamp/{timestamp}")
    public ResponseEntity<AccountHistory> getAccountHistoryByTimestamp (@PathVariable("timestamp") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        AccountHistory accountHistory = accountHistoryService.findByTimestamp(timestamp);
        return new ResponseEntity<>(accountHistory, HttpStatus.OK);
    }

}
