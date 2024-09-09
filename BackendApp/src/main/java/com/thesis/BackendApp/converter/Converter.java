package com.thesis.BackendApp.converter;

import com.thesis.BackendApp.dtos.BaseDTO;

import java.io.Serializable;



public interface Converter <ID extends Serializable, Model , DTO extends BaseDTO<ID>>{
    Model convertDtoToModel(DTO dto);

    DTO convertModelToDto(Model model);
}
