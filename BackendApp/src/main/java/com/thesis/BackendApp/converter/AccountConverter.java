package com.thesis.BackendApp.converter;

import com.thesis.BackendApp.dtos.AccountDTO;
import com.thesis.BackendApp.model.Account;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class AccountConverter extends BaseConverter<Long, Account, AccountDTO> {
    @Autowired
    private UserConverter userConverter;

    @SneakyThrows
    @Override
    public Account convertDtoToModel(AccountDTO dto) {

        var model = new Account();
        model.setId(dto.getId());
        if(dto.getPublicKey() != null && dto.getPrivateKey() != null) {
            byte[] bytePubKey  = Base64.getDecoder().decode(dto.getPublicKey());
            KeyFactory factory = KeyFactory.getInstance("ECDSA", "BC");
            PublicKey publicKey = (ECPublicKey) factory.generatePublic(new X509EncodedKeySpec(bytePubKey));

            byte[] bytePrivKey  = Base64.getDecoder().decode(dto.getPrivateKey());
            PrivateKey privateKey = (ECPrivateKey) factory.generatePrivate(new PKCS8EncodedKeySpec(bytePrivKey));

            model.setPublicKey(publicKey);
            model.setPrivateKey(privateKey);
        }

        model.setAccountStatus(dto.getAccountStatus());
        model.setCurrency(dto.getCurrency());
        model.setFullname(dto.getFullname());
        model.setAddress(dto.getAddress());
        model.setStatus(dto.getStatus());
        model.setNextstatus(dto.getNextstatus());
        if(dto.getOwner()!=null) {
            model.setOwner(userConverter.convertDtoToModel(dto.getOwner()));
        }

        return model;
    }

    @Override
    public AccountDTO convertModelToDto(Account account) {

        byte[] bytePubKey = account.getPublicKey().getEncoded();
        String strPubKey = Base64.getEncoder().encodeToString(bytePubKey);

        byte[] bytePrivKey = account.getPrivateKey().getEncoded();
        String strPrivKey = Base64.getEncoder().encodeToString(bytePrivKey);

        var accountDto = new AccountDTO( account.getFullname(), account.getAddress(),  account.getCurrency(), account.getAccountStatus(), strPubKey, strPrivKey);
        accountDto.setId(account.getId());
        accountDto.setStatus(account.getStatus());
        accountDto.setNextstatus(account.getNextstatus());
        if(account.getOwner()!=null)
        {
            accountDto.setOwner(userConverter.convertModelToDto(account.getOwner()));
        }
        return accountDto;

    }

}
