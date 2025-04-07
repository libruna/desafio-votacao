package com.desafio.code.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "Erro", description = "Representa uma resposta de erro.")
public class ErrorDTO {

    @Schema(description = "Status http do erro.")
    private int code;

    @Schema(description = "Mensagem de erro.")
    private String message;
}
