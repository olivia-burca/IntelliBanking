package com.thesis.BackendApp.converter;

import com.thesis.BackendApp.dtos.BlockDTO;
import com.thesis.BackendApp.model.Block;
import org.springframework.stereotype.Component;

@Component
public class BlockConverter extends BaseConverter<Long, Block, BlockDTO> {

    @Override
    public Block convertDtoToModel(BlockDTO dto) {

        var model = new Block();
        model.setId(dto.getId());
        model.setHash(dto.getHash());
        model.setPreviousHash(dto.getPreviousHash());
        model.setNonce(dto.getNonce());
        model.setTimeStamp(dto.getTimestamp());

        return model;
    }

    @Override
    public BlockDTO convertModelToDto(Block block) {

        var blockDto = new BlockDTO(block.getHash(), block.getPreviousHash(), block.getTimeStamp(), block.getNonce());
        blockDto.setId(block.getId());

        return blockDto;

    }

}

