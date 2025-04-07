package com.desafio.code.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Pauta", description = "Representa uma pauta.")
public class ProposalDTO {

    @Schema(description = "Identificador único da pauta.")
    private String id;

    @NotNull(message = "Título da pauta é obrigatório.")
    @Schema(description = "Título da pauta.")
    private String title;

    @NotNull(message = "Descrição da pauta é obrigatória.")
    @Schema(description = "Descrição da pauta.")
    private String description;
}
