package com.fiap.challenge.pedido.domain.entities;

public enum StatusPedido {
    AGUARDANDO_PAGAMENTO("Aguardando pagamento"),
    RECEBIDO("Recebido"),
    EM_PREPARACAO("Em preparação"),
    PRONTO("Pronto"),
    FINALIZADO("Finalizado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusPedido fromString(String text) {
        for (StatusPedido b : StatusPedido.values()) {
            if (b.name().equalsIgnoreCase(text) || b.descricao.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Nenhum status encontrado com o texto: " + text + ". Valores permitidos: RECEBIDO, EM_PREPARACAO, PRONTO, FINALIZADO.");
    }
}