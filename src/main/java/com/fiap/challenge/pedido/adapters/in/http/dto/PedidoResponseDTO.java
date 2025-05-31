package com.fiap.challenge.pedido.adapters.in.http.dto;

import com.fiap.challenge.pedido.domain.entities.Pedido;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Data
public class PedidoResponseDTO {
    private Long id;
    private Long clienteId;
    private List<ItemPedidoResponseDTO> itens; // Usaremos um DTO específico para itens na resposta
    private BigDecimal valorTotal;
    private String status; // String para facilitar a exibição
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String qrCode;

    public static PedidoResponseDTO fromDomain(Pedido pedido) {
        List<ItemPedidoResponseDTO> itemDTOs = pedido.getItens().stream()
                .map(ItemPedidoResponseDTO::fromDomain)
                .toList();
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getClienteId(),
                itemDTOs,
                pedido.getValorTotal(),
                pedido.getStatus().getDescricao(), // Usar a descrição do Enum
                pedido.getDataCriacao(),
                pedido.getDataAtualizacao(),
                pedido.getQrCode()
        );
    }
}