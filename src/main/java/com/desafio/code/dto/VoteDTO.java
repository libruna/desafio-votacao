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
@Schema(name = "Voto", description = "Representa um voto.")
public class VoteDTO {

    @Schema(description = "Identificador único do voto.")
    private String id;

    @Schema(description = "Identificador único do associado.")
    private String memberId;

    @Schema(description = "Identificador único da sessão.")
    private String sessionId;

    @Schema(description = "Indica se o voto foi favorável ou contrário.")
    private Boolean favorable;
}
