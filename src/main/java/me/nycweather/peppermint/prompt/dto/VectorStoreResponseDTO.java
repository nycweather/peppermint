package me.nycweather.peppermint.prompt.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record VectorStoreResponseDTO(
        String documentName,
        List<String> content
) {
}
