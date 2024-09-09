package com.thesis.BackendApp.repository;

import com.thesis.BackendApp.model.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {

    BalanceHistory findByTimestamp(LocalDateTime timestamp);

}
