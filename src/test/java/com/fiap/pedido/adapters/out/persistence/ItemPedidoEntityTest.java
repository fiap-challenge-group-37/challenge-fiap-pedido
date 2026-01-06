package com.fiap.challenge.pedido.adapters.out.persistence;

import com.fiap.challenge.pedido.domain.entities.ItemPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoEntityTest {

    private PedidoEntity pedidoEntityMock; // Mock ou instância real simples para associação

    @BeforeEach
    void setUp() {
        // Criar um mock simples ou uma instância real de PedidoEntity para os testes
        // Se PedidoEntity tiver dependências complexas, considere mockar com Mockito
        // Por simplicidade, vamos instanciar um PedidoEntity vazio para o relacionamento
        pedidoEntityMock = new PedidoEntity();
        pedidoEntityMock.setId(1L); // Definir um ID para o pedido mock
    }

    @Test
    @DisplayName("Deve criar ItemPedidoEntity com construtor completo e getters devem funcionar")
    void deveCriarComConstrutorCompletoEGettersFuncionarem() {
        Long produtoId = 1L;
        String nomeProduto = "Hamburguer";
        Integer quantidade = 2;
        BigDecimal precoUnitario = BigDecimal.TEN;
        BigDecimal precoTotal = BigDecimal.valueOf(20);

        ItemPedidoEntity entity = new ItemPedidoEntity(produtoId, nomeProduto, quantidade, precoUnitario, precoTotal, pedidoEntityMock);
        entity.setId(100L); // Simular ID gerado

        assertEquals(100L, entity.getId());
        assertEquals(produtoId, entity.getProdutoId());
        assertEquals(nomeProduto, entity.getNomeProduto());
        assertEquals(quantidade, entity.getQuantidade());
        assertEquals(precoUnitario, entity.getPrecoUnitario());
        assertEquals(precoTotal, entity.getPrecoTotal());
        assertEquals(pedidoEntityMock, entity.getPedido());
    }

    @Test
    @DisplayName("Deve criar ItemPedidoEntity com construtor padrão e setters devem funcionar")
    void deveCriarComConstrutorPadraoESettersFuncionarem() {
        ItemPedidoEntity entity = new ItemPedidoEntity();
        Long id = 101L;
        Long produtoId = 2L;
        String nomeProduto = "Batata Frita";
        Integer quantidade = 1;
        BigDecimal precoUnitario = BigDecimal.valueOf(5);
        BigDecimal precoTotal = BigDecimal.valueOf(5);

        entity.setId(id);
        entity.setProdutoId(produtoId);
        entity.setNomeProduto(nomeProduto);
        entity.setQuantidade(quantidade);
        entity.setPrecoUnitario(precoUnitario);
        entity.setPrecoTotal(precoTotal);
        entity.setPedido(pedidoEntityMock);

        assertEquals(id, entity.getId());
        assertEquals(produtoId, entity.getProdutoId());
        assertEquals(nomeProduto, entity.getNomeProduto());
        assertEquals(quantidade, entity.getQuantidade());
        assertEquals(precoUnitario, entity.getPrecoUnitario());
        assertEquals(precoTotal, entity.getPrecoTotal());
        assertEquals(pedidoEntityMock, entity.getPedido());
    }

    @Test
    @DisplayName("Deve converter de ItemPedido (domínio) para ItemPedidoEntity corretamente")
    void deveConverterDeDominioParaEntity() {
        Long produtoId = 3L;
        String nomeProduto = "Refrigerante";
        Integer quantidade = 3;
        BigDecimal precoUnitario = BigDecimal.valueOf(3);
        // Preço total será calculado no construtor do ItemPedido de domínio
        ItemPedido itemDominio = new ItemPedido(produtoId, nomeProduto, quantidade, precoUnitario);
        itemDominio.setId(200L); // Simular ID do domínio

        ItemPedidoEntity entity = ItemPedidoEntity.fromDomain(itemDominio, pedidoEntityMock);

        assertNotNull(entity);
        assertEquals(itemDominio.getId(), entity.getId());
        assertEquals(produtoId, entity.getProdutoId());
        assertEquals(nomeProduto, entity.getNomeProduto());
        assertEquals(quantidade, entity.getQuantidade());
        assertEquals(precoUnitario, entity.getPrecoUnitario());
        assertEquals(itemDominio.getPrecoTotal(), entity.getPrecoTotal());
        assertEquals(pedidoEntityMock, entity.getPedido());
    }

    @Test
    @DisplayName("Deve converter de ItemPedido (domínio) sem ID para ItemPedidoEntity corretamente")
    void deveConverterDeDominioSemIdParaEntity() {
        Long produtoId = 4L;
        String nomeProduto = "Agua";
        Integer quantidade = 1;
        BigDecimal precoUnitario = BigDecimal.valueOf(2);
        ItemPedido itemDominioSemId = new ItemPedido(produtoId, nomeProduto, quantidade, precoUnitario);
        // itemDominioSemId.setId(null); // ID é nulo por defeito

        ItemPedidoEntity entity = ItemPedidoEntity.fromDomain(itemDominioSemId, pedidoEntityMock);

        assertNotNull(entity);
        assertNull(entity.getId()); // ID da entidade deve ser nulo
        assertEquals(produtoId, entity.getProdutoId());
        assertEquals(nomeProduto, entity.getNomeProduto());
        assertEquals(quantidade, entity.getQuantidade());
        assertEquals(precoUnitario, entity.getPrecoUnitario());
        assertEquals(itemDominioSemId.getPrecoTotal(), entity.getPrecoTotal());
        assertEquals(pedidoEntityMock, entity.getPedido());
    }

    @Test
    @DisplayName("Deve converter de ItemPedidoEntity para ItemPedido (domínio) corretamente")
    void deveConverterDeEntityParaDominio() {
        Long id = 300L;
        Long produtoId = 5L;
        String nomeProduto = "Sorvete";
        Integer quantidade = 1;
        BigDecimal precoUnitario = BigDecimal.valueOf(8);
        BigDecimal precoTotal = BigDecimal.valueOf(8);

        ItemPedidoEntity entity = new ItemPedidoEntity(produtoId, nomeProduto, quantidade, precoUnitario, precoTotal, pedidoEntityMock);
        entity.setId(id);

        ItemPedido itemDominio = entity.toDomain();

        assertNotNull(itemDominio);
        assertEquals(id, itemDominio.getId());
        assertEquals(produtoId, itemDominio.getProdutoId());
        assertEquals(nomeProduto, itemDominio.getNomeProduto());
        assertEquals(quantidade, itemDominio.getQuantidade());
        assertEquals(precoUnitario, itemDominio.getPrecoUnitario());
        assertEquals(precoTotal, itemDominio.getPrecoTotal());
    }
}
