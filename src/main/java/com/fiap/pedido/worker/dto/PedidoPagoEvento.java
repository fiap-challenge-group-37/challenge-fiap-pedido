package com.fiap.pedido.worker.dto;

import java.util.List;

public record PedidoPagoEvento(
        Long idPedido,
        List<ItemPedido> itens
) {
    public record ItemPedido(
            String nome,
            Integer quantidade
    ) {}
}