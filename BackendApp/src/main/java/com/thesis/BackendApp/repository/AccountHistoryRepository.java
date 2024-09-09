package com.thesis.BackendApp.repository;

import com.thesis.BackendApp.model.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountHistoryRepository extends JpaRepository<AccountHistory, Long> {
    List<AccountHistory> findByAccountId(Long accountId);

    AccountHistory findTopByAccountIdOrderByIdDesc(Long accountId);

    AccountHistory findByTimestamp(LocalDateTime timestamp);

}