package com.thesis.BackendApp.converter;

import com.thesis.BackendApp.dtos.PaymentDTO;
import com.thesis.BackendApp.model.Payment;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class PaymentConverter extends BaseConverter<Long, Payment, PaymentDTO> {
    @Autowired
    private AccountConverter accountConverter;

    @SneakyThrows
    @Override
    public Payment convertDtoToModel(PaymentDTO dto) {

        var model = new Payment();
        model.setId(dto.getId());
        model.setTimestamp(dto.getTimestamp());
        model.setOperation(dto.getOperation());
        model.setAmount(dto.getAmount());
        model.setDebitAccountId(dto.getDebitAccountId());
        model.setStatus(dto.getStatus());
        model.setNextstatus(dto.getNextstatus());
        model.setTransactionHash(dto.getTransactionHash());
        KeyFactory factory = KeyFactory.getInstance("ECDSA", "BC");
        if(dto.getSender() != null) {
            byte[] bytePubKeySender  = Base64.getDecoder().decode(dto.getSender());
            PublicKey publicKeySender = (ECPublicKey) factory.generatePublic(new X509EncodedKeySpec(bytePubKeySender));
            model.setSender(publicKeySender);
        }
        if(dto.getRecipient() != null) {
            byte[] bytePubKeyRec  = Base64.getDecoder().decode(dto.getRecipient());
            PublicKey publicKeyRec = (ECPublicKey) factory.generatePublic(new X509EncodedKeySpec(bytePubKeyRec));
            model.setRecipient(publicKeyRec);
        }

        model.setSignature(dto.getSignature());
        model.setSequence(dto.getSequence());
        if(dto.getCreditAccount() != null) {
            model.setCreditAccount(accountConverter.convertDtoToModel(dto.getCreditAccount()));
        }
        return model;
    }

    @Override
    public PaymentDTO convertModelToDto(Payment payment) {

        byte[] bytePubKeySender = payment.getSender().getEncoded();
        String strPubKeySender = Base64.getEncoder().encodeToString(bytePubKeySender);

        byte[] bytePubKeyRec = payment.getRecipient().getEncoded();
        String strPubKeyRec = Base64.getEncoder().encodeToString(bytePubKeyRec);

        var paymentDTO = new PaymentDTO(payment.getOperation(), payment.getAmount(), payment.getDebitAccountId(), payment.getTimestamp(), payment.getTransactionHash(), strPubKeySender, strPubKeyRec, payment.getSignature(), payment.getSequence());
        paymentDTO.setId(payment.getId());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setNextstatus(payment.getNextstatus());
        if(payment.getCreditAccount() != null) {
            paymentDTO.setCreditAccount(accountConverter.convertModelToDto(payment.getCreditAccount()));
        }
        return paymentDTO;

    }

}

