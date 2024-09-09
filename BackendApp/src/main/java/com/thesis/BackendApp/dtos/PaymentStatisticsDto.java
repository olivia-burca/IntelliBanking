package com.thesis.BackendApp.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@NoArgsConstructor
@Data
@ToString(callSuper = true)
@Getter
@Setter
public class PaymentStatisticsDto {
    private String status;
    private BigInteger count;
    private BigDecimal sum;

    public PaymentStatisticsDto(String status, BigInteger count, BigDecimal sum) {
        this.status = status;
        this.count = count;
        this.sum = sum;
    }
}
