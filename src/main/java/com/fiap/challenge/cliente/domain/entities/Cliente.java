package com.fiap.challenge.cliente.domain.entities;

public class Cliente {
    private Cpf cpf;
    private String nome;
    private String email;
    private String role; // Adicionado

    public Cliente(String cpf, String nome, String email, String role) {
        this.cpf = new Cpf(cpf);
        this.nome = nome;
        this.email = email;
        this.role = role;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public void setCpf(Cpf cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}