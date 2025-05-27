package com.fiap.challenge.pedido.application.service;

import com.fiap.challenge.pedido.application.exception.PedidoNaoEncontradoException;
import com.fiap.challenge.pedido.application.exception.ValidacaoPedidoException;
import com.fiap.challenge.pedido.application.port.in.*;
import com.fiap.challenge.pedido.domain.entities.ItemPedido;
import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import com.fiap.challenge.pedido.domain.port.PedidoRepository;
import com.fiap.challenge.produto.domain.entities.Produto; // Para buscar detalhes do produto
import com.fiap.challenge.produto.application.port.in.BuscarProdutoPorIdUseCase; // Para buscar detalhes do produto

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
    private final BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase; // Injetar para buscar produtos

    public PedidoApplicationService(PedidoRepository pedidoRepository, BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase) {
        this.pedidoRepository = pedidoRepository;
        this.buscarProdutoPorIdUseCase = buscarProdutoPorIdUseCase;
    }

    @Transactional
    @Override
    public Pedido executar(PedidoDTO pedidoDTO) {
        List<ItemPedido> itensDominio = new ArrayList<>();
        if (pedidoDTO.getItens() == null || pedidoDTO.getItens().isEmpty()) {
            throw new ValidacaoPedidoException("O pedido deve conter pelo menos um item.");
        }

        for (ItemPedidoDTO itemDTO : pedidoDTO.getItens()) {
            // Buscar o produto para obter nome e preço, garantindo que ele existe e está ativo
            // (Implementação do buscarProdutoPorIdUseCase no módulo de produto deve lançar exceção se não encontrar)
            Produto produto = buscarProdutoPorIdUseCase.buscarPorId(itemDTO.getProdutoId());
            // Você pode adicionar mais validações aqui, como verificar se o produto está disponível

            itensDominio.add(new ItemPedido(
                    produto.getId(),
                    produto.getNome(),
                    itemDTO.getQuantidade(),
                    produto.getPreco() // Usar o preço do catálogo
            ));
        }

        Pedido novoPedido = new Pedido(pedidoDTO.getClienteId(), itensDominio);
        // Para o "fake checkout", apenas salvar o pedido é suficiente. [cite: 30]
        // A integração com pagamento (QRCode Mercado Pago) seria um próximo passo. [cite: 16]
        return pedidoRepository.save(novoPedido);
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
        // Se o status não for fornecido ou for vazio, listar todos os pedidos que não estão finalizados
        // e ordená-los: PRONTO primeiro, depois EM_PREPARACAO, depois RECEBIDO.
        // Esta lógica de ordenação pode ser mais complexa e ficar no repositório.
        // Por simplicidade aqui, vamos apenas chamar o findAll se o status não for válido.
        // No Tech Challenge pede para "Listar os pedidos" [cite: 31] e "Acompanhamento de pedidos" [cite: 23]
        // A ordem da fila é: prontos > em preparação > recebidos.
        // Isso pode ser implementado no repositório ou aqui com multiplas chamadas e concatenação.
        // Para simplificar, vamos retornar todos se o status for inválido/ausente.
        return pedidoRepository.findAll();
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