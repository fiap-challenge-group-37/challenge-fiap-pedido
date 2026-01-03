package com.fiap.challenge.cliente.adapters.in.http.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClienteDTO {
    private String cpf;
    private String nome;
    private String email;
    private String role;

    public ClienteDTO() {}

    public ClienteDTO(String cpf, String nome, String email, String role) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.role = role;
    }

}
