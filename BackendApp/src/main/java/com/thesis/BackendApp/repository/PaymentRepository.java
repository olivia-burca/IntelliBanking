package com.thesis.BackendApp.repository;

import com.thesis.BackendApp.dtos.PaymentStatisticsDto;
import com.thesis.BackendApp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM payment p WHERE (p.credit_account_id=?1 OR p.debit_account_id=?2) AND p.status='COMPLETED' ORDER BY p.id ")
    List<Payment> findByCreditAccountIdOrDebitAccountIdOrderById(Long creditAccountId, Long debitAccountId);

    @Query(nativeQuery = true)
    PaymentStatisticsDto getDebitStatistics(String status, Long debitAccountId);

    @Query(nativeQuery = true)
    PaymentStatisticsDto getCreditStatistics(String status, Long creditAccountId);

    List<Payment> findByStatus(String status);

    List<Payment> findByBlockId(Long blockId);


}