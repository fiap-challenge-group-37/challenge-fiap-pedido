package com.fiap.challenge.produto.adapters.in.http.dto;

import com.fiap.challenge.produto.domain.entities.Produto;
import com.fiap.challenge.produto.domain.entities.Categoria;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoDTO {
    private Long id;
    private String nome;
    private String categoria; // Recebe como String para facilitar e valida na conversão
    private BigDecimal preco;
    private String descricao;

    public ProdutoDTO(Long id, String nome, String categoria, BigDecimal preco, String descricao) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.descricao = descricao;
    }

    public static ProdutoDTO fromDomain(Produto produto) {
        if (produto == null) return null;
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria().name(), // Converte Enum para String
                produto.getPreco(),
                produto.getDescricao()
        );
    }

    public Produto toDomain() {
        Produto produto = new Produto(
                this.nome,
                Categoria.fromString(this.categoria), // Converte String para Enum
                this.preco,
                this.descricao
        );
        // Se o DTO é usado para atualização e possui um ID, ele é repassado para a entidade de domínio
        if (this.id != null) {
            produto.setId(this.id);
        }
        return produto;
    }
}