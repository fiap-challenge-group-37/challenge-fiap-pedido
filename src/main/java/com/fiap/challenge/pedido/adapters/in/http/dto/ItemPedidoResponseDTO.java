package com.fiap.challenge.pedido.adapters.in.http.dto;

import com.fiap.challenge.pedido.domain.entities.ItemPedido;
import java.math.BigDecimal;

public class ItemPedidoResponseDTO {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal precoTotal;

    public ItemPedidoResponseDTO(Long id, Long produtoId, String nomeProduto, Integer quantidade, BigDecimal precoUnitario, BigDecimal precoTotal) {
        this.id = id;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoTotal;
    }

    public static ItemPedidoResponseDTO fromDomain(ItemPedido itemPedido) {
        if (itemPedido == null) return null;
        return new ItemPedidoResponseDTO(
                itemPedido.getId(),
                itemPedido.getProdutoId(),
                itemPedido.getNomeProduto(),
                itemPedido.getQuantidade(),
                itemPedido.getPrecoUnitario(),
                itemPedido.getPrecoTotal()
        );
    }

    // Getters
    public Long getId() { return id; }
    public Long getProdutoId() { return produtoId; }
    public String getNomeProduto() { return nomeProduto; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public BigDecimal getPrecoTotal() { return precoTotal; }
}