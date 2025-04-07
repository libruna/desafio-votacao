package com.desafio.code.controller;

import com.desafio.code.dto.ErrorDTO;
import com.desafio.code.dto.MemberDTO;
import com.desafio.code.dto.ProposalDTO;
import com.desafio.code.service.MemberService;
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
@RequestMapping("/members")
@AllArgsConstructor
@Tag(name = "Asssociado", description = "Recursos utilizado para gerenciar associados.")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "Registra uma nova associado", description = "Recurso utilizado para registrar um novo associado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Associado registrado com sucesso.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProposalDTO.class), examples = @ExampleObject("{\"id\": \"12a3b4567c7897fa165b\"}"))
            })
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = MemberDTO.class), mediaType = "application/json", examples = @ExampleObject(name = "Registrado com sucesso", value = "{\"name\": \"Beatriz Rodrigues Gomes\", \"CPF\": \"11113719702\"}")))
    @PostMapping
    public ResponseEntity<MemberDTO> registerMember(@Valid @RequestBody MemberDTO memberDTO) {
        return ResponseEntity.created(null).body(this.memberService.save(memberDTO));
    }

    @Operation(summary = "Recupera os dados de um associado", description = "Recurso utilizado para recuperar os dados de um associado pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associado encontrado.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MemberDTO.class), examples = @ExampleObject("{\"name\": \"Beatriz Rodrigues Gomes\", \"CPF\": \"11113719702\"}"))
            }),
            @ApiResponse(responseCode = "404", description = "Associado não encontrado.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class), examples = @ExampleObject("{\"code\": \"404\", \"message\": \"Associado não encontrado.\"}"))
            })
    })
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMember(@PathVariable String id) {
        return ResponseEntity.ok(memberService.getById(id));
    }
}
