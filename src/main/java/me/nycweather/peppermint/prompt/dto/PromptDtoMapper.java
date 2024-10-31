package me.nycweather.peppermint.prompt.dto;

import me.nycweather.peppermint.prompt.model.VectorStoreModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptDtoMapper {
    public VectorStoreResponseDTO vectorStoreModelToDto(List<VectorStoreModel> vectorStoreModel) {
        List<String> content = vectorStoreModel.stream()
                .map(VectorStoreModel::getContent)
                .toList();
        return VectorStoreResponseDTO.builder()
                .documentName(vectorStoreModel.getFirst().getDocumentName())
                .content(content)
                .build();

    }
}
