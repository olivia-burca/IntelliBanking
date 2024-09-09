package com.thesis.BackendApp.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor
public class BalanceHistory extends BaseBalance {

    private Long balanceId;


    public BalanceHistory(Long balanceId, BigDecimal availableDebit, BigDecimal availableCredit, BigDecimal pendingDebit, BigDecimal pendingCredit, String status, String nextstatus, LocalDateTime timestamp) {
        this.balanceId = balanceId;
        this.setAvailableCredit(availableCredit);
        this.setAvailableDebit(availableDebit);
        this.setPendingCredit(pendingCredit);
        this.setPendingDebit(pendingDebit);
        this.setStatus(status);
        this.setNextstatus(nextstatus);
        this.setTimestamp(timestamp);
    }
}
