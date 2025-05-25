package com.fiap.challenge.produto.adapters.out.persistence;

import com.fiap.challenge.produto.domain.entities.Produto;
import com.fiap.challenge.produto.domain.entities.Categoria;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Categoria categoria;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false, length = 255)
    private String descricao;

    public ProdutoEntity(String nome, Categoria categoria, BigDecimal preco, String descricao) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.descricao = descricao;
    }

    public Produto toDomain() {
        return new Produto(this.id, this.nome, this.categoria, this.preco, this.descricao);
    }

    public static ProdutoEntity fromDomain(Produto produto) {
        ProdutoEntity entity = new ProdutoEntity(
                produto.getNome(),
                produto.getCategoria(),
                produto.getPreco(),
                produto.getDescricao()
        );
        if (produto.getId() != null) {
            entity.setId(produto.getId());
        }
        return entity;
    }
}