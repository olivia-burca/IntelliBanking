package com.thesis.BackendApp.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class BalanceDTO extends BaseDTO<Long> {
    private BigDecimal availableDebit;

    private BigDecimal availableCredit;

    private BigDecimal pendingDebit;

    private BigDecimal pendingCredit;

    private LocalDateTime timestamp;

    private AccountDTO account;

    public BalanceDTO(BigDecimal availableDebit, BigDecimal availableCredit, BigDecimal pendingDebit, BigDecimal pendingCredit, LocalDateTime timestamp ) {
        this.availableDebit = availableDebit;
        this.availableCredit = availableCredit;
        this.pendingCredit = pendingCredit;
        this.pendingDebit = pendingDebit;
        this.timestamp = timestamp;

    }
}

