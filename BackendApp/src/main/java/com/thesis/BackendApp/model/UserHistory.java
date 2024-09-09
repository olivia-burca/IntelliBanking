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
public class UserHistory extends BaseUser {

    private Long userId;

    private LocalDateTime timestamp;


    public UserHistory(Long userId, String username, String fullname, String address, String password, String email, String status, String nextstatus, LocalDateTime timestamp) {
        this.userId = userId;
        this.setUsername(username);
        this.setFullname(fullname);
        this.setAddress(address);
        this.setPassword(password);
        this.setEmail(email);
        this.setStatus(status);
        this.setNextstatus(nextstatus);
        this.timestamp = timestamp;

    }
}
