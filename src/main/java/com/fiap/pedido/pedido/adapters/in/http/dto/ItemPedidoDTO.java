package com.fiap.pedido.pedido.adapters.in.http.dto;

import java.math.BigDecimal;

public class ItemPedidoDTO {
    private Long produtoId;
    private String nomeProduto;  // ← Adicionar este campo
    private Integer quantidade;
    private BigDecimal preco;     // ← Adicionar este campo

    // Getters e Setters
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getNomeProduto() {  // ← Adicionar este getter
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPreco() {  // ← Adicionar este getter
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}