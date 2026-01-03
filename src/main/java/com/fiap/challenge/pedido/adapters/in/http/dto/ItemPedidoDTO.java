package com.fiap.challenge.pedido.adapters.in.http.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ItemPedidoDTO {

    @NotNull(message = "ID do produto não pode ser nulo.")
    private Long produtoId;


    @NotNull(message = "Quantidade não pode ser nula.")
    @Positive(message = "Quantidade deve ser positiva.")
    private Integer quantidade;

    // Getters e Setters
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}