package com.desafio.code.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    CPF_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "CPF não encontrado."),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "Associado já cadastrado."),
    MEMBER_ALREADY_VOTED(HttpStatus.CONFLICT.value(), "Associado já votou nesta sessão."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Associado não encontrado."),
    MEMBER_UNABLE_TO_VOTE(HttpStatus.FORBIDDEN.value(), "Associado não tem permissão para votar."),
    PROPOSAL_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Pauta não encontrada."),
    SESSION_ALREADY_OPEN_FOR_THIS_PROPOSAL(HttpStatus.CONFLICT.value(), "Já existe uma sessão de votação em andamento para esta pauta."),
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Sessão não encontrada."),
    VOTE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Voto não encontrado."),
    VOTING_SESSION_CLOSED(HttpStatus.NOT_FOUND.value(), "Sessão não existe ou a votação já foi encerrada."),
    WAIT_FOR_SESSION_END(HttpStatus.UNPROCESSABLE_ENTITY.value(), "A sessão de votação ainda está em andamento. Aguarde para consultar os resultados.");

    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
