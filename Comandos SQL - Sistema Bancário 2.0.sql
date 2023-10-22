/* CRIAÇÃO DO BANCO DE DADOS - SISTEMA BANCÁRIO */
CREATE DATABASE sistemabancariopds;

/* CRIAÇÃO DA TABELA USUARIOS FISICOS*/
CREATE TABLE UsuariosFisico (
	cpf CHAR(11) NOT NULL,
	nome VARCHAR(100) NOT NULL,
	rg CHAR(9) NOT NULL,
	renda NUMERIC(10, 2) NOT NULL,
	telefone CHAR(11) NOT NULL,
	email VARCHAR(100) NOT NULL,
	dataNascimento DATE NOT NULL,
	cep CHAR(9) NOT NULL,
	rua VARCHAR(100) NOT NULL,
	numeroDaCasa INT NOT NULL,
	bairro VARCHAR(100) NOT NULL,
	cidade VARCHAR(100) NOT NULL,
 	uf CHAR(2) NOT NULL,
    PRIMARY KEY (cpf)
);

/* CRIAÇÃO DA TABELA USUARIOS JURIDICOS*/
CREATE TABLE UsuariosJuridico (
    cnpj CHAR(14) NOT NULL,
    nomeFantasia VARCHAR(100) NOT NULL,
    razaoSocial VARCHAR(100) NOT NULL,
    inscricaoEstadual CHAR(12) NOT NULL,
    receita NUMERIC(100, 2) NOT NULL,
    telefone CHAR(11) NOT NULL,
    cep CHAR(9) NOT NULL,
    rua VARCHAR(100) NOT NULL,
    numeroDaCasa INT NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    uf CHAR(2) NOT NULL,
    PRIMARY KEY (cnpj)
);

/* CRIAÇÃO DA TABELA GERENTES */
CREATE TABLE Gerentes (
	cpf CHAR(11) NOT NULL,
	nome VARCHAR(100) NOT NULL,
	rg CHAR(8) NOT NULL,
	telefone CHAR(11) NOT NULL,
	email VARCHAR(100) NOT NULL,
	dataNascimento DATE NOT NULL,
	cargo VARCHAR(50) NOT NULL,
	salario NUMERIC(10, 2) NOT NULL,
	senha CHAR(6) NOT NULL,
	cep CHAR(9) NOT NULL,
	rua VARCHAR(100) NOT NULL,
	numeroDaCasa INT NOT NULL,
	bairro VARCHAR(100) NOT NULL,
	cidade VARCHAR(100) NOT NULL,
	uf CHAR(2) NOT NULL,
	PRIMARY KEY (cpf)
);

/* CRIAÇÃO DA TABELA CONTAS */
CREATE TABLE Contas (
    cpf_cnpj VARCHAR NOT NULL,
    tipoconta VARCHAR(50) NOT NULL,
    agencia CHAR(4) NOT NULL,
    conta CHAR(6) NOT NULL,
    saldo NUMERIC(10,2) NOT NULL,
    senha CHAR(6) NOT NULL,
    statusconta BOOLEAN NOT NULL,
    datacadastro DATE DEFAULT CURRENT_DATE NOT NULL,
	PRIMARY KEY (cpf_cnpj)
);

/* CRIAÇÃO DA TABELA EMPRESTIMO */
CREATE TABLE Emprestimos (
    cpf_cnpj VARCHAR NOT NULL,
	documento VARCHAR NOT NULL,
	dataemissao DATE DEFAULT CURRENT_DATE NOT NULL,
	valortotal NUMERIC(10,2) NOT NULL,
	quantidadeparcelas INTEGER NOT NULL,
	valorjuros NUMERIC(4,2) NOT NULL,
	datavencimento DATE NOT NULL,
	parcelaatual INTEGER NOT NULL,
	valorparcela NUMERIC(10,2) NOT NULL,
	valorrecebido NUMERIC(10,2) NOT NULL,
	statusparcela BOOLEAN DEFAULT FALSE NOT NULL
);

/* CRIAÇÃO DA TABELA TRANSACOES */
CREATE TABLE Transacoes (
    cpf_cnpj VARCHAR NOT NULL,
   	descricao VARCHAR(150) NOT NULL,
   	valor NUMERIC(10, 2) NOT NULL,
    data DATE DEFAULT CURRENT_DATE NOT NULL
);