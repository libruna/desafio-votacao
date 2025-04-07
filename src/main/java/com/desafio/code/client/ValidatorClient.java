package com.desafio.code.client;

import com.desafio.code.enums.CPFValidationStatusEnum;
import com.desafio.code.exception.BusinessException;
import com.desafio.code.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ValidatorClient {

    public CPFValidationStatusEnum CPFValidator(String CPF) {
        boolean isValid = new Random().nextBoolean();
        if (!isValid) {
            throw new BusinessException(ErrorCode.CPF_NOT_FOUND);
        }

        return new Random().nextBoolean()
                ? CPFValidationStatusEnum.ABLE_TO_VOTE
                : CPFValidationStatusEnum.UNABLE_TO_VOTE;
    }
}
