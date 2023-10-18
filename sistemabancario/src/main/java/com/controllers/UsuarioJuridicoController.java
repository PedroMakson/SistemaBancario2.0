package com.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import com.App;
import com.models.dao.UsuarioJuridicoDAO;
import com.models.entity.Conexao;
import com.models.entity.UsuarioJuridico;

public class UsuarioJuridicoController {

    private static Connection conexao = Conexao.getInstancia();
    private static UsuarioJuridico usuarioJuridico;
    private static UsuarioJuridicoDAO usuarioJuridicoDAO = new UsuarioJuridicoDAO(conexao);
    private static Scanner scanner = new Scanner(System.in);

    // Método para cadastrar/inserir um usuário ao banco de dados.
    public static UsuarioJuridico cadastrarUsuarioJuridico() {

        try {
            // Solicita ao usuário que insira os dados
            String cnpj;
            do {
                System.out.println("+------------------------+");
                System.out.println("| C  A  D  A  S  T  R  O |");
                System.out.println("+------------------------+");

                System.out.print("-> CNPJ: ");
                cnpj = scanner.nextLine();

                // Verifica se o CPF contém apenas dígitos
                if (!cnpj.matches("\\d+")) {
                    App.limparTela();
                    System.out.println("\n > CNPJ deve conter apenas dígitos. <\n");
                    continue;
                }

                // Verifica se o CPF tem 11 dígitos
                if (cnpj.length() != 14) {
                    App.limparTela();
                    System.out.println("\n > CNPJ deve conter 14 dígitos. <\n");
                    continue;
                }

                // Verifica se um CPF já existe no banco de dados
                if (ContaController.buscarPorCNPJ(cnpj)) {
                    App.limparTela();
                    System.out.println("\n > Já existe um usuário com esse CNPJ, tente novamente. <\n");
                }

            } while (cnpj.length() != 14 || ContaController.buscarPorCNPJ(cnpj) || cnpj.startsWith("-"));

            String nomeFantasia;
            do {
                System.out.print("-> Nome fantasia: ");
                nomeFantasia = scanner.nextLine();

                // Verifica se o nome não contém números
                if (nomeFantasia.matches(".*\\d+.*")) {
                    App.limparTela();
                    System.out.println("\n > O nome não deve conter números. Tente novamente. <\n");
                    continue; // Volta ao início do loop para uma nova entrada
                }

                // Se o nome não contiver números, é válido
                break;
            } while (true);

            String razaoSocial;
            do {
                System.out.print("-> Razão social: ");
                razaoSocial = scanner.nextLine();

                // Verifica se o nome não contém números
                if (razaoSocial.matches(".*\\d+.*")) {
                    App.limparTela();
                    System.out.println("\n > A razão social não deve conter números. Tente novamente. <\n");
                    continue; // Volta ao início do loop para uma nova entrada
                }

                // Se o nome não contiver números, é válido
                break;
            } while (true);

            String inscricaoEstadual;
            do {
                System.out.print("-> Inscrição estadual: ");
                inscricaoEstadual = scanner.nextLine();

                // Verifica se o RG tem 8 dígitos
                if (inscricaoEstadual.length() != 12 || inscricaoEstadual.startsWith("-")) {
                    App.limparTela();
                    System.out.println(
                            "\n > IE inválido. O IE não deve ser negativo e deve conter exatamente 12 dígitos. <\n");
                    continue; // Volta ao início do loop para uma nova entrada
                }

                // Verifica se o RG contém apenas dígitos
                if (!inscricaoEstadual.matches("\\d+")) {
                    App.limparTela();
                    System.out.println("\n > RG não deve conter letras. <\n");
                    continue; // Volta ao início do loop para uma nova entrada
                }

                // Se passar por ambas as verificações, o RG é válido
                break;
            } while (true);

            double receita = 0;
            boolean receitaValida = false;
            do {
                System.out.print("-> Receita: R$");

                if (scanner.hasNextDouble()) {
                    receita = scanner.nextDouble();
                    scanner.nextLine(); // Limpar a linha (avançar para a próxima linha)

                    // Verifica se a receita é menor que zero
                    if (receita < 0) {
                        App.limparTela();
                        System.out.println(
                                "\n > Valor de receita inválido. A receita não pode ser negativa. Tente novamente. <\n");
                    } else {
                        receitaValida = true;
                    }
                } else {
                    App.limparTela();
                    System.out
                            .println("\n > Valor de receita inválido. Digite um valor numérico. Tente novamente. <\n");
                    scanner.nextLine(); // Limpar entrada inválida e avançar para a próxima linha
                }
            } while (!receitaValida);

            String telefone;
            do {
                System.out.print("-> Telefone: ");
                telefone = scanner.nextLine();

                // Verifica se o telefone contém apenas dígitos
                if (!telefone.matches("[\\d,]+")) {
                    App.limparTela();
                    System.out.println("\n > Telefone não deve conter letras. <\n");
                    continue;
                }

                // Verifica se o telefone tem 11 dígitos
                if (telefone.length() != 11) {
                    App.limparTela();
                    System.out.println("\n > Telefone deve conter exatamente 11 dígitos. <\n");
                }
            } while (!telefone.matches("\\d{11}"));
            
            String cepString;
            while (true) {
                System.out.print("-> CEP: ");
                cepString = scanner.nextLine();

                // Verifica se o CEP contém apenas dígitos
                if (!cepString.matches("\\d+")) {
                    App.limparTela();
                    System.out.println("\n > CEP deve conter apenas dígitos. <\n");
                    continue; // Volta ao início do loop para uma nova entrada
                }

                // Verifica se o CEP tem 9 dígitos
                if (cepString.length() != 8) {
                    App.limparTela();
                    System.out.println("\n > CEP deve conter exatamente 8 dígitos. <\n");
                    continue; // Volta ao início do loop para uma nova entrada
                }
                // Se passar por ambas as verificações, o CEP é válido
                break;
            }

            String cep = (cepString.substring(0, 5) + "-" + cepString.substring(5));

            System.out.print("-> Rua: ");
            String rua = scanner.nextLine();

            int numeroDaCasa = 0;
            boolean numeroValido = false;

            do {
                System.out.print("-> Número da Casa: ");
                String input = scanner.nextLine();

                if (input.matches("\\d+")) {
                    numeroDaCasa = Integer.parseInt(input);

                    if (numeroDaCasa < 0) {
                        App.limparTela();
                        System.out.println(
                                "\n > Número da casa não pode ser negativo. Digite um valor numérico válido. <\n");
                    } else {
                        numeroValido = true;
                    }
                } else {
                    App.limparTela();
                    System.out.println("\n > Número da casa inválido. Digite um valor numérico. <\n");
                }
            } while (!numeroValido);

            System.out.print("-> Bairro: ");
            String bairro = scanner.nextLine();

            System.out.print("-> Cidade: ");
            String cidade = scanner.nextLine();

            String uf;
            do {
                System.out.print("-> UF: ");
                uf = scanner.nextLine();

                // Verifica se a UF tem exatamente 2 caracteres
                if (uf.length() != 2) {
                    App.limparTela();
                    System.out.println("\n > A UF deve conter exatamente 2 caracteres. <\n");
                    continue; // Volta ao início do loop para uma nova entrada
                }

                // Verifica se a UF não contém números
                if (uf.matches(".*\\d+.*")) {
                    App.limparTela();
                    System.out.println("\n > A UF não deve conter números. <\n");
                    continue; // Volta ao início do loop para uma nova entrada
                }

                // Se passar por ambas as verificações, a UF é válida
                break;
            } while (true);

            // Cria um novo usuário com os dados inseridos pelo usuário
            usuarioJuridico = new UsuarioJuridico(cnpj, nomeFantasia, razaoSocial, inscricaoEstadual, receita, telefone, cep, rua, numeroDaCasa, bairro, cidade, uf);

            // Insere o novo usuário no banco de dados
            usuarioJuridicoDAO.inserirUsuarioJuridico(usuarioJuridico);

            return usuarioJuridico;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void visualizarInformacoesCNPJ(String cnpj) throws SQLException {
        UsuarioJuridico usuario = usuarioJuridicoDAO.buscarPorCNPJ(cnpj);

        if (usuario != null) {
            App.limparTela();
            System.out.println("\n+-----------------------------------------+");
            System.out.println("|  I N F O R M A Ç Õ E S  D A  C O N T A  |");
            System.out.println("+-----------------------------------------+");
            System.out.println("| Razão Social: " + usuario.getRazaoSocial());
            System.out.println("| Fantasia: " + usuario.getNomeFantasia());
            System.out.printf("| CNPJ: " + usuario.getCnpj().substring(0, 2) + "." + usuario.getCnpj().substring(2, 5)
                    + "." + usuario.getCnpj().substring(5, 8) + "/" + usuario.getCnpj().substring(8, 12) + "-"
                    + usuario.getCnpj().substring(12, 14));
            System.out.println("| IE: " + usuario.getInscricaoEstadual().substring(0, 3) + "."
                    + usuario.getInscricaoEstadual().substring(3, 6)
                    + "." + usuario.getInscricaoEstadual().substring(6, 9) + "."
                    + usuario.getInscricaoEstadual().substring(9, 12) + "\n");
            System.out.printf("| Receita: R$%.2f\n", usuario.getReceita());
            System.out.println("+-----------------------------------------+");
            System.out.println("|              C O N T A T O              |");
            System.out.println("+-----------------------------------------+");
            System.out.printf("| Telefone: (" + usuario.getTelefone().substring(0, 2) + ")"
                    + usuario.getTelefone().substring(2, 7) + "-" + usuario.getTelefone().substring(7)
                    + "\n");
            System.out.println("+-----------------------------------------+");
            System.out.println("|              E N D E R E Ç O            |");
            System.out.println("+-----------------------------------------+");
            System.out.println("| CEP: " + usuario.getCep());
            System.out.println("| Rua: " + usuario.getRua());
            System.out.println("| Número da Casa: " + usuario.getNumeroDaCasa());
            System.out.println("| Bairro: " + usuario.getBairro());
            System.out.println("| Cidade: " + usuario.getCidade());
            System.out.println("| UF: " + usuario.getUf());
        } else {
            System.out.println("Usuário com CNPJ " + cnpj + " não encontrado.");
        }
    }

    // Método que atualiza algum dado escolhido pelo usuário
    public static void atualizarDados(String cnpj) throws SQLException {

        // Verificar se o usuário com o CPF fornecido existe
        int opcao;

        do {
            do {
                // Exibir opções para atualização
                App.limparTela();
                System.out.println("+----------------------------------------------+");
                System.out.println("|   A T U A L I Z A R  I N F O R M A Ç Õ E S   |");
                System.out.println("+----------------------------------------------+");
                System.out.println("| 1 - Nome fantasia                            |");
                System.out.println("| 2 - Razão social                             |");
                System.out.println("| 3 - Inscrição estadual                       |");
                System.out.println("| 4 - Receita                                  |");
                System.out.println("| 5 - Telefone                                 |");
                System.out.println("| 6 - CEP                                      |");
                System.out.println("| 7 - Rua                                      |");
                System.out.println("| 8 - Número da Casa                           |");
                System.out.println("| 9 - Bairro                                   |");
                System.out.println("| 10 - Cidade                                  |");
                System.out.println("| 11 - UF                                      |");
                System.out.println("| 0 - Sair                                     |");
                System.out.println("+----------------------------------------------+");

                System.out.printf("| > Escolha o dado: ");
                opcao = scanner.nextInt();
                scanner.nextLine();
                System.out.println("+----------------------------------------------+");

                if ((opcao < 0 || opcao > 11)) {
                    System.out.println("\n > Opção inválida, tente novamente! <\n");
                }

            } while (opcao < 0 || opcao > 11);

            switch (opcao) {

                case 1: // ATUALIZAR NOME FANTASIA
                    String nome;
                    do {
                        System.out.print("| Novo nome: ");
                        nome = scanner.nextLine();

                        // Verifica se o nome não contém números
                        if (nome.matches(".*\\d+.*")) {
                            App.limparTela();
                            System.out.println("\n > O nome não deve conter números. Tente novamente. <\n");
                            continue; // Volta ao início do loop para uma nova entrada
                        }

                        // Se o nome não contiver números, é válido
                        if (usuarioJuridicoDAO.atualizarAtributoUsuario(cnpj, 1, nome)) {
                            App.limparTela();
                            System.out.println("\n > Nome atualizado com sucesso! < \n");
                            opcao = 0;
                        }
                        break;
                    } while (true);

                    break;

                case 2: // ATUALIZAR FANTASIA
                    String fantasia;
                    do {
                        System.out.print("| Nova fantasia: ");
                        fantasia = scanner.nextLine();

                        // Verifica se o nome não contém números
                        if (fantasia.matches(".*\\d+.*")) {
                            App.limparTela();
                            System.out.println("\n > A fantasia não deve conter números. Tente novamente. <\n");
                            continue; // Volta ao início do loop para uma nova entrada
                        }

                        // Se o nome não contiver números, é válido
                        if (usuarioJuridicoDAO.atualizarAtributoUsuario(cnpj, 2, fantasia)) {
                            App.limparTela();
                            System.out.println("\n > Fantasia atualizado com sucesso! < \n");
                            opcao = 0;
                        }
                        break;
                    } while (true);
                    break;

                case 3: // ATUALIZAR INSCRIÇÃO ESTADUAL
                    String inscricaoEstadual;
                    do {
                        System.out.print("-> Nova inscrição estadual: ");
                        inscricaoEstadual = scanner.nextLine();

                        // Verifica se o RG tem 8 dígitos
                        if (inscricaoEstadual.length() != 12 || inscricaoEstadual.startsWith("-")) {
                            App.limparTela();
                            System.out.println(
                                    "\n > IE inválido. O IE não deve ser negativo e deve conter exatamente 12 dígitos. <\n");
                            continue; // Volta ao início do loop para uma nova entrada
                        }

                        // Verifica se o RG contém apenas dígitos
                        if (!inscricaoEstadual.matches("\\d+")) {
                            App.limparTela();
                            System.out.println("\n > RG não deve conter letras. <\n");
                            continue; // Volta ao início do loop para uma nova entrada
                        }

                        if (usuarioJuridicoDAO.atualizarAtributoUsuario(cnpj, 3, inscricaoEstadual)) {
                            App.limparTela();
                            System.out.println("\n > Inscrição estadual atualizado com sucesso! < \n");
                            opcao = 0;
                        }
                        break;
                    } while (true);
                    break;

                case 4: // ATUALIZAR RECEITA
                    double receita = 0;
                    String receitaStr = "";
                    boolean receitaValida = false;

                    do {
                        System.out.print("| Nova receita: R$");
                        if (scanner.hasNextDouble()) {
                            receita = scanner.nextDouble();

                            // Verifica se a receita é menor que zero
                            if (receita < 0) {
                                App.limparTela();
                                System.out.println(
                                        "\n > Valor de receita inválido. A receita mensal não pode ser negativa. Tente novamente. <\n");
                            } else {
                                receitaValida = true;
                            }
                        } else {
                            App.limparTela();
                            System.out.println(
                                    "\n > Valor de receita inválido. Digite um valor numérico. Tente novamente. <\n");
                            scanner.next(); // Limpar entrada inválida
                        }
                    } while (!receitaValida);

                    receitaStr += receita;

                    // Após sair do loop, a receita é válida, então você pode tentar atualizá-la
                    if (usuarioJuridicoDAO.atualizarAtributoUsuario(cnpj, 4, String.valueOf(receitaStr))) {
                        App.limparTela();
                        System.out.println("\n > Renda atualizada com sucesso! <\n");
                        opcao = 0;
                    } else {
                        App.limparTela();
                        System.out.println("\n > Falha ao atualizar a receita. <\n");
                    }
                    break;

                case 5: // ATUALIZAR TELEFONE
                    String telefone;
                    do {
                        System.out.print("| Novo telefone: ");
                        telefone = scanner.nextLine();

                        // Verifica se o telefone contém apenas dígitos
                        if (!telefone.matches("\\d+")) {
                            App.limparTela();
                            System.out.println("\n > Telefone não deve conter letras. <\n");
                            continue;
                        }

                        // Verifica se o telefone tem 11 dígitos
                        if (telefone.length() != 11) {
                            App.limparTela();
                            System.out.println("\n > Telefone deve conter exatamente 11 dígitos. <\n");
                        }
                    } while (!telefone.matches("\\d{11}"));

                    if (usuarioJuridicoDAO.atualizarAtributoUsuario(cnpj, 5, telefone)) {
                        App.limparTela();

                        System.out.println("\n > Telefone atualizado com sucesso! <\n");
                        opcao = 0;
                    }
                    break;

                case 6: // ATUALIZAR CEP
                    String cepString = "";

                    while (true) {
                        System.out.print("| Novo CEP: ");
                        cepString = scanner.nextLine();

                        // Verifica se o CEP contém apenas dígitos
                        if (!cepString.matches("\\d+")) {
                            App.limparTela();
                            System.out.println("\n > CEP deve conter apenas dígitos. <\n");
                            continue; // Volta ao início do loop para uma nova entrada
                        }

                        // Verifica se o CEP tem 9 dígitos
                        if (cepString.length() != 8) {
                            App.limparTela();
                            System.out.println("\n > CEP deve conter exatamente 8 dígitos. <\n");
                            continue; // Volta ao início do loop para uma nova entrada
                        }
                        // Se passar por ambas as verificações, o CEP é válido
                        break;
                    }

                    String cep = (cepString.substring(0, 5) + "-" + cepString.substring(5));

                    if (usuarioJuridicoDAO.atualizarAtributoUsuario(cnpj, 6, cep)) {
                        App.limparTela();
                        System.out.println("\n > CEP atualizado com sucesso! <\n");
                        opcao = 0;
                    }
                    break;

                case 7: // ATUALIZAR RUA
                    System.out.print("| Nova rua: ");
                    String novaRua = scanner.nextLine();
                    usuarioJuridicoDAO.atualizarAtributoUsuario(cnpj, 7, novaRua);
                    App.limparTela();
                    System.out.println("\n > Rua atualizada com sucesso! <\n");
                    opcao = 0;
                    break;

                case 8: // ATUALIZAR NÚMERO DA CASA
                    int numeroDaCasa = 0;
                    boolean numeroValido = false;

                    do {
                        System.out.print("| Novo número da casa: ");
                        if (scanner.hasNextInt()) {
                            numeroDaCasa = scanner.nextInt();
                            if (numeroDaCasa < 0) {
                                App.limparTela();
                                System.out.println(
                                        "\n > Número da casa não pode ser negativo. Digite um valor numérico válido. <\n");
                            } else {
                                numeroValido = true;
                            }
                        } else {
                            App.limparTela();
                            System.out.println("\n > Número da casa inválido. Digite um valor numérico. <\n");
                            scanner.next(); // Limpar entrada inválida
                        }
                    } while (!numeroValido);

                    if (usuarioJuridicoDAO.atualizarAtributoUsuario(cnpj, 8, String.valueOf(numeroDaCasa))) {
                        App.limparTela();
                        System.out.println("\n > Número da casa atualizado com sucesso! <\n");
                        opcao = 0;
                    }
                    break;

                case 9: // ATUALIZAR BAIRRO
                    System.out.print("| Novo bairro: ");
                    String novoBairro = scanner.nextLine();
                    usuarioJuridicoDAO.atualizarAtributoUsuario(cnpj, 9, novoBairro);
                    App.limparTela();
                    System.out.println("\n > Bairro atualizado com sucesso! <\n");
                    opcao = 0;
                    break;

                case 10: // ATUALIZAR CIDADE
                    System.out.print("Nova cidade: ");
                    String novaCidade = scanner.nextLine();
                    usuarioJuridicoDAO.atualizarAtributoUsuario(cnpj, 10, novaCidade);
                    App.limparTela();
                    System.out.println("\n > Cidade atualizada com sucesso! <\n");
                    opcao = 0;
                    break;

                case 11: // ATUALIZAR UF
                    String uf;
                    do {
                        System.out.print("| Nova UF: ");
                        uf = scanner.nextLine();
                        // Verifica se a UF tem exatamente 2 caracteres
                        if (uf.length() != 2) {
                            App.limparTela();
                            System.out.println("\n > A UF deve conter exatamente 2 caracteres. <\n");
                            continue; // Volta ao início do loop para uma nova entrada
                        }

                        // Verifica se a UF não contém números
                        if (uf.matches(".*\\d+.*")) {
                            App.limparTela();
                            System.out.println("\n > A UF não deve conter números. <\n");
                            continue; // Volta ao início do loop para uma nova entrada
                        }

                        // Se passar por ambas as verificações, a UF é válida
                        break;
                    } while (true);

                    if (usuarioJuridicoDAO.atualizarAtributoUsuario(cnpj, 11, uf)) {
                        App.limparTela();
                        System.out.println("\n > UF atualizada com sucesso! <\n");
                        opcao = 0;
                    }
                    break;
                case 0:
                    // Sair do menu de atualização
                    App.limparTela();
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }

        } while (opcao != 0);
    }

    // Método que verifica o formato da data de nascimento e se o usuário é maior de
    // idade
    static Date stringParaData(String dataString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dataNascimento = sdf.parse(dataString);

            // Verificar se a data de nascimento é maior de 18 anos
            Calendar calNascimento = Calendar.getInstance();
            calNascimento.setTime(dataNascimento);
            Calendar calAtual = Calendar.getInstance();
            calAtual.add(Calendar.YEAR, -18);

            if (calNascimento.before(calAtual)) {
                return dataNascimento; // Data válida
            } else {
                App.limparTela();
                System.out.println("\n > Você deve ser maior de 18 anos para se cadastrar. Tente novamente. <\n");
                return null; // Data inválida
            }
        } catch (ParseException e) {
            App.limparTela();
            System.out.println(
                    "\n > Erro na conversão de data. Certifique-se de usar o formato AAAA-MM-DD. Tente novamente. <\n");
            return null; // Data inválida
        }
    }

}
