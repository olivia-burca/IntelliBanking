package com.thesis.BackendApp.model;

import com.thesis.BackendApp.exceptions.BlockchainException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Blockchain {

    public  List<Block> chain = new ArrayList<Block>();
    public  int difficulty = 4;

    public Blockchain(List<Block> list) {
        this.chain = list;
    }

    /**
     * Checks if the blockchain is valid by looping through it and checking the hashes and
     * other properties of the blocks.
     * @return true if the blockchain is valid
     * @throws BlockchainException if there is an error while validating the blockchain
     */
    public  Boolean isBlockchainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[this.getDifficulty()]).replace('\0', '0');
        for(int i = 1; i < this.getChain().size(); i++) {
            currentBlock = this.getChain().get(i);
            previousBlock = this.getChain().get(i-1);
            if(!currentBlock.getHash().equals(currentBlock.calculateBlockHash()) ){
                throw(new BlockchainException("Current hashes of block are not equal."));
            }
            if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) {
                throw(new BlockchainException("Previous hashes of block are not equal."));
            }
            if(!currentBlock.getHash().substring( 0, getDifficulty()).equals(hashTarget)) {
                throw(new BlockchainException("Block has not been mined."));
            }
        }
        return true;
    }
    public  void addBlockDummy(Block newBlock) {
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
    }


}
