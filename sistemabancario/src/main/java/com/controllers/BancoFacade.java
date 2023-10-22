package com.controllers;

import java.sql.SQLException;
import com.models.entity.Conta;

public abstract class BancoFacade {

    // MÉTODOS CONTA

    public static Conta cadastrarConta(int tipoConta) throws SQLException {
        return ContaController.cadastrarConta(tipoConta);
    }

    public static boolean logar(String cpfCnpj, String senha) throws SQLException {
        return ContaController.logar(cpfCnpj, senha);
    }

    public static String buscarTipoConta(String cpfCnpj) throws SQLException {
        return ContaController.buscarTipoConta(cpfCnpj);
    }

    public static void depositar(String cpfCnpj, double valor) throws SQLException {
        ContaController.depositar(cpfCnpj, valor);
    }

    public static boolean sacar(String cpfCnpj, double valor) throws SQLException {
        return ContaController.sacar(cpfCnpj, valor);
    }

    public static void imprimirExtrato(String cpfCnpj) throws SQLException {
        ContaController.imprimirExtrato(cpfCnpj);
    }

    public static double imprimirSaldo(String cpfCnpj) throws SQLException {
        return ContaController.imprimirSaldo(cpfCnpj);
    }

    public static void visualizarInformacoes(String cpfCnpj) throws SQLException {
        ContaController.visualizarInformacoes(cpfCnpj);
    }

    public static String alterarSenhaLogado(String cpfCnpj, String senha) throws SQLException {
        return ContaController.alterarSenhaLogado(cpfCnpj, senha);
    }

    public static void excluirConta(String cpfCnpj) throws SQLException {
        ContaController.excluirConta(cpfCnpj);
    }

    public static boolean buscarPorCPF(String cpf) throws SQLException {
        return ContaController.buscarPorCPF(cpf);
    }

    public static boolean buscarPorCNPJ(String cnpj) throws SQLException {
        return ContaController.buscarPorCNPJ(cnpj);
    }

    public static boolean transferir(String contaOrigem, String contaDestino, double valor, String senha)
            throws SQLException {
        return ContaController.transferir(contaOrigem, contaDestino, valor, senha);
    }

    public static void alterarSenhaDeslogado() throws SQLException {
        ContaController.alterarSenhaDeslogado();
    }

    public static void desbloquearConta() throws SQLException {
        ContaController.desbloquearConta();
    }

    // MÉTODOS EMPRÉSTIMO

    public static void solicitarEmprestimo(String cpfCnpj) throws SQLException {
        EmprestimoController.solicitarEmprestimo(cpfCnpj);
    }

    public static void visualizarEmprestimo(String cpfCnpj) throws SQLException {
        EmprestimoController.visualizarEmprestimo(cpfCnpj);
    }

    public static void pagarParcelaEmprestimo(String cpfCnpj) throws SQLException {
        EmprestimoController.pagarParcelaEmprestimo(cpfCnpj);
    }

    // MÉTODOS USUÁRIOS FISICOS

    public static void atualizarDadosFisico(String cpf) throws SQLException {
        UsuarioFisicoController.atualizarDados(cpf);
    }

    // MÉTODOS USUÁRIOS JURIDICO

    public static void atualizarDadosJuridico(String cnpj) throws SQLException {
        UsuarioJuridicoController.atualizarDados(cnpj);
    }

    // MÉTODOS GERENTE

    public static boolean aprovarCadastro(Conta conta) throws SQLException {
        return GerenteController.aprovarCadastro(conta);
    }

}