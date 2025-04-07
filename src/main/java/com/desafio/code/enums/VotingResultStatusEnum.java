package com.desafio.code.enums;

import lombok.Getter;

@Getter
public enum VotingResultStatusEnum {

    APPROVED("Aprovado"),
    REJECTED("Reprovado"),
    TIED("Empatado");

    private final String status;

    VotingResultStatusEnum(String status) {
        this.status = status;
    }
}