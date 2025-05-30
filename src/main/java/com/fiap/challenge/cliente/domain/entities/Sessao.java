package com.fiap.challenge.cliente.domain.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Sessao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;
    private String token;
    private Date dataCriacao;

    // Getters
    public Long getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getToken() {
        return token;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}