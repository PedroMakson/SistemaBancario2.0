package com.models.entity;

import java.util.Date;

public class UsuarioFisico extends Usuario {

    private String cpf;
    private String nome;
    private String rg;
    private double renda;
    private Date dataNascimento;
    private String email;
    
    public UsuarioFisico(String cpf, String nome, String rg, double renda, String telefone, String email, Date dataNascimento, String cep, String rua, int numeroDaCasa, String bairro, String cidade, String uf) {
        super(telefone, cep, rua, numeroDaCasa, bairro, cidade, uf);
        this.cpf = cpf;
        this.nome = nome;
        this.rg = rg;
        this.renda = renda;
        this.dataNascimento = dataNascimento;
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public double getRenda() {
        return renda;
    }

    public void setRenda(double renda) {
        this.renda = renda;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}