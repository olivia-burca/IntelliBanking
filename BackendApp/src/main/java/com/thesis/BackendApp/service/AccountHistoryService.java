package com.thesis.BackendApp.service;

import com.thesis.BackendApp.model.AccountHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountHistoryService {

    AccountHistory addAccountHistory(AccountHistory accountHistory);

    List<AccountHistory> findAllAccountHistory();

    List<AccountHistory> findByAccountId(Long id);

    AccountHistory findLastAccountHistoryByAccountId(Long accountId);

    AccountHistory findByTimestamp(LocalDateTime timestamp);

}
