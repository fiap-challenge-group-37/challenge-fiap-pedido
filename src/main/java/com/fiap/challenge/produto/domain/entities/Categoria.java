package com.fiap.challenge.produto.domain.entities;

public enum Categoria {
    LANCHE("Lanche"),
    ACOMPANHAMENTO("Acompanhamento"),
    BEBIDA("Bebida"),
    SOBREMESA("Sobremesa");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Categoria fromString(String text) {
        for (Categoria b : Categoria.values()) {
            if (b.descricao.equalsIgnoreCase(text) || b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada com o texto: " + text);
    }
}