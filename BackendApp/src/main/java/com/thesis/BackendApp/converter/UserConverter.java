package com.thesis.BackendApp.converter;

import com.thesis.BackendApp.dtos.UserDTO;
import com.thesis.BackendApp.model.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserConverter extends BaseConverter<Long, AppUser, UserDTO> {

    @Override
    public AppUser convertDtoToModel(UserDTO dto) {

        var model = new AppUser();
        model.setId(dto.getId());
        model.setUsername(dto.getUsername());
        model.setPassword(dto.getPassword());
        model.setEmail(dto.getEmail());
        model.setFullname(dto.getFullname());
        model.setAddress(dto.getAddress());
        model.setStatus(dto.getStatus());
        model.setNextstatus(dto.getNextstatus());
        return model;
    }

    @Override
    public UserDTO convertModelToDto(AppUser user) {

        var userDto = new UserDTO(user.getUsername(), user.getPassword(), user.getEmail(), user.getFullname(), user.getAddress());
        userDto.setId(user.getId());
        userDto.setStatus(user.getStatus());
        userDto.setNextstatus(user.getNextstatus());
        return userDto;

    }

}


