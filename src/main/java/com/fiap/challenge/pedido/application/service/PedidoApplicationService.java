package com.fiap.challenge.pedido.application.service;

import com.fiap.challenge.pagamento.application.ports.out.MercadoPagoGateway;
import com.fiap.challenge.pedido.application.exception.PedidoNaoEncontradoException;
import com.fiap.challenge.pedido.application.exception.ValidacaoPedidoException;
import com.fiap.challenge.pedido.application.port.in.*;
import com.fiap.challenge.pedido.domain.entities.ItemPedido;
import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import com.fiap.challenge.pedido.domain.port.PedidoRepository;
import com.fiap.challenge.produto.domain.entities.Produto;
import com.fiap.challenge.produto.application.port.in.BuscarProdutoPorIdUseCase;

import com.fiap.challenge.pedido.adapters.in.http.dto.PedidoDTO;
import com.fiap.challenge.pedido.adapters.in.http.dto.ItemPedidoDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoApplicationService implements CriarPedidoUseCase, ListarPedidosUseCase, BuscarPedidoPorIdUseCase, AtualizarStatusPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;
    private final MercadoPagoGateway mercadoPagoGateway;

    public PedidoApplicationService(PedidoRepository pedidoRepository, BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase, MercadoPagoGateway mercadoPagoGateway) {
        this.pedidoRepository = pedidoRepository;
        this.buscarProdutoPorIdUseCase = buscarProdutoPorIdUseCase;
        this.mercadoPagoGateway = mercadoPagoGateway;
    }

    @Transactional
    @Override
    public Pedido executar(PedidoDTO pedidoDTO) {
        List<ItemPedido> itensDominio = new ArrayList<>();
        if (pedidoDTO.getItens() == null || pedidoDTO.getItens().isEmpty()) {
            throw new ValidacaoPedidoException("O pedido deve conter pelo menos um item.");
        }

        for (ItemPedidoDTO itemDTO : pedidoDTO.getItens()) {
            Produto produto = buscarProdutoPorIdUseCase.buscarPorId(itemDTO.getProdutoId());

            itensDominio.add(new ItemPedido(
                    produto.getId(),
                    produto.getNome(),
                    itemDTO.getQuantidade(),
                    produto.getPreco() // Usar o preço do catálogo
            ));
        }

        Pedido pedido = pedidoRepository.save(new Pedido(pedidoDTO.getClienteId(), itensDominio));
        pedido.setQrCode(mercadoPagoGateway.criarPagamento(pedido).getQrData());
        pedidoRepository.saveQRCode(pedido);
        return pedido;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Pedido> executar() {
        return pedidoRepository.findAll(); // Idealmente ordenados pela lógica da fila
    }

    @Transactional(readOnly = true)
    @Override
    public List<Pedido> executar(Optional<String> statusOpt) {
        if (statusOpt.isPresent() && !statusOpt.get().trim().isEmpty()) {
            try {
                StatusPedido status = StatusPedido.fromString(statusOpt.get());
                return pedidoRepository.findByStatus(status);
            } catch (IllegalArgumentException e) {
                throw new ValidacaoPedidoException("Status inválido fornecido: " + statusOpt.get() + ". " + e.getMessage());
            }
        }
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido executar(String externalID) {
        return pedidoRepository.findByExternalId(externalID)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido com External id " + externalID + " não encontrado."));
    }

    @Transactional(readOnly = true)
    @Override
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido com ID " + id + " não encontrado."));
    }

    @Transactional
    @Override
    public Pedido executar(Long pedidoId, String novoStatusStr) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido com ID " + pedidoId + " não encontrado para atualização de status."));

        StatusPedido novoStatus;
        try {
            novoStatus = StatusPedido.fromString(novoStatusStr);
        } catch (IllegalArgumentException e) {
            throw new ValidacaoPedidoException("Status '" + novoStatusStr + "' inválido. " + e.getMessage());
        }
        pedido.atualizarStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }
}