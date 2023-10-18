package com.models.entity;

import java.time.LocalDate;

public class Conta {

    private UsuarioFisico usuarioFisico;
    private UsuarioJuridico usuarioJuridico;
    private String tipoDaConta;
    private int agencia;
    private int conta;
    private double saldo;
    private String senha;
    private boolean statusConta;
    private LocalDate dataCadastro;

    public Conta(UsuarioFisico usuarioFisico, String tipoDaConta, String senha) {
        this.usuarioFisico = usuarioFisico;
        this.tipoDaConta = tipoDaConta;
        this.agencia = 0;
        this.conta = 0;
        this.saldo = 0;
        this.senha = senha;
        this.statusConta = false;
        this.dataCadastro = LocalDate.now();
    }

    public Conta(UsuarioJuridico usuarioJuridico, String tipoDaConta, String senha) {
        this.usuarioJuridico = usuarioJuridico;
        this.tipoDaConta = tipoDaConta;
        this.agencia = 0;
        this.conta = 0;
        this.saldo = 0;
        this.senha = senha;
        this.statusConta = false;
        this.dataCadastro = LocalDate.now();
    }

    public UsuarioFisico getUsuarioFisico() {
        return usuarioFisico;
    }

    public void setUsuarioFisico(UsuarioFisico usuarioFisico) {
        this.usuarioFisico = usuarioFisico;
    }

    public UsuarioJuridico getUsuarioJuridico() {
        return usuarioJuridico;
    }

    public void setUsuarioJuridico(UsuarioJuridico usuarioJuridico) {
        this.usuarioJuridico = usuarioJuridico;
    }

    public String getTipoDaConta() {
        return tipoDaConta;
    }

    public void setTipoDaConta(String tipoDaConta) {
        this.tipoDaConta = tipoDaConta;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public int getConta() {
        return conta;
    }

    public void setConta(int conta) {
        this.conta = conta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isStatusConta() {
        return statusConta;
    }

    public void setStatusConta(boolean statusConta) {
        this.statusConta = statusConta;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

}