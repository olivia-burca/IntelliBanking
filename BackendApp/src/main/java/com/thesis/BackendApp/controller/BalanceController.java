package com.thesis.BackendApp.controller;

import com.thesis.BackendApp.converter.BalanceConverter;
import com.thesis.BackendApp.dtos.BalanceDTO;
import com.thesis.BackendApp.dtos.BalancesDto;
import com.thesis.BackendApp.model.Balance;
import com.thesis.BackendApp.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BalanceConverter balanceConverter;

    @GetMapping("/balances/last/{accountId}")
    public ResponseEntity<BalanceDTO> getLastBalanceOfAccount (@PathVariable("accountId") Long accountId) {

        Balance balance = balanceService.findLastBalanceOfAccount(accountId);
        BalanceDTO balanceDto = balanceConverter.convertModelToDto(balance);
        return new ResponseEntity<>(balanceDto, HttpStatus.OK);

    }


    @GetMapping("/balances/{accountId}")
    public ResponseEntity<List<BalanceDTO>> getBalancesOfAccount (@PathVariable("accountId") Long accountId) {

        List<Balance> balances = balanceService.findBalancesOfAccount(accountId);
        BalancesDto balancesDto = new BalancesDto(balanceConverter.convertModelsToDTOs(balances));
        return new ResponseEntity<>(balancesDto.getBalances(), HttpStatus.OK);

    }

}

