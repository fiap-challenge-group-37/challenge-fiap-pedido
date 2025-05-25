package com.fiap.challenge.produto.application.port.in;

// No longer returns boolean
public interface RemoverProdutoUseCase {
    void removerPorId(Long id);
}