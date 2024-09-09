package com.thesis.BackendApp.service.impl;

import com.thesis.BackendApp.model.Audit;
import com.thesis.BackendApp.model.Balance;
import com.thesis.BackendApp.model.BalanceHistory;
import com.thesis.BackendApp.repository.BalanceRepository;
import com.thesis.BackendApp.model.AppUser;
import com.thesis.BackendApp.service.AuditService;
import com.thesis.BackendApp.service.BalanceHistoryService;
import com.thesis.BackendApp.service.BalanceService;
import com.thesis.BackendApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private BalanceHistoryService balanceHistoryService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserService userService;

    @Override
    public Balance addBalance(Long userId, Balance balance) {

        AppUser userWhoModified = userService.findUserById(userId);
        LocalDateTime timestamp = LocalDateTime.now();
        balance.setTimestamp(timestamp);
        Balance newBalance = this.balanceRepository.save(balance);
        BalanceHistory balanceHistory = new BalanceHistory(newBalance.getId(), newBalance.getAvailableDebit(), newBalance.getAvailableCredit(), newBalance.getPendingDebit(), newBalance.getPendingCredit(), newBalance.getStatus(), newBalance.getNextstatus(), timestamp);
        Audit audit = new Audit(userId, userWhoModified.getUsername(),  newBalance.getId(), "BALANCE", "ADD", timestamp);
        auditService.addAudit(audit);
        balanceHistoryService.addBalanceHistory(balanceHistory);

        return newBalance;
    }

    @Override
    public Balance findLastBalanceOfAccount(Long accountId) {
        return balanceRepository.findTopByAccountIdOrderByIdDesc(accountId);
    }

    @Override
    public List<Balance> findBalancesOfAccount(Long accountId) {
        List<Balance> balanceList =  balanceRepository.findByAccountIdOrderById(accountId).stream().filter(b -> b.getAvailableDebit().subtract(b.getAvailableCredit()).compareTo(BigDecimal.ZERO) >= 0)
                .collect(Collectors.toList());
        List<Balance> balanceListFinal = new ArrayList<>(balanceList);
        for(int i =0; i < balanceList.size() - 1; i++) {
            if (balanceList.get(i).getAvailableCredit().equals(balanceList.get(i+1).getAvailableCredit()) &&
                    balanceList.get(i).getAvailableDebit().equals(balanceList.get(i+1).getAvailableDebit()) &&
                    balanceList.get(i).getPendingCredit().equals(balanceList.get(i+1).getPendingCredit()) &&
                    balanceList.get(i).getPendingDebit().equals(balanceList.get(i+1).getPendingDebit()) ) {
                balanceListFinal.remove(balanceList.get(i));

            }
        }
        return balanceListFinal;
    }

}
