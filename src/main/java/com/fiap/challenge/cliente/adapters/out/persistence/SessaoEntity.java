package com.fiap.challenge.cliente.adapters.out.persistence;

import com.fiap.challenge.cliente.domain.entities.Sessao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "sessao")
public class SessaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;
    private String token;
    private Date dataCriacao;

    public SessaoEntity() {}

    public Sessao toDomain() {
        Sessao sessao = new Sessao();
        sessao.setId(this.id);
        sessao.setCpf(this.cpf);
        sessao.setToken(this.token);
        sessao.setDataCriacao(this.dataCriacao);
        return sessao;
    }

    public static SessaoEntity fromDomain(Sessao sessao) {
        SessaoEntity entity = new SessaoEntity();
        entity.setId(sessao.getId());
        entity.setCpf(sessao.getCpf());
        entity.setToken(sessao.getToken());
        entity.setDataCriacao(sessao.getDataCriacao());
        return entity;
    }
}