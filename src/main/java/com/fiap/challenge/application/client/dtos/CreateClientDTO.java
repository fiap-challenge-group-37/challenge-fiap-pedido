package com.fiap.challenge.application.client.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ClientDTO {
    private Long id;
    private String name;
    private String email;
    private String cpf;

    public ClientDTO() {}

    public ClientDTO(Long id, String name, String email, String cpf) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
    }
}