package com.fiap.challenge.pedido.domain.entities;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
public class Pedido {
    private Long id;
    private Long clienteId; // Opcional, conforme a descrição [cite: 14]
    private List<ItemPedido> itens;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String qrCode;

    // Construtor para novo pedido
    public Pedido(Long clienteId, List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Pedido deve conter ao menos um item.");
        }
        this.clienteId = clienteId; // Pode ser nulo se o cliente não se identificar [cite: 14]
        this.itens = new ArrayList<>(itens); // Cria uma cópia defensiva
        this.status = StatusPedido.AGUARDANDO_PAGAMENTO; // Status inicial [cite: 18]
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        calcularValorTotal();
    }

    // Construtor para reconstrução (ex: do banco de dados)
    public Pedido(Long id, Long clienteId, List<ItemPedido> itens, BigDecimal valorTotal, StatusPedido status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this(clienteId, itens); // Chama o construtor principal para validações e inicializações
        this.id = id;
        this.valorTotal = (valorTotal != null) ? valorTotal : this.valorTotal; // usa o calculado se não vier do BD
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    private void calcularValorTotal() {
        this.valorTotal = BigDecimal.ZERO;
        if (this.itens != null) {
            for (ItemPedido item : this.itens) {
                this.valorTotal = this.valorTotal.add(item.getPrecoTotal());
            }
        }
    }

    public void adicionarItem(ItemPedido item) {
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo.");
        }
        this.itens.add(item);
        this.dataAtualizacao = LocalDateTime.now();
        calcularValorTotal();
    }

    public void atualizarStatus(StatusPedido novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException("Novo status não pode ser nulo.");
        }
        // Adicionar lógica de transição de status se necessário
        // Ex: não pode ir de PRONTO para EM_PREPARACAO
        this.status = novoStatus;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
        this.dataAtualizacao = LocalDateTime.now();
    }
}