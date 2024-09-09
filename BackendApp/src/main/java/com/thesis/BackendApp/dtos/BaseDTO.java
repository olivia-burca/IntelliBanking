package com.thesis.BackendApp.dtos;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BaseDTO<ID extends Serializable> implements Serializable {
    private Long id;

    public String status;

    public String nextstatus;
}