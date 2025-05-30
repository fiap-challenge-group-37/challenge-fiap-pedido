package com.fiap.challenge.cliente.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessaoJpaRepository extends JpaRepository<SessaoEntity, Long> {
    Optional<SessaoEntity> findByToken(String token);
    Optional<SessaoEntity> findByCpf(String cpf);
}