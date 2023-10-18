package com.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

import com.App;
import com.models.dao.ContaDAO;
import com.models.dao.EmprestimoDAO;
import com.models.dao.UsuarioFisicoDAO;
import com.models.dao.UsuarioJuridicoDAO;
import com.models.entity.Conexao;
import com.models.entity.Conta;
import com.models.entity.UsuarioFisico;
import com.models.entity.UsuarioJuridico;

public class ContaController {

    private static Connection conexao = Conexao.getInstancia();
    private static UsuarioFisico usuarioFisico;
    private static UsuarioJuridico usuarioJuridico;
    private static Conta conta;
    private static ContaDAO contaDAO = new ContaDAO(conexao);
    private static EmprestimoDAO emprestimoDAO = new EmprestimoDAO(conexao);
    private static UsuarioFisicoDAO usuarioFisicoDAO = new UsuarioFisicoDAO(conexao);
    private static UsuarioJuridicoDAO usuarioJuridicoDAO = new UsuarioJuridicoDAO(conexao);
    private static Scanner scanner = new Scanner(System.in);


    public static Conta cadastrarConta(int tipoConta) throws SQLException {

        if (tipoConta == 1) {
            usuarioFisico = UsuarioFisicoController.cadastrarUsuarioFisico();
        } else if (tipoConta == 2) {
            usuarioJuridico = UsuarioJuridicoController.cadastrarUsuarioJuridico();
        }

        if (usuarioFisico != null) {
            String senha, tipoDaConta;
            int opcaoTipoConta = 0;

            do {
                System.out.print("-> Tipo da conta [ 1 - Conta corrente | 2 - Conta poupança ]: ");
                opcaoTipoConta = scanner.nextInt();

                if (opcaoTipoConta < 1 || opcaoTipoConta > 2) {
                    App.limparTela();
                }

            } while (opcaoTipoConta < 1 || opcaoTipoConta > 2);

            if (opcaoTipoConta == 1) {
                tipoDaConta = "Conta Corrente";
            } else {
                tipoDaConta = "Conta Poupança";
            }

            do {
                System.out.print("-> Senha: ");
                senha = scanner.nextLine();

                // Verifica se a senha contém apenas dígitos
                if (!senha.matches("\\d{6}")) {
                    App.limparTela();
                    System.out.println("\n > A senha deve conter apenas dígitos numéricos. <\n");
                    continue;
                }

                // Verifica se a senha tem exatamente 6 dígitos
                if (senha.length() != 6) {
                    App.limparTela();
                    System.out.println("\n > A senha deve conter exatamente 6 dígitos. <\n");
                }
            } while (!senha.matches("\\d{6}"));

            conta = new Conta(usuarioFisico, tipoDaConta, senha);
            contaDAO.inserirContaFisica(usuarioFisico, conta);
        }

        if (usuarioJuridico != null) {
            String senha;

            do {

                System.out.print("-> Senha: ");
                senha = scanner.nextLine();

                // Verifica se a senha contém apenas dígitos
                if (!senha.matches("\\d{6}")) {
                    App.limparTela();
                    System.out.println("\n > A senha deve conter apenas dígitos numéricos. <\n");
                    continue;
                }

                // Verifica se a senha tem exatamente 6 dígitos
                if (senha.length() != 6) {
                    App.limparTela();
                    System.out.println("\n > A senha deve conter exatamente 6 dígitos. <\n");
                }
            } while (!senha.matches("\\d{6}"));

            conta = new Conta(usuarioFisico, "Conta Corrente", senha);
            contaDAO.inserirContaJuridica(usuarioJuridico, conta);
        }

        return conta;
    }

    public static boolean logar(String cpfCnpj, String senha) throws SQLException {

        if (contaDAO.validarUsuario(cpfCnpj, senha)) {
            return true;
        }
        return false;
    }

    public static boolean sacar(String cpfCnpj, double valor) throws SQLException {
        if (contaDAO.removerValor(cpfCnpj, valor)) {
            return true;
        }
        return false;
    }

    public static void depositar(String cpfCnpj, double valor) throws SQLException {
        contaDAO.adicionarValor(cpfCnpj, valor);
    }

    public static boolean transferir(String contaOrigem, String contaDestino, double valor, String senha)
            throws SQLException {
        int tentativas = 0;

        while (tentativas < 3) {
            tentativas++;
            contaDAO.obterDadosUsuarioContaPorCPF(contaDestino);

            String senhaUsuario;
            System.out.printf("| Tentativa %d de 3 \n| Confirmar senha: ", tentativas);
            senhaUsuario = scanner.next();

            if (senhaUsuario.equals(senha) && contaDAO.validarUsuario(contaOrigem, senha)) {
                if (contaDAO.transferirValor(contaOrigem, contaDestino, valor)) {
                    return true;
                } else {
                    return false; // Falha na transferência
                }
            } else {
                App.limparTela();
                System.out.println("\n > Senha inválida, tente novamente! <\n");
            }
        }

        return false; // Excedeu o número máximo de tentativas
    }

    public static void imprimirExtrato(String cpfCnpj) throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        java.sql.Date dataInicioSql = null;
        java.sql.Date dataFimSql = null;

        while (dataInicioSql == null || dataFimSql == null) {
            System.out.println("+-------------------------------------------+");
            System.out.println("|            E  X  T  R  A  T  O            |");
            System.out.println("+-------------------------------------------+");
            System.out.println("| Conta: " + cpfCnpj);
            System.out.println("+-------------------------------------------+");
            try {
                System.out.print("| Data inicio (AAAA-MM-DD): ");
                String dataInicioString = scanner.nextLine();
                Date dataInicio = dateFormat.parse(dataInicioString);
                dataInicioSql = new java.sql.Date(dataInicio.getTime());

                System.out.print("| Data fim (AAAA-MM-DD): ");
                String dataFimString = scanner.nextLine();
                Date dataFim = dateFormat.parse(dataFimString);
                dataFimSql = new java.sql.Date(dataFim.getTime());

                if (dataInicio.compareTo(dataFim) > 0) {
                    App.limparTela();
                    System.out.println(
                            "\n > A data de início não pode ser posterior à data de fim. Tente novamente. <\n");
                    dataInicioSql = null;
                    dataFimSql = null;
                }
            } catch (ParseException e) {
                App.limparTela();
                System.out.println("\n > Formato de data inválido. Tente novamente. <\n");
                dataInicioSql = null;
                dataFimSql = null;
            }
        }
        contaDAO.exibirTransacoes(cpfCnpj, dataInicioSql, dataFimSql);
        System.out.println("+-------------------------------------------+\n");

    }

    public static String alterarSenhaLogado(String cpfCnpj, String senha) throws SQLException {
        String novaSenha1 = "";
        String novaSenha2 = "";

        do {
            System.out.println("+-------------------------------------------+");
            System.out.println("|    A  L  T  E  R  A  R   S  E  N  H  A    |");
            System.out.println("+-------------------------------------------+");
            System.out.println("| Conta: " + cpfCnpj);
            System.out.println("+-------------------------------------------+");

            // Solicita a nova senha
            System.out.printf("| Nova senha: ");
            novaSenha1 = scanner.next();

            // Verifica se a nova senha atende aos critérios
            if (!novaSenha1.matches("\\d{6}")) {
                App.limparTela();
                System.out.println("\n > A senha deve conter exatamente 6 dígitos numéricos. <\n");
                continue;
            }

            // Solicita a confirmação da nova senha
            System.out.printf("| Confirmar nova senha: ");
            novaSenha2 = scanner.next();

            // Verifica se a confirmação da nova senha é igual à nova senha
            if (!novaSenha1.equals(novaSenha2)) {
                App.limparTela();
                System.out.println("\n > As senhas não condizem, tente novamente. <\n");
                continue;
            }

            // Verifica se a nova senha é igual à senha anterior
            if (novaSenha1.equals(senha)) {
                App.limparTela();
                System.out.println("\n > Você está usando uma senha atual, tente novamente. <\n");
            }
        } while (!novaSenha1.equals(novaSenha2) || novaSenha1.equals(senha));

        // Altera a senha no banco de dados
        contaDAO.mudarSenha(cpfCnpj, novaSenha1);
        App.limparTela();
        System.out.println("\n > Senha alterada com sucesso. <\n");
        return novaSenha1;
    }

    public static void alterarSenhaDeslogado() throws SQLException {

        String novaSenha1 = "";
        String novaSenha2 = "";
        String cpfCnpj = null;

        do {
            System.out.println("+-------------------------------------------+");
            System.out.println("|    A  L  T  E  R  A  R   S  E  N  H  A    |");
            System.out.println("+-------------------------------------------+");
            System.out.printf("| Conta (CPF/CNPJ): ");
            cpfCnpj = scanner.nextLine();

            if (cpfCnpj.length() == 11) {
                if (usuarioFisicoDAO.buscarPorCPFBollean(cpfCnpj) != true) {
                    App.limparTela();
                    System.out.println("\n > Usuário inexistente! <\n");
                }
            } else if (cpfCnpj.length() == 14) {
                if (usuarioJuridicoDAO.buscarPorCNPJBollean(cpfCnpj) != true) {
                    App.limparTela();
                    System.out.println("\n > Usuário inexistente! <\n");
                }
            } else {
                App.limparTela();
                System.out.println("\n > Usuário inexistente! <\n");
            }
        } while (usuarioFisicoDAO.buscarPorCPFBollean(cpfCnpj) != true
                && usuarioJuridicoDAO.buscarPorCNPJBollean(cpfCnpj) != true);

        do {
            App.limparTela();
            System.out.println("+-------------------------------------------+");
            System.out.println("|    A  L  T  E  R  A  R   S  E  N  H  A    |");
            System.out.println("+-------------------------------------------+");
            System.out.println("| Conta: " + cpfCnpj);
            System.out.println("+-------------------------------------------+");
            System.out.printf("| Nova senha: ");
            novaSenha1 = scanner.next();
            System.out.printf("| Confirmar nova senha: ");
            novaSenha2 = scanner.next();

            if (!(novaSenha1.equals(novaSenha2))) {
                App.limparTela();
                System.out.println("\n > As senhas não condizem, tente novamente. <\n");
            }
        } while (!(novaSenha1.equals(novaSenha2)));

        contaDAO.mudarSenha(cpfCnpj, novaSenha1);
        App.limparTela();
        System.out.println("\n > Senha alterada com sucesso. <\n");
    }

    public static void visualizarInformacoes(String cpfCnpj) throws SQLException {

        String agencia = contaDAO.obterAgenciaPorCPFCNPJ(cpfCnpj);
        String conta = contaDAO.obterNumeroDaContaPorCPFCNPJ(cpfCnpj);
        String tipoDaConta = contaDAO.obterTipoDaContaPorCPFCNPJ(cpfCnpj);
        LocalDate dataCadastro = contaDAO.obterDataCadastroPorCPFCNPJ(cpfCnpj);

        if (cpfCnpj.length() == 11) {
            UsuarioFisicoController.visualizarInformacoesCPF(cpfCnpj);
        } else if (cpfCnpj.length() == 14) {
            UsuarioJuridicoController.visualizarInformacoesCNPJ(cpfCnpj);
        }

        System.out.println("+-----------------------------------------+");
        System.out.println("|                 C O N T A               |");
        System.out.println("+-----------------------------------------+");
        System.out.println("| Agência: " + agencia);
        System.out.println("| Conta: " + conta.substring(0, 5) + "-" + conta.substring(5));
        System.out.println("| Tipo da conta: " + tipoDaConta);
        System.out.println("| Data cadastro: " + dataCadastro);
        System.out.println("+-----------------------------------------+\n");
    }

    public static double imprimirSaldo(String cpfCnpj) throws SQLException {
        return contaDAO.obterSaldoPorCPFCNPJ(cpfCnpj);
    }

    public static void excluirConta(String cpfCnpj) throws SQLException {

        double saldo = contaDAO.obterSaldoPorCPFCNPJ(cpfCnpj);

        if (emprestimoDAO.existeEmprestimoParaCPFCNPJ(cpfCnpj) != true) {
            if (saldo > 0) {
                double valorSaque;
                do {
                    System.out.printf(
                            "\n> ATENÇÃO: Para realizar a exclusão da conta, precisa sacar todo o seu dinheiro. <");
                    System.out.printf("\n\t\t\t   > SALDO ATUAL: R$%.2f <\n\n", saldo);
                    System.out.println("+-------------------------------+");
                    System.out.println("|         S  A  Q  U  E         |");
                    System.out.println("+-------------------------------+");
                    System.out.printf("| Digite o valor: R$");
                    valorSaque = scanner.nextDouble();

                    if (valorSaque > 0 && valorSaque <= saldo) {
                        App.limparTela();
                        ContaController.sacar(cpfCnpj, valorSaque);
                        saldo -= valorSaque; // Atualiza o saldo
                    } else if (valorSaque <= 0) {
                        App.limparTela();
                        System.out.printf("\n  > Saque de R$%.2f inválido. <\n\n", valorSaque);
                    } else if (valorSaque > saldo) {
                        App.limparTela();
                        System.out.println("\n  > Valor de saque superior ao saldo disponível. Tente novamente. <\n");
                    }

                } while (saldo > 0);
            }

            if (GerenteController.aprovarExclusao()) {
                contaDAO.excluirDados(cpfCnpj);
            } else {
                App.limparTela();
                System.out.println("\n > Gerente inválido. Tente novamente! <\n");
            }

        } else {
            System.out.println("\n > Sua conta está vinculada a um empréstimo, pague-o antes de excluir a conta. <\n");
        }
    }

    public static void desbloquearConta() throws SQLException {

        String cpfCnpj = "";
        boolean usuarioExistente = false;

        do {
            System.out.println("+-------------------------------------------+");
            System.out.println("|     D E S B L O Q U E A R  C O N T A      |");
            System.out.println("+-------------------------------------------+");
            System.out.printf("| Conta (CPF/CNPJ): ");
            cpfCnpj = scanner.nextLine();

            if (cpfCnpj.length() == 11) {
                usuarioExistente = usuarioFisicoDAO.buscarPorCPFBollean(cpfCnpj);
            } else if (cpfCnpj.length() == 14) {
                usuarioExistente = usuarioJuridicoDAO.buscarPorCNPJBollean(cpfCnpj);
            } else {
                // Se o comprimento do CPF/CNPJ não for 11 nem 14, é uma entrada inválida
                usuarioExistente = false;
            }

            if (!usuarioExistente) {
                App.limparTela();
                System.out.println("\n > Usuário inexistente! <\n");
            }

        } while (!usuarioExistente);

        if (GerenteController.aprovarDesbloqueio(cpfCnpj)) {
            App.limparTela();
            System.out.println("\n > Usuário desbloqueado com sucesso. <\n");
        } else {
            App.limparTela();
            System.out.println("\n > Gerente inválido, tente novamente. <\n");
        }

    }

    public static boolean buscarPorCPF(String cpf) throws SQLException {
        String sql = "SELECT cpf FROM usuariosFisico WHERE cpf = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next(); // Retorna true se o CPF existe no banco de dados, caso contrário, retorna
                                         // false
            }
        }
    }

    public static boolean buscarPorCNPJ(String cnpj) throws SQLException {
        String sql = "SELECT cnpj FROM usuariosJuridico WHERE cnpj = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cnpj);

            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next(); // Retorna true se o CPF existe no banco de dados, caso contrário, retorna
                                         // false
            }
        }
    }

    public static String buscarTipoConta(String cpfCnpj) throws SQLException {
        return contaDAO.obterTipoDaContaPorCPFCNPJ(cpfCnpj);
    }
}