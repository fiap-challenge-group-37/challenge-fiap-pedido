package com.fiap.pedido.domain.dto;

import java.util.List;

/**
 * Evento publicado quando um pedido é pago
 */
public record PedidoPagoEvento(
        Long idPedido,
        List<ItemPedido> itens
) {
    public record ItemPedido(
            String nome,
            Integer quantidade
    ) {}
}