package com.desafio.code.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT.value(), "Associado já cadastrado."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Associado não encontrado.");

    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
