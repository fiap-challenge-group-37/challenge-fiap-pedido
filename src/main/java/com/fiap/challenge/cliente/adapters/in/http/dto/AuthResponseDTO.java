package com.fiap.challenge.cliente.adapters.in.http.dto;

import lombok.Getter;

@Getter
public class AuthResponseDTO {
    private final String token;

    public AuthResponseDTO(String token) { this.token = token; }
}
