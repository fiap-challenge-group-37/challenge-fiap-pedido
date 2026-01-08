package com.fiap.pedido.produto.application.service;

import com.fiap.pedido.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap.pedido.produto.application.exception.ApplicationServiceException;
import com.fiap.pedido.produto.application.exception.ProdutoNaoEncontradoException;
import com.fiap.pedido.produto.application.port.in.*;
import com.fiap.pedido.produto.domain.entities.Categoria;
import com.fiap.pedido.produto.domain.entities.Produto;
import com.fiap.pedido.produto.domain.port.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ProdutoApplicationService implements
        CriarProdutoUseCase,
        AtualizarProdutoUseCase,
        RemoverProdutoUseCase,
        BuscarProdutoPorIdUseCase,
        BuscarProdutoPorCategoriaUseCase,
        ListarTodosProdutosUseCase {

    private final ProdutoRepository produtoRepository;

    public ProdutoApplicationService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    @Override
    public Produto executar(ProdutoDTO produtoDTO) {
        Categoria categoria;
        try {
            categoria = Categoria.fromString(produtoDTO.getCategoria());
        } catch (IllegalArgumentException e) {
            throw new ApplicationServiceException("Categoria inválida fornecida: " + produtoDTO.getCategoria(), e);
        }

        Produto produto = new Produto(
                produtoDTO.getNome(),
                categoria, // Usa a categoria validada
                produtoDTO.getPreco(),
                produtoDTO.getDescricao()
        );

        return produtoRepository.save(produto);
    }

    @Transactional
    @Override
    public Produto executar(Long id, ProdutoDTO produtoDTO) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto com ID " + id + " não encontrado para atualização."));

        produtoExistente.setNome(produtoDTO.getNome());
        try {
            produtoExistente.setCategoria(Categoria.fromString(produtoDTO.getCategoria()));
        } catch (IllegalArgumentException e) {
            throw new ApplicationServiceException("Categoria inválida fornecida: " + produtoDTO.getCategoria(), e);
        }
        produtoExistente.setPreco(produtoDTO.getPreco());
        produtoExistente.setDescricao(produtoDTO.getDescricao());
        return produtoRepository.save(produtoExistente);
    }

    @Transactional
    @Override
    public void removerPorId(Long id) {
        if (produtoRepository.findById(id).isEmpty()) {
            throw new ProdutoNaoEncontradoException("Produto com ID " + id + " não encontrado para remoção.");
        }
        produtoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto com ID " + id + " não encontrado."));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Produto> executar(Categoria categoria) {
        return produtoRepository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Produto> executar() {
        return produtoRepository.findAll();
    }
}
