package com.thesis.BackendApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Audit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    private Long userId;

    private String username;
    private Long objectId;
    private String objectType;
    private String operation;
    private LocalDateTime timestamp;

    public Audit(Long userId, String username, Long objectId, String objectType, String operation, LocalDateTime timestamp) {
        this.userId = userId;
        this.username = username;
        this.objectId = objectId;
        this.objectType = objectType;
        this.operation = operation;
        this.timestamp = timestamp;
    }
}
