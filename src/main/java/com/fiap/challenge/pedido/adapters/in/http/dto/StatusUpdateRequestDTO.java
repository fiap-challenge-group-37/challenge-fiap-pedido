package com.fiap.challenge.pedido.adapters.in.http.dto;

import jakarta.validation.constraints.NotBlank;

public class StatusUpdateRequestDTO {
    @NotBlank(message = "Novo status não pode ser vazio.")
    private String novoStatus;

    public String getNovoStatus() {
        return novoStatus;
    }

    public void setNovoStatus(String novoStatus) {
        this.novoStatus = novoStatus;
    }
}