package com.thesis.BackendApp.controller;

import com.thesis.BackendApp.model.Cryptocurrency;
import com.thesis.BackendApp.service.BlockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api")
public class CryptoController {

    @Autowired
    private BlockchainService blockchainService;

    @GetMapping("/crypto")
    public ResponseEntity<List<Cryptocurrency>> getCryptocurrencies() throws IOException {

        List<Cryptocurrency> cryptoList = blockchainService.getCryptocurrencies();

        return new ResponseEntity<>(cryptoList, HttpStatus.OK);
    }



}
