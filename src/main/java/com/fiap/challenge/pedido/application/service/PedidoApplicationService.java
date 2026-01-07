package com.fiap.challenge.pedido. application. service;

import com.fiap.challenge.pedido.application.exception.PedidoNaoEncontradoException;
import com.fiap.challenge.pedido.application.exception.ValidacaoPedidoException;
import com.fiap.challenge.pedido.application. port.in.*;
import com.fiap.challenge.pedido.domain.entities.ItemPedido;
import com. fiap.challenge.pedido. domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import com.fiap.challenge.pedido.domain.port.PedidoRepository;
import com.fiap.challenge.pedido.adapters.in.http.dto.PedidoDTO;
import com.fiap.challenge. pedido.adapters.in. http.dto.ItemPedidoDTO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation. Transactional;

import java. util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoApplicationService implements CriarPedidoUseCase, ListarPedidosUseCase, BuscarPedidoPorIdUseCase, AtualizarStatusPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public PedidoApplicationService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    @Override
    public Pedido executar(PedidoDTO pedidoDTO) {
        List<ItemPedido> itensDominio = new ArrayList<>();
        if (pedidoDTO.getItens() == null || pedidoDTO.getItens().isEmpty()) {
            throw new ValidacaoPedidoException("O pedido deve conter pelo menos um item.");
        }

        // Como agora não temos acesso direto ao catálogo de produtos,
        // assumimos que os dados vêm completos do DTO
        for (ItemPedidoDTO itemDTO : pedidoDTO.getItens()) {
            if (itemDTO.getProdutoId() == null) {
                throw new ValidacaoPedidoException("Produto ID não pode ser nulo");
            }
            if (itemDTO.getQuantidade() == null || itemDTO.getQuantidade() <= 0) {
                throw new ValidacaoPedidoException("Quantidade deve ser maior que zero");
            }
            if (itemDTO.getPreco() == null || itemDTO.getPreco().doubleValue() <= 0) {
                throw new ValidacaoPedidoException("Preço deve ser maior que zero");
            }

            itensDominio.add(new ItemPedido(
                    itemDTO.getProdutoId(),
                    itemDTO.getNomeProduto(), // Nome deve vir do DTO
                    itemDTO.getQuantidade(),
                    itemDTO.getPreco() // Preço deve vir do DTO
            ));
        }

        Pedido pedido = pedidoRepository.save(new Pedido(pedidoDTO. getClienteId(), itensDominio));

        // TODO: Integrar com serviço de pagamento via API REST ou mensageria
        // String qrCode = chamarServicoPagamento(pedido);
        // pedido.setQrCode(qrCode);
        // pedidoRepository.saveQRCode(pedido);

        return pedido;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Pedido> executar() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Pedido> executar(Optional<String> statusOpt) {
        if (statusOpt.isPresent() && !statusOpt.get().trim().isEmpty()) {
            try {
                StatusPedido status = StatusPedido.fromString(statusOpt.get());
                return pedidoRepository. findByStatus(status);
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
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido com ID " + id + " não encontrado. "));
    }

    @Transactional
    @Override
    public Pedido executar(Long pedidoId, String novoStatusStr) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoNaoEncontradoException("Pedido com ID " + pedidoId + " não encontrado para atualização de status. "));

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