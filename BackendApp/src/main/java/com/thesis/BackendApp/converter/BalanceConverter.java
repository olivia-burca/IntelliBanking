package com.thesis.BackendApp.converter;

import com.thesis.BackendApp.dtos.BalanceDTO;
import com.thesis.BackendApp.model.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BalanceConverter extends BaseConverter<Long, Balance, BalanceDTO> {
    @Autowired
    private AccountConverter accountConverter;

    @Override
    public Balance convertDtoToModel(BalanceDTO dto) {

        var model = new Balance();
        model.setId(dto.getId());
        model.setTimestamp(dto.getTimestamp());
        model.setAvailableDebit(dto.getAvailableDebit());
        model.setAvailableCredit(dto.getAvailableCredit());
        model.setPendingCredit(dto.getPendingCredit());
        model.setPendingDebit(dto.getPendingDebit());
        model.setStatus(dto.getStatus());
        model.setNextstatus(dto.getNextstatus());
        if(dto.getAccount() != null) {
            model.setAccount(accountConverter.convertDtoToModel(dto.getAccount()));
        }
        return model;
    }

    @Override
    public BalanceDTO convertModelToDto(Balance balance) {

        var accountDto = new BalanceDTO(balance.getAvailableDebit(), balance.getAvailableCredit(), balance.getPendingDebit(), balance.getPendingCredit(), balance.getTimestamp());
        accountDto.setId(balance.getId());
        accountDto.setStatus(balance.getStatus());
        accountDto.setNextstatus(balance.getNextstatus());
        if(balance.getAccount() != null) {
            accountDto.setAccount(accountConverter.convertModelToDto(balance.getAccount()));
        }
        return accountDto;

    }

}
