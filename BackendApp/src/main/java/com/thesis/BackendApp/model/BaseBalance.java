package com.thesis.BackendApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseBalance extends BaseEntity<Long> {

    private BigDecimal availableDebit;

    private BigDecimal availableCredit;

    private BigDecimal pendingDebit;

    private BigDecimal pendingCredit;

    private LocalDateTime timestamp;
}
