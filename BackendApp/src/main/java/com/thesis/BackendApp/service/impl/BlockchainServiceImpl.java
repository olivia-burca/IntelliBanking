package com.thesis.BackendApp.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.thesis.BackendApp.model.Cryptocurrency;
import com.thesis.BackendApp.model.Block;
import com.thesis.BackendApp.model.Blockchain;
import com.thesis.BackendApp.repository.BlockRepository;
import com.thesis.BackendApp.service.BlockchainService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class BlockchainServiceImpl implements BlockchainService {

    @Autowired
    public BlockRepository blockRepository;

    private Blockchain blockchain;


    @Override
    public Block addBlock(Block newBlock) {
        newBlock.mineBlock(this.blockchain.difficulty);
        this.blockchain.chain.add(newBlock);
        this.blockRepository.save(newBlock);
        return newBlock;
    }

    @Override
    public void removeBlock(Block block) {

        this.blockchain.chain.remove(block);
        this.blockRepository.delete(block);

    }

    @Override
    public Blockchain getBlockchain() {
        return this.blockchain;
    }

    @Override
    @PostConstruct
    public void initializeBlockchain() {
        this.blockchain = new Blockchain(this.findAllBlocks());
    }
    @Override
    public List<Block> findAllBlocks() {
        return this.blockRepository.findAll();
    }
    @Override
    public Block getLastBlock() {
        if(this.blockchain.chain.size() == 0) {
            return null;
        }
        else return this.blockchain.chain.get(this.blockchain.chain.size()-1);
    }

    @SneakyThrows
    @Override
    public List<Cryptocurrency> getCryptocurrencies()  {

        JsonObject jsonObject = new JsonObject();
        String data = null;
        StringBuilder responseData = new StringBuilder();
        JsonObject jsonObject2 = null;
        URL url = null;


        url = new URL("https://api.coincap.io/v2/assets");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                responseData.append(line);
            }
            jsonObject2 = new Gson().fromJson(responseData.toString(), JsonObject.class);
            data = jsonObject2.get("data").toString();
        }

        data = data.replaceAll("^\"|\"$", "");
        StringTokenizer jsonTokenizer = new StringTokenizer(data,",");
        String internalData[];
        String expectedCryptocurrencyOutput = null;

        List<Cryptocurrency> cryptoList = new ArrayList<Cryptocurrency>(101);;
        int i = -1;
        while (jsonTokenizer.hasMoreTokens()) {
            expectedCryptocurrencyOutput = jsonTokenizer.nextToken();
            internalData = StringUtils.split(expectedCryptocurrencyOutput,":");
            String key = internalData[0].substring(1,internalData[0].length()-1);
            String value = internalData[1].substring(1,internalData[1].length()-1);
            if(key.equalsIgnoreCase("{\"id") || key.equalsIgnoreCase("\"id"))
            {
                i++;
                cryptoList.add(new Cryptocurrency());
                Cryptocurrency aux = cryptoList.get(i);
                aux.setId(value);
                cryptoList.set(i, aux);
            }
            else if (key.equalsIgnoreCase("rank")) {
                Cryptocurrency aux = cryptoList.get(i);
                aux.setRank(value);
                cryptoList.set(i, aux);
            }
            else if (key.equalsIgnoreCase("symbol")) {
                Cryptocurrency aux = cryptoList.get(i);
                aux.setSymbol(value);
                cryptoList.set(i, aux);
            }
            else if (key.equalsIgnoreCase("priceUsd")) {
                Cryptocurrency aux = cryptoList.get(i);
                int index = value.indexOf('.');
                String newValue = value.substring(0, index+3);
                aux.setPrice(newValue);
                cryptoList.set(i, aux);
            }
            else if (key.equalsIgnoreCase("name")) {
                Cryptocurrency aux = cryptoList.get(i);
                aux.setName(value);
                cryptoList.set(i, aux);
            }
            else if (key.equalsIgnoreCase("supply")) {
                Cryptocurrency aux = cryptoList.get(i);
                int index = value.indexOf('.');
                String newValue = value.substring(0, index+3);
                aux.setSupply(newValue);
                cryptoList.set(i, aux);
            }
            else if (key.equalsIgnoreCase("marketCapUsd")) {
                Cryptocurrency aux = cryptoList.get(i);
                int index = value.indexOf('.');
                String newValue = value.substring(0, index+3);
                aux.setMarketCap(newValue);
                cryptoList.set(i, aux);
            }
            else if (key.equalsIgnoreCase("volumeUsd24Hr")) {
                Cryptocurrency aux = cryptoList.get(i);
                int index = value.indexOf('.');
                String newValue = value.substring(0, index+3);
                aux.setVolume24h(newValue);
                cryptoList.set(i, aux);
            }
            else if (key.equalsIgnoreCase("changePercent24Hr")) {
                Cryptocurrency aux = cryptoList.get(i);
                int index = value.indexOf('.');
                String newValue = value.substring(0, index+3);
                aux.setChange24h(newValue);
                cryptoList.set(i, aux);
            }
            else if (key.equalsIgnoreCase("vwap24Hr")) {
                Cryptocurrency aux = cryptoList.get(i);
                int index = value.indexOf('.');
                String newValue = value.substring(0, index+3);
                aux.setVwap24h(newValue);
                cryptoList.set(i, aux);
            }
        }

        return cryptoList;
    }

}
