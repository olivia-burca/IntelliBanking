package com.thesis.BackendApp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Account extends BaseAccount {


    @ManyToOne
    @JoinColumn(name="owner_id", nullable=false)
    private AppUser owner;

    @OneToMany(mappedBy = "account")
    private List<Balance> balances = new ArrayList<>();

    @OneToMany(mappedBy = "creditAccount")
    private List<Payment> payments = new ArrayList<>();

    private PrivateKey privateKey;

    private PublicKey publicKey;

}
