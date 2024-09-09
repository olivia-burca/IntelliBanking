package com.thesis.BackendApp.converter;

import com.thesis.BackendApp.dtos.BaseDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseConverter<ID extends Serializable, Model , DTO extends BaseDTO<ID>> implements Converter<ID, Model, DTO> {

    public List<DTO> convertModelsToDTOs(Collection<Model> models) {
        return models.stream().map(this::convertModelToDto).collect(Collectors.toList());
    }

}
