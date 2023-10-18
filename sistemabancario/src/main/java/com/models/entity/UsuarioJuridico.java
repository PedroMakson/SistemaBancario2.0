package com.models.entity;

public class UsuarioJuridico extends Usuario {

    private String cnpj;
    private String nomeFantasia;
    private String razaoSocial;
    private String inscricaoEstadual;
    private double receita;

    public UsuarioJuridico(String cnpj, String nomeFantasia, String razaoSocial, String inscricaoEstadual,
            double receita, String telefone, String cep, String rua, int numeroDaCasa, String bairro, String cidade,
            String uf) {
        super(telefone, cep, rua, numeroDaCasa, bairro, cidade, uf);
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.inscricaoEstadual = inscricaoEstadual;
        this.receita = receita;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public double getReceita() {
        return receita;
    }

    public void setReceita(double receita) {
        this.receita = receita;
    }

}