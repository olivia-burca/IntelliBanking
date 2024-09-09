package com.thesis.BackendApp.service.impl;

import com.thesis.BackendApp.model.AccountHistory;
import com.thesis.BackendApp.repository.AccountHistoryRepository;
import com.thesis.BackendApp.service.AccountHistoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class AccountHistoryServiceImpl implements AccountHistoryService {
    @Autowired
    private AccountHistoryRepository accountHistoryRepository;

    @Override
    public AccountHistory addAccountHistory(AccountHistory accountHistory) {
        return accountHistoryRepository.save(accountHistory);
    }

    @Override
    public List<AccountHistory> findAllAccountHistory() {
        return accountHistoryRepository.findAll();
    }

    @Override
    public List<AccountHistory> findByAccountId(Long id) {
        return accountHistoryRepository.findByAccountId(id);
    }

    @Override
    public AccountHistory findLastAccountHistoryByAccountId(Long accountId) { return accountHistoryRepository.findTopByAccountIdOrderByIdDesc(accountId); }

    @Override
    public AccountHistory findByTimestamp(LocalDateTime timestamp) { return accountHistoryRepository.findByTimestamp(timestamp); }

}
