package com.thesis.BackendApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseAccount extends BaseEntity<Long> {

    private String fullname;

    private String address;

    private String currency;

    private String accountStatus;

}
