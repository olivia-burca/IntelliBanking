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
public class BlockDTO extends BaseDTO<Long> {

    private String hash;

    private String previousHash;

    private long timestamp;

    private int nonce;

    private List<PaymentDTO> payments = new ArrayList<>();

    public BlockDTO( String hash, String previousHash, long timestamp, int nonce) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timestamp = timestamp;
        this.nonce = nonce;

    }
}

