package com.thesis.BackendApp.service;

import com.thesis.BackendApp.dtos.PaymentStatisticsDto;
import com.thesis.BackendApp.model.Account;
import com.thesis.BackendApp.model.Payment;

import java.util.List;

public interface PaymentService {

    void generateSignature(Payment payment, Account creditAccount);

    boolean verifiySignature(Payment payment);

    Payment addPayment(Long userId, Payment payment, Long creditAccountId);

    List<Payment> findPaymentsOfAccount(Long accountId);

    List<Payment> findByStatus(String status);

    Payment findById(Long id);

    Payment rejectPayment(Long userId, Payment payment);

    List<Payment> findPaymentsOfBlock(Long blockId);

    PaymentStatisticsDto getDebitStatistics(String status, Long accountId);

    PaymentStatisticsDto getCreditStatistics(String status, Long accountId);


    String calculatePaymentHash(Payment payment);

}
