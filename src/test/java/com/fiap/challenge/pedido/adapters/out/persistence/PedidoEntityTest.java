package com.fiap.challenge.pedido.adapters.out.persistence;

import com.fiap.challenge.pedido.domain.entities.ItemPedido;
import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoEntityTest {

    private ItemPedido itemPedidoDominioExemplo;
    private ItemPedidoEntity itemPedidoEntityExemplo;
    private PedidoEntity pedidoEntityParaRelacionamento;

    @BeforeEach
    void setUp() {
        pedidoEntityParaRelacionamento = new PedidoEntity();
        pedidoEntityParaRelacionamento.setId(1L); // ID simulado

        // Item de domínio de exemplo
        itemPedidoDominioExemplo = new ItemPedido(1L, "Produto Teste", 1, BigDecimal.TEN);
        itemPedidoDominioExemplo.setId(10L);

        // Item de entidade de exemplo
        itemPedidoEntityExemplo = ItemPedidoEntity.fromDomain(itemPedidoDominioExemplo, pedidoEntityParaRelacionamento);
    }

    @Test
    @DisplayName("Deve criar PedidoEntity com construtor padrão e setters devem funcionar")
    void deveCriarComConstrutorPadraoESettersFuncionarem() {
        PedidoEntity entity = new PedidoEntity();
        Long id = 1L;
        Long clienteId = 100L;
        BigDecimal valorTotal = BigDecimal.valueOf(50.75);
        StatusPedido status = StatusPedido.EM_PREPARACAO;
        LocalDateTime dataCriacao = LocalDateTime.now().minusHours(1);
        LocalDateTime dataAtualizacao = LocalDateTime.now();
        List<ItemPedidoEntity> itens = new ArrayList<>(Collections.singletonList(itemPedidoEntityExemplo));

        entity.setId(id);
        entity.setClienteId(clienteId);
        entity.setValorTotal(valorTotal);
        entity.setStatus(status);
        entity.setDataCriacao(dataCriacao);
        entity.setDataAtualizacao(dataAtualizacao);
        entity.setItens(itens);

        assertEquals(id, entity.getId());
        assertEquals(clienteId, entity.getClienteId());
        assertEquals(valorTotal, entity.getValorTotal());
        assertEquals(status, entity.getStatus());
        assertEquals(dataCriacao, entity.getDataCriacao());
        assertEquals(dataAtualizacao, entity.getDataAtualizacao());
        assertEquals(1, entity.getItens().size());
        assertEquals(itemPedidoEntityExemplo, entity.getItens().get(0));
    }

    @Test
    @DisplayName("Deve converter de Pedido (domínio) para PedidoEntity corretamente")
    void deveConverterDeDominioParaEntity() {
        Long clienteId = 200L;
        List<ItemPedido> itensDominio = Collections.singletonList(itemPedidoDominioExemplo);
        // O valor total é calculado no construtor do Pedido de domínio
        Pedido pedidoDominio = new Pedido(clienteId, itensDominio);
        pedidoDominio.setId(2L); // Simular ID do domínio
        pedidoDominio.atualizarStatus(StatusPedido.PRONTO); // Mudar status para teste
        LocalDateTime dataCriacaoOriginal = pedidoDominio.getDataCriacao();
        LocalDateTime dataAtualizacaoOriginal = pedidoDominio.getDataAtualizacao();


        PedidoEntity entity = PedidoEntity.fromDomain(pedidoDominio);

        assertNotNull(entity);
        assertEquals(pedidoDominio.getId(), entity.getId());
        assertEquals(clienteId, entity.getClienteId());
        assertEquals(pedidoDominio.getValorTotal(), entity.getValorTotal());
        assertEquals(StatusPedido.PRONTO, entity.getStatus());
        assertEquals(dataCriacaoOriginal, entity.getDataCriacao());
        assertEquals(dataAtualizacaoOriginal, entity.getDataAtualizacao());
        assertNotNull(entity.getItens());
        assertEquals(1, entity.getItens().size());
        assertEquals(itemPedidoDominioExemplo.getProdutoId(), entity.getItens().get(0).getProdutoId());
        // Verifica se o PedidoEntity foi setado nos itens
        entity.getItens().forEach(item -> assertEquals(entity, item.getPedido()));
    }

    @Test
    @DisplayName("Deve converter de Pedido (domínio) sem ID para PedidoEntity corretamente")
    void deveConverterDeDominioSemIdParaEntity() {
        Long clienteId = 300L;
        List<ItemPedido> itensDominio = Collections.singletonList(itemPedidoDominioExemplo);
        Pedido pedidoDominioSemId = new Pedido(clienteId, itensDominio);
        // pedidoDominioSemId.setId(null); // ID é nulo por defeito

        PedidoEntity entity = PedidoEntity.fromDomain(pedidoDominioSemId);

        assertNotNull(entity);
        assertNull(entity.getId()); // ID da entidade deve ser nulo
        assertEquals(clienteId, entity.getClienteId());
    }

    @Test
    @DisplayName("Deve converter de PedidoEntity para Pedido (domínio) corretamente")
    void deveConverterDeEntityParaDominio() {
        Long id = 3L;
        Long clienteId = 300L;
        BigDecimal valorTotal = BigDecimal.valueOf(10.0);
        StatusPedido status = StatusPedido.FINALIZADO;
        LocalDateTime dataCriacao = LocalDateTime.now().minusDays(1);
        LocalDateTime dataAtualizacao = LocalDateTime.now();

        PedidoEntity entity = new PedidoEntity();
        entity.setId(id);
        entity.setClienteId(clienteId);
        entity.setValorTotal(valorTotal);
        entity.setStatus(status);
        entity.setDataCriacao(dataCriacao);
        entity.setDataAtualizacao(dataAtualizacao);
        entity.setItens(Collections.singletonList(itemPedidoEntityExemplo));

        Pedido pedidoDominio = entity.toDomain();

        assertNotNull(pedidoDominio);
        assertEquals(id, pedidoDominio.getId());
        assertEquals(clienteId, pedidoDominio.getClienteId());
        assertEquals(valorTotal, pedidoDominio.getValorTotal());
        assertEquals(status, pedidoDominio.getStatus());
        assertEquals(dataCriacao, pedidoDominio.getDataCriacao());
        assertEquals(dataAtualizacao, pedidoDominio.getDataAtualizacao());
        assertNotNull(pedidoDominio.getItens());
        assertEquals(1, pedidoDominio.getItens().size());
        assertEquals(itemPedidoEntityExemplo.getProdutoId(), pedidoDominio.getItens().get(0).getProdutoId());
    }
}
