package com.models.entity;

public class Usuario {

    private String telefone;
    private String cep;
    private String rua;
    private int numeroDaCasa;
    private String bairro;
    private String cidade;
    private String uf;

    // Construtor com todos os atributos
    public Usuario(String telefone, String cep, String rua, int numeroDaCasa, String bairro, String cidade, String uf) {
        this.telefone = telefone;
        this.cep = cep;
        this.rua = rua;
        this.numeroDaCasa = numeroDaCasa;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNumeroDaCasa() {
        return numeroDaCasa;
    }

    public void setNumeroDaCasa(int numeroDaCasa) {
        this.numeroDaCasa = numeroDaCasa;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

}