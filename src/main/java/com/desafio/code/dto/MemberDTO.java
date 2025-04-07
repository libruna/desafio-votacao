package com.desafio.code.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Associado", description = "Representa um associado.")
public class MemberDTO {

    @Schema(description = "Identificador do associado.")
    private String id;

    @Schema(description = "CPF do associado.")
    private String CPF;

    @Schema(description = "Nome do associado.")
    private String name;
}
