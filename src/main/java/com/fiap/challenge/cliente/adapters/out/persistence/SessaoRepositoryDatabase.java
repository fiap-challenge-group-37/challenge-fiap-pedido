package com.fiap.challenge.cliente.adapters.out.persistence;

import com.fiap.challenge.cliente.domain.entities.Sessao;
import com.fiap.challenge.cliente.domain.port.SessaoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SessaoRepositoryDatabase implements SessaoRepository {

    private final SessaoJpaRepository sessaoJpaRepository;

    public SessaoRepositoryDatabase(SessaoJpaRepository sessaoJpaRepository) {
        this.sessaoJpaRepository = sessaoJpaRepository;
    }

    @Override
    public Optional<Sessao> findByToken(String token) {
        return sessaoJpaRepository.findByToken(token)
                .map(SessaoEntity::toDomain);
    }

    @Override
    public Optional<Sessao> findByCpf(String cpf) {
        return sessaoJpaRepository.findByCpf(cpf)
                .map(SessaoEntity::toDomain);
    }

    @Override
    public Sessao save(Sessao sessao) {
        SessaoEntity entity = SessaoEntity.fromDomain(sessao);
        SessaoEntity saved = sessaoJpaRepository.save(entity);
        return saved.toDomain();
    }
}