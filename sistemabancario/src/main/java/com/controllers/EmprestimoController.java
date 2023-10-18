package com.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.App;
import com.models.dao.ContaDAO;
import com.models.dao.EmprestimoDAO;
import com.models.dao.UsuarioFisicoDAO;
import com.models.dao.UsuarioJuridicoDAO;
import com.models.entity.Conexao;
import com.models.entity.Conta;
import com.models.entity.Emprestimo;

public class EmprestimoController {

    private static Connection conexao = Conexao.getInstancia();
    private static EmprestimoDAO emprestimoDAO = new EmprestimoDAO(conexao);
    private static ContaDAO contaDAO = new ContaDAO(conexao);
    private static UsuarioFisicoDAO usuarioFisicoDAO = new UsuarioFisicoDAO(conexao);
    private static UsuarioJuridicoDAO usuarioJuridicoDAO = new UsuarioJuridicoDAO(conexao);
    private static Scanner scanner = new Scanner(System.in);

    public static void solicitarEmprestimo(String cpfCnpj) throws SQLException {

        double rendaReceita = 0;
        double valorEmprestimo = 0;
        double valorEmprestimoComJuros = 0;
        int parcelas = 0, juros = 0;
        double validarValorParcela;

        if (cpfCnpj.length() == 11) {
            rendaReceita = usuarioFisicoDAO.obterRendaPorCPF(cpfCnpj);
            valorEmprestimo = (Math.round(((rendaReceita * 2.3) / 100)) * 100);
            valorEmprestimoComJuros = valorEmprestimo + (0.06 * valorEmprestimo);
            juros = 6;
        }

        if (cpfCnpj.length() == 14) {
            rendaReceita = usuarioJuridicoDAO.obterReceitaPorCNPJ(cpfCnpj);
            valorEmprestimo = (Math.round(((rendaReceita * 6) / 100)) * 100);
            valorEmprestimoComJuros = valorEmprestimo + (0.10 * valorEmprestimo);
            juros = 10;
        }

        do {
            System.out.println("+-------------------------------------------+");
            System.out.println("|       E  M  P  R  E  S  T  I  M  O        |");
            System.out.println("+-------------------------------------------+");
            System.out.printf("\t > RENDA ATUAL: R$%.2f <\n", rendaReceita);
            System.out.println("     Divida em até 12x com " + juros + "% de juros.");
            System.out.println("+-------------------------------------------+");
            System.out.printf("| > VALOR NORMAL: R$%.2f\n", valorEmprestimo);
            System.out.println("+-------------------------------------------+");
            System.out.printf("| > VALOR COM JUROS: R$%.2f\n", valorEmprestimoComJuros);
            System.out.println("+-------------------------------------------+");
            System.out.print("-> Quantidade de parcelas: ");
            parcelas = scanner.nextInt();

            validarValorParcela = valorEmprestimoComJuros / parcelas;

            if (parcelas < 1 || parcelas > 12) {
                App.limparTela();
                System.out.println("\n > Quantidade de parcelas inválidas. Digite novamente! <\n");
            } else {
                if (validarValorParcela > rendaReceita) {
                    App.limparTela();
                    System.out.printf("\n\n > O valor total ficou de R$%.2f em %dX com %.0f% de juros! <\n",
                            valorEmprestimoComJuros, parcelas, (double) juros);
                    System.out.printf(
                            " > As parcelas ficaram no valor de R$%.2f, o que não condiz com o valor da sua rendaReceita. <\n",
                            validarValorParcela);
                    System.out.println(" > Aumente o número de parcelas para adequar ao seu orçamento! <\n\n");
                }
            }
        } while (parcelas < 1 || parcelas > 12
                || (parcelas >= 1 && parcelas <= 12 && validarValorParcela > rendaReceita));

        if (GerenteController.aprovarEmprestimo()) {
            App.limparTela();
            Conta conta = contaDAO.buscarContaPorCPFCNPJ(cpfCnpj);
            Emprestimo emprestimo = new Emprestimo(conta, valorEmprestimoComJuros, parcelas, 6);
            emprestimoDAO.inserirEmprestimo(emprestimo);
            System.out.println("\n > Empréstimo aprovado com sucesso. <\n");

        } else {
            App.limparTela();
            System.out.println("\n > Gerente inválido. Empréstimo não foi aprovado. <\n");
        }

    }

    public static void visualizarEmprestimo(String cpfCnpj) throws SQLException {
        emprestimoDAO.exibirEmprestimosPorCPFCNPJ(cpfCnpj);
    }

    public static void pagarParcelaEmprestimo(String cpfCnpj) throws SQLException {

        String documento;
        int numeroParcela;
        do {
            System.out.println("\n+-------------------------------------------+");
            System.out.println("|    P  A  G  A  R   P  A  R  C  E  L  A    |");
            System.out.println("+-------------------------------------------+");
            System.out.printf("| Documento : ");
            documento = scanner.next();
            App.limparTela();

            if (!emprestimoDAO.documentoExiste(documento.toUpperCase())) {
                App.limparTela();
                System.out.println("\n > Documento inválido, tente novamente. <\n");
            }
        } while (!emprestimoDAO.documentoExiste(documento.toUpperCase()));

        boolean parcelaExiste;
        do {
            System.out.println("+-------------------------------------------+");
            System.out.println("|    P  A  G  A  R   P  A  R  C  E  L  A    |");
            System.out.println("+-------------------------------------------+");
            System.out.println("| Documento: " + documento.toUpperCase());
            System.out.printf("| Parcela : ");
            numeroParcela = scanner.nextInt();

            if (numeroParcela < 1 || numeroParcela > 12) {
                App.limparTela();
                System.out.println("\n > Parcela inválida, escolha entre 1 e 12. <\n");
            }

            parcelaExiste = emprestimoDAO.parcelaExisteParaDocumento(documento.toUpperCase(), numeroParcela);
            if (!parcelaExiste) {
                App.limparTela();
                System.out.println("\n > Parcela não existe. <\n");
            }

        } while (numeroParcela < 1 || numeroParcela > 12 || parcelaExiste != true);

        emprestimoDAO.pagarParcela(cpfCnpj, documento.toUpperCase(), numeroParcela);

    }
}