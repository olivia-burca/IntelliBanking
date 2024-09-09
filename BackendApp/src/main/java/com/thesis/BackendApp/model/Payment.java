package com.thesis.BackendApp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thesis.BackendApp.dtos.PaymentStatisticsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;

@NamedNativeQuery(name = "Payment.getDebitStatistics",
        query = "Select A.status,count (A.id),sum(A.amount) from (Select  status,id,amount, debit_account_id from payment) A WHERE A.status = ?1 AND A.debit_account_id = ?2 GROUP BY A.status",
        resultSetMapping = "Mapping.PaymentStatisticsDto")
@NamedNativeQuery(name = "Payment.getCreditStatistics",
        query = "Select A.status,count (A.id),sum(A.amount) from (Select  status,id,amount, credit_account_id from payment) A WHERE A.status = ?1 AND A.credit_account_id = ?2 GROUP BY A.status",
        resultSetMapping = "Mapping.PaymentStatisticsDto")
@SqlResultSetMapping(name = "Mapping.PaymentStatisticsDto",
        classes = @ConstructorResult(targetClass = PaymentStatisticsDto.class,
                columns = {@ColumnResult(name = "status"),
                        @ColumnResult(name = "count"),
                        @ColumnResult(name = "sum")}))



@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Payment extends BasePayment {

    @ManyToOne
    @JoinColumn(name="credit_account_id")
    private Account creditAccount;

    @ManyToOne
    @JoinColumn(name="block_id")
    private Block block;


    @Override
    public String toString() {
        return getTransactionHash() + getSender() + getRecipient() + Arrays.toString(getSignature()) + getAmount().toString() + getSequence();
    }
}
