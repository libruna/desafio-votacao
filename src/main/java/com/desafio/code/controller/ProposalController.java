package com.desafio.code.controller;

import com.desafio.code.dto.*;
import com.desafio.code.service.ProposalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proposals")
@AllArgsConstructor
@Tag(name = "Pauta", description = "Recursos utilizado para gerenciar pautas.")
public class ProposalController {

    private final ProposalService proposalService;

    @Operation(summary = "Cria uma nova pauta", description = "Recurso utilizado para criação de uma nova pauta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProposalDTO.class), examples = @ExampleObject("{\"id\": \"12a3b4567c7897fa165b\"}"))
            })
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = ProposalDTO.class), mediaType = "application/json", examples = @ExampleObject(name = "Criado com sucesso", value = "{\"title\": \"Implantação de Semana de Inovação\", \"description\": \"Proposta para reservar uma semana por semestre dedicada a projetos internos de inovação.\"}")))
    @PostMapping
    public ResponseEntity<ProposalDTO> createProposal(@Valid @RequestBody ProposalDTO agendaDTO) {
        return ResponseEntity.created(null).body(proposalService.save(agendaDTO));
    }

    @Operation(summary = "Recupera os dados de uma pauta", description = "Recurso utilizado para recuperar os dados de uma pauta pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pauta encontrada.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProposalDTO.class), examples = @ExampleObject("{\"title\": \"Implantação de Semana de Inovação\", \"description\": \"Proposta para reservar uma semana por semestre dedicada a projetos internos de inovação.\"}"))
            }),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class), examples = @ExampleObject("{\"code\": \"404\", \"message\": \"Pauta não encontrada.\"}"))
            })
    })
    @GetMapping("/{proposalId}")
    public ResponseEntity<ProposalDTO> getProposal(@PathVariable String proposalId) {
        return ResponseEntity.ok(proposalService.getById(proposalId));
    }

    @Operation(summary = "Inicia uma nova sessão de votação", description = "Recurso utilizado para iniciar uma nova sessão de votação em uma pauta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sessão criada com sucesso.", content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = ProposalDTO.class), examples = @ExampleObject("{\"id\": \"12a3b4567c7897fa165b\"}"))
    }),
            @ApiResponse(responseCode = "409", description = "Já exise uma sessão em andamento.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class), examples = @ExampleObject("{\"code\": \"409\", \"message\": \"Já existe uma sessão de votação em andamento para esta pauta.\"}"))
            })
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = SessionDTO.class), mediaType = "application/json", examples = @ExampleObject(name = "Criado com sucesso", value = "{\"durationInMinutes\": 2}")))
    @PostMapping("/{proposalId}/sessions")
    public ResponseEntity<SessionDTO> openVotingSession(@PathVariable String proposalId,
                                                        @RequestBody(required = false) SessionDTO sessionDTO) {
        return ResponseEntity.created(null).body(proposalService.openVotingSession(proposalId, sessionDTO));
    }

    @Operation(summary = "Registra um voto", description = "Recurso utilizado para registrar um voto em uma pauta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProposalDTO.class), examples = @ExampleObject("{\"id\": \"12a3b4567c7897fa165b\"}"))
            }),
            @ApiResponse(responseCode = "403", description = "Sem permissão para votar.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class), examples = @ExampleObject("{\"code\": \"403\", \"message\": \"Associado não tem permissão para votar.\"}"))
            }),
            @ApiResponse(responseCode = "404", description = "Sessão não existe ou já foi encerrada.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class), examples = @ExampleObject("{\"code\": \"404\", \"message\": \"Sessão não encontrada ou a votação já foi encerrada.\"}"))
            })
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = VoteDTO.class), mediaType = "application/json", examples = @ExampleObject(name = "Registrado com sucesso", value = "{\"memberId\": \"67f19e66b726772310f320ds\", \"sessionId\": \"90f19e66b726772310f8edb\", \"favorable\": false}")))
    @PostMapping("/{proposalId}/votes")
    public ResponseEntity<VoteDTO> registerVote(@PathVariable String proposalId,
                                                @Valid @RequestBody VoteDTO voteDTO) {
        return ResponseEntity.created(null).body(this.proposalService.registerVote(proposalId, voteDTO));
    }

    @Operation(summary = "Recupera o resultado de votação em uma pauta", description = "Recurso utilizado para recuperar o resultado de votação em uma pauta pelo ID sessão.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado disponível.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = VoteDTO.class), examples = @ExampleObject("{\"status\": \"Aprovado\", \"totalVotes\": 20, \"totalFavorable\": 18, \"totalOpposed\": 2, \"startTime\": \"67f19e66b726772310f320ds\", \"endTime\": \"90f19e66b726772310f8edb\"}"))
            }),
            @ApiResponse(responseCode = "404", description = "Sessão não encontrada.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class), examples = @ExampleObject("{\"code\": \"404\", \"message\": \"A sessão de votação não foi encontrada.\"}"))
            }),
            @ApiResponse(responseCode = "422", description = "Votação ainda está em andamento.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class), examples = @ExampleObject("{\"code\": \"422\", \"message\": \"A sessão de votação ainda está em andamento. Aguarde para consultar os resultados.\"}"))
            })
    })
    @GetMapping("/{proposalId}/summary")
    public ResponseEntity<SessionDTO> getVotingResult(@PathVariable String proposalId) {
        return ResponseEntity.ok(this.proposalService.getVotingResult(proposalId));
    }
}
