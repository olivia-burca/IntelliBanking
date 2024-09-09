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
public class UserDTO extends BaseDTO<Long> {
    private String username;

    private String fullname;

    private String address;

    private String password;

    private String email;

    private List<AccountDTO> accounts = new ArrayList<>();

    public UserDTO(String username, String password, String email, String fullname, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.address = address;
    }
}

