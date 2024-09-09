package com.thesis.BackendApp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thesis.BackendApp.service.BlockchainUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(length = 1024)
    public String hash;
    @Column(length = 1024)
    public String previousHash;
    @Column(length = 1024)
    private String data;
    private long timeStamp;
    private int nonce;

    @OneToMany(mappedBy = "block")
    private List<Payment> payments = new ArrayList<>();


    public Block(String data,String previousHash ) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateBlockHash();
    }


    /**
     * Calculates the hash of a block based on its attributes
     * @return The calculated hash value as a string
     */
    public String calculateBlockHash() {
        String calculatedhash = BlockchainUtils.generateSHA256(
                getPreviousHash() +
                        getTimeStamp() +
                        getNonce() +
                        getData()
        );
        return calculatedhash;
    }
    /**
     * Mines the current block by incrementing the nonce until a hash with
     * a certain difficulty level is obtained.
     * @param difficulty the desired difficulty level for the block hash
     */
    public void mineBlock(int difficulty) {
        String target = BlockchainUtils.getDificultyString(difficulty);
        while(!getHash().substring( 0, difficulty).equals(target)) {
            setNonce(getNonce() + 1);
            setHash(calculateBlockHash());
        }
    }

}
