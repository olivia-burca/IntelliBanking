package com.thesis.BackendApp.service.impl;

import com.thesis.BackendApp.model.BalanceHistory;
import com.thesis.BackendApp.repository.BalanceHistoryRepository;
import com.thesis.BackendApp.service.BalanceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

public class BalanceHistoryServiceImpl implements BalanceHistoryService {

    @Autowired
    private BalanceHistoryRepository balanceHistoryRepository;

    @Override
    public BalanceHistory addBalanceHistory(BalanceHistory balanceHistory) {
        return balanceHistoryRepository.save(balanceHistory);
    }


    @Override
    public BalanceHistory findByTimestamp(LocalDateTime timestamp) { return balanceHistoryRepository.findByTimestamp(timestamp); }

}
