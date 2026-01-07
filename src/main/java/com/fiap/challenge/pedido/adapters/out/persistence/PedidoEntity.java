package com.fiap.challenge.pedido.adapters.out.persistence;

import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import com.fiap.challenge.pedido.domain.entities.ItemPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String externalID;

    @Column
    private String qrCode;

    @Column
    private Long clienteId;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ItemPedidoEntity> itens = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatusPedido status;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    public Pedido toDomain() {
        List<ItemPedido> domainItens = this.itens.stream()
                .map(ItemPedidoEntity::toDomain)
                .toList();
        return new Pedido(this.id, this.clienteId, domainItens, this.valorTotal, this.status, this.dataCriacao, this.dataAtualizacao, this.externalID, this.qrCode);
    }

    public static PedidoEntity fromDomain(Pedido pedido) {
        PedidoEntity entity = new PedidoEntity();
        entity.setId(pedido.getId());
        entity.setClienteId(pedido.getClienteId());
        entity.setValorTotal(pedido.getValorTotal());
        entity.setStatus(pedido.getStatus());
        entity.setExternalID(pedido.getExternalID());
        entity.setQrCode(pedido.getQrCode());
        entity.setDataCriacao(
                Optional.ofNullable(pedido.getDataCriacao()).orElse(LocalDateTime.now())
        );
        entity.setDataAtualizacao(
                Optional.ofNullable(pedido.getDataAtualizacao()).orElse(LocalDateTime.now())
        );

        List<ItemPedidoEntity> itemEntities = Optional.ofNullable(pedido.getItens())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(itemDomain -> ItemPedidoEntity.fromDomain(itemDomain, entity))
                .toList();

        entity.setItens(itemEntities);
        return entity;
    }

    @PrePersist
    public void gerarExternalID() {
        if (externalID == null) {
            this.externalID = gerarReferenciaUnica("PED");
        }
    }

    public String gerarReferenciaUnica(String prefixo) {
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        return String.format("%s-%s-%s-%s", prefixo, data, timestamp, uuid).toUpperCase();
    }
}