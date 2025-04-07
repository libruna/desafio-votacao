package com.desafio.code.exception.handler;

import com.desafio.code.dto.ErrorDTO;
import com.desafio.code.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> handleApiException(BusinessException ex) {
        ErrorDTO error = new ErrorDTO(ex.getCode().getStatusCode(), ex.getMessage());
        return ResponseEntity.status(ex.getCode().getStatusCode()).body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDTO> handleApiException(MissingServletRequestParameterException ex) {
        ErrorDTO error = new ErrorDTO(ex.getStatusCode().value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
