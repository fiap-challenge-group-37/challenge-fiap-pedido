package com.fiap.challenge.pedido.adapters.out.persistence;

import com.fiap.challenge.pedido.domain.entities.ItemPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "itens_pedido")
public class ItemPedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long produtoId; // Id do produto referenciado

    @Column(nullable = false)
    private String nomeProduto;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoEntity pedido;


    public ItemPedidoEntity(Long produtoId, String nomeProduto, Integer quantidade, BigDecimal precoUnitario, BigDecimal precoTotal, PedidoEntity pedido) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoTotal;
        this.pedido = pedido;
    }

    public ItemPedido toDomain() {
        return new ItemPedido(this.id, this.produtoId, this.nomeProduto, this.quantidade, this.precoUnitario, this.precoTotal);
    }

    public static ItemPedidoEntity fromDomain(ItemPedido itemPedido, PedidoEntity pedidoEntity) {
        ItemPedidoEntity entity = new ItemPedidoEntity(
                itemPedido.getProdutoId(),
                itemPedido.getNomeProduto(),
                itemPedido.getQuantidade(),
                itemPedido.getPrecoUnitario(),
                itemPedido.getPrecoTotal(),
                pedidoEntity
        );
        if (itemPedido.getId() != null) {
            entity.setId(itemPedido.getId());
        }
        return entity;
    }
}