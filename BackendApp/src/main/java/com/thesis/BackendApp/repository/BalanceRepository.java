package com.thesis.BackendApp.repository;

import com.thesis.BackendApp.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    List<Balance> findByAccountIdOrderById(Long accountId);

    Balance findTopByAccountIdOrderByIdDesc(Long accountId);
}
