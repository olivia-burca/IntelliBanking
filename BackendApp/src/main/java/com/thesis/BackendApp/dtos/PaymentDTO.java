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
public class PaymentDTO extends BaseDTO<Long> {

    private String operation;

    private BigDecimal amount;

    private Long debitAccountId;

    private LocalDateTime timestamp;

    private AccountDTO creditAccount;

    private String transactionHash;
    private String sender;
    private String recipient;
    private byte[] signature;

    private int sequence = 0;

    public PaymentDTO( String operation, BigDecimal amount, Long debitAccountId, LocalDateTime timestamp, String transactionHash, String sender, String recipient, byte[] signature, int sequence) {

        this.operation = operation;
        this.amount = amount;
        this.debitAccountId = debitAccountId;
        this.timestamp = timestamp;
        this.transactionHash = transactionHash;
        this.sender = sender;
        this.recipient = recipient;
        this.signature = signature;
        this.sequence = sequence;
    }
}
