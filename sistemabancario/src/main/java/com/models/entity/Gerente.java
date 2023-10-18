package com.models.entity;

import java.util.Date;

public class Gerente extends Usuario {

    private String cpf;
    private String nome;
    private String rg;
    private double renda;
    private Date dataNascimento;
    private String email;
    private String cargo;
    private double salario;
    private String senha;
    
    public Gerente(String telefone, String cep, String rua, int numeroDaCasa, String bairro, String cidade, String uf,
            String cpf, String nome, String rg, double renda, Date dataNascimento, String email, String cargo,
            double salario, String senha) {
        super(telefone, cep, rua, numeroDaCasa, bairro, cidade, uf);
        this.cpf = cpf;
        this.nome = nome;
        this.rg = rg;
        this.renda = renda;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.cargo = cargo;
        this.salario = salario;
        this.senha = senha;
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

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
}