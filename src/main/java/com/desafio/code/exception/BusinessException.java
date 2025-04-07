package com.desafio.code.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode code;
    private final String[] args;

    public BusinessException(ErrorCode code, String... args) {
        super(code.getMessage());
        this.code = code;
        this.args = args;
    }
}
