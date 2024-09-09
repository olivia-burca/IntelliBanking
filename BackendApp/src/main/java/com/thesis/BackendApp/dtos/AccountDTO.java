package com.thesis.BackendApp.dtos;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class AccountDTO extends BaseDTO<Long> {

    private String fullname;

    private String address;

    private String currency;

    private String accountStatus;

    private UserDTO owner;

    private String publicKey;

    private String privateKey;

    private List<BalanceDTO> balances = new ArrayList<>();

    private List<PaymentDTO> payments = new ArrayList<>();

    public AccountDTO( String fullname, String address, String currency, String accountStatus, String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.fullname = fullname;
        this.address = address;
        this.privateKey = privateKey;
        this.currency = currency;
        this.accountStatus = accountStatus;
    }
}

