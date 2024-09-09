package com.thesis.BackendApp.service;

import com.thesis.BackendApp.model.Cryptocurrency;
import com.thesis.BackendApp.model.Block;
import com.thesis.BackendApp.model.Blockchain;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

public interface BlockchainService {


    Block addBlock(Block newBlock);

    void removeBlock(Block block);

    @PostConstruct
    void initializeBlockchain();

    List<Block> findAllBlocks();

    Block getLastBlock();

    Blockchain getBlockchain();


    List<Cryptocurrency> getCryptocurrencies() throws IOException;
}
