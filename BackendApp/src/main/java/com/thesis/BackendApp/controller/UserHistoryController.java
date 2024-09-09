package com.thesis.BackendApp.controller;

import com.thesis.BackendApp.model.UserHistory;
import com.thesis.BackendApp.service.UserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserHistoryController {

    @Autowired
    private UserHistoryService userHistoryService;

    @GetMapping("/userhistory/{userid}")
    public ResponseEntity<List<UserHistory>> getUserHistoryByUserId (@PathVariable("userid") Long userId) {
        List<UserHistory> userHistory = userHistoryService.findByUserId(userId);
        return new ResponseEntity<>(userHistory, HttpStatus.OK);
    }

    @GetMapping("/userhistory/timestamp/{timestamp}")
    public ResponseEntity<UserHistory> getUserHistoryByTimestamp (@PathVariable("timestamp") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        UserHistory userHistory = userHistoryService.findByTimestamp(timestamp);
        return new ResponseEntity<>(userHistory, HttpStatus.OK);
    }

}

