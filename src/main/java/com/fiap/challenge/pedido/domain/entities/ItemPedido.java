package com.fiap.challenge.pedido.domain.entities;

import java.math.BigDecimal;

public class ItemPedido {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal precoTotal;


    public ItemPedido(Long produtoId, String nomeProduto, Integer quantidade, BigDecimal precoUnitario) {
        if (produtoId == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo.");
        }
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva.");
        }
        if (precoUnitario == null || precoUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser nulo ou negativo.");
        }
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public ItemPedido(Long id, Long produtoId, String nomeProduto, Integer quantidade, BigDecimal precoUnitario, BigDecimal precoTotal) {
        this(produtoId, nomeProduto, quantidade, precoUnitario);
        this.id = id;
        this.precoTotal = (precoTotal != null) ? precoTotal : this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
    }


    public Long getId() {
        return id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal getPrecoTotal() {
        return precoTotal;
    }

    public void setId(Long id) {
        this.id = id;
    }
}