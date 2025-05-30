package com.fiap.challenge.cliente.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT) // 409
public class ClienteCpfJaCadastradoException extends RuntimeException {
    public ClienteCpfJaCadastradoException(String message) {
        super(message);
    }
}