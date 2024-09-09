package com.thesis.BackendApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor

public class AccountHistory extends BaseAccount {
    private Long accountId;

    private LocalDateTime timestamp;

    public AccountHistory(Long accountId, String fullname, String address, String currency, String accountStatus, String status, String nextstatus, LocalDateTime timestamp) {
        this.accountId = accountId;
        this.setFullname(fullname);
        this.setAddress(address);
        this.setCurrency(currency);
        this.setAccountStatus(accountStatus);
        this.setStatus(status);
        this.setNextstatus(nextstatus);
        this.timestamp = timestamp;
    }
}
