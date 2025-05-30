package com.fiap.challenge.cliente.domain.port;

import com.fiap.challenge.cliente.domain.entities.Sessao;

import java.util.Optional;

public interface SessaoRepository {
    Optional<Sessao> findByToken(String token);
    Optional<Sessao> findByCpf(String cpf);
    Sessao save(Sessao sessao);
}