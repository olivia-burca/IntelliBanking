package com.thesis.BackendApp.service;

import com.thesis.BackendApp.model.Balance;

import java.util.List;

public interface BalanceService {

    Balance addBalance(Long userId, Balance balance);

    Balance findLastBalanceOfAccount(Long AccountId);

    List<Balance> findBalancesOfAccount(Long AccountId);

}
