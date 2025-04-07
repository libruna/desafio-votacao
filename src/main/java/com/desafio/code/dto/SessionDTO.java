package com.desafio.code.dto;

import com.desafio.code.enums.VotingResultStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Sessão", description = "Representa uma sessão de votação.")
public class SessionDTO {

    @Schema(description = "Identificador único da sessão.")
    private String id;

    @Schema(description = "Duração da sessão em minutos.")
    private Integer durationInMinutes;

    @Schema(description = "Status do processamento de apuração dos votos.")
    private VotingResultStatusEnum status;

    @Schema(description = "Quantidade total de votos.")
    private Integer totalVotes;

    @Schema(description = "Quantidade total de votos favoráveis.")
    private Integer totalFavorable;

    @Schema(description = "Quantidade total de votos contrários.")
    private Integer totalOpposed;

    @Schema(description = "Data e horário de início da sessão.")
    private LocalDateTime startTime;

    @Schema(description = "Data e horário de fim da sessão.")
    private LocalDateTime endTime;
}
