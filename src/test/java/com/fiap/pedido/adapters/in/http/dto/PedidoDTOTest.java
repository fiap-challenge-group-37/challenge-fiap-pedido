package com.fiap.pedido.adapters.in.http.dto;

import com.fiap.pedido.pedido.adapters.in.http.dto.ItemPedidoDTO;
import com.fiap.pedido.pedido.adapters.in.http.dto.PedidoDTO;
import com.fiap.pedido.pedido.adapters.in.http.dto.StatusUpdateRequestDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit. jupiter.api.Assertions.*;

class PedidoDTOTest {

    @Test
    void deveInstanciarPedidoDTO() {
        PedidoDTO dto = new PedidoDTO();
        dto.setClienteId(1L);

        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoId(1L);
        item.setNomeProduto("Hamburguer");
        item.setQuantidade(2);
        item.setPreco(BigDecimal.valueOf(20.00));

        dto.setItens(List.of(item));

        assertNotNull(dto);
        assertEquals(1L, dto.getClienteId());
        assertEquals(1, dto.getItens().size());
        assertEquals("Hamburguer", dto.getItens().get(0).getNomeProduto());
    }

    @Test
    void deveInstanciarItemPedidoDTO() {
        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoId(10L);
        item.setNomeProduto("Refrigerante");
        item.setQuantidade(3);
        item.setPreco(BigDecimal.valueOf(5.00));

        assertEquals(10L, item.getProdutoId());
        assertEquals("Refrigerante", item.getNomeProduto());
        assertEquals(3, item.getQuantidade());
        assertEquals(BigDecimal.valueOf(5.00), item.getPreco());
    }

    @Test
    void deveInstanciarStatusUpdateRequestDTO() {
        StatusUpdateRequestDTO dto = new StatusUpdateRequestDTO();
        dto.setNovoStatus("EM_PREPARACAO");

        assertEquals("EM_PREPARACAO", dto.getNovoStatus());
    }
}