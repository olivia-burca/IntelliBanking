package com.thesis.BackendApp.service;

import com.thesis.BackendApp.model.BalanceHistory;

import java.time.LocalDateTime;

public interface BalanceHistoryService {

    BalanceHistory addBalanceHistory(BalanceHistory balanceHistory);

    BalanceHistory findByTimestamp(LocalDateTime timestamp);
}
