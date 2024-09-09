package com.thesis.BackendApp.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasePayment extends BaseEntity<Long> {

    private String operation;

    private BigDecimal amount;

    private Long debitAccountId;

    private LocalDateTime timestamp;


    private String transactionHash;
    private PublicKey sender;
    private PublicKey recipient;
    private byte[] signature;

    private int sequence = 0;
}
