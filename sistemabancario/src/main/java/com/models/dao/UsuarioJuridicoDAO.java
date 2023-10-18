package com.models.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.models.entity.UsuarioJuridico;

public class UsuarioJuridicoDAO {

    private Connection conexao;

    public UsuarioJuridicoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // Método para INSERIR um novo usuário jurídico no banco de dados
    public void inserirUsuarioJuridico(UsuarioJuridico usuario) throws SQLException {
        String sql = "INSERT INTO UsuariosJuridico (cnpj, nomeFantasia, razaoSocial, inscricaoEstadual, receita, telefone, cep, rua, numeroDaCasa, bairro, cidade, uf) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getCnpj());
            stmt.setString(2, usuario.getNomeFantasia());
            stmt.setString(3, usuario.getRazaoSocial());
            stmt.setString(4, usuario.getInscricaoEstadual());
            stmt.setDouble(5, usuario.getReceita());
            stmt.setString(6, usuario.getTelefone());
            stmt.setString(7, usuario.getCep());
            stmt.setString(8, usuario.getRua());
            stmt.setInt(9, usuario.getNumeroDaCasa());
            stmt.setString(10, usuario.getBairro());
            stmt.setString(11, usuario.getCidade());
            stmt.setString(12, usuario.getUf());

            stmt.executeUpdate();
        }
    }

    // Método para BUSCAR um usuário jurídico pelo CNPJ que retorna um RESULTSET
    public UsuarioJuridico buscarPorCNPJ(String cnpj) throws SQLException {
        String sql = "SELECT * FROM UsuariosJuridico WHERE cnpj = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cnpj);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return criarUsuarioJuridico(resultSet);
                }
            }
        }

        return null;
    }

    // Método para CRIAR um objeto UsuarioJuridico a partir de um ResultSet
    private UsuarioJuridico criarUsuarioJuridico(ResultSet resultSet) throws SQLException {
        String cnpj = resultSet.getString("cnpj");
        String nomeFantasia = resultSet.getString("nomeFantasia");
        String razaoSocial = resultSet.getString("razaoSocial");
        String inscricaoEstadual = resultSet.getString("inscricaoEstadual");
        double receita = resultSet.getDouble("receita");
        String telefone = resultSet.getString("telefone");
        String cep = resultSet.getString("cep");
        String rua = resultSet.getString("rua");
        int numeroDaCasa = resultSet.getInt("numeroDaCasa");
        String bairro = resultSet.getString("bairro");
        String cidade = resultSet.getString("cidade");
        String uf = resultSet.getString("uf");

        return new UsuarioJuridico(cnpj, nomeFantasia,
                razaoSocial, inscricaoEstadual, receita, telefone, cep, rua, numeroDaCasa, bairro, cidade, uf);
    }

    // Método para RETORNAR a receita de um usuário pelo CPF
    public double obterReceitaPorCNPJ(String cnpj) throws SQLException {
        String sql = "SELECT receita FROM UsuariosJuridico WHERE cnpj = ?";
        double receita = 0.0;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cnpj);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    receita = resultSet.getDouble("receita");
                }
            }
        }

        return receita;
    }

    // Método que ATUALIZA algum dado do cliente no banco
    public boolean atualizarAtributoUsuario(String cnpj, int caso, String novoValor) throws SQLException {
        String sql = "";
        String colunaAtributo = "";
        double novaReceita = 0;
        int novoNumeroCasa = 0;

        switch (caso) {
            case 1:
                colunaAtributo = "nomeFantasia";
                break;
            case 2:
                colunaAtributo = "razaoSocial";
                break;
            case 3:
                colunaAtributo = "inscricaoEstadual";
                break;
            case 4:
                colunaAtributo = "receita";
                novaReceita = Double.parseDouble(novoValor);
                break;
            case 5:
                colunaAtributo = "telefone";
                break;
            case 6:
                colunaAtributo = "cep";
                break;
            case 7:
                colunaAtributo = "rua";
                break;
            case 8:
                colunaAtributo = "numeroDaCasa";
                novoNumeroCasa = Integer.parseInt(novoValor);
                break;
            case 9:
                colunaAtributo = "bairro";
                break;
            case 10:
                colunaAtributo = "cidade";
                break;
            case 11:
                colunaAtributo = "uf";
                break;
            default:
                return false;
        }

        sql = "UPDATE UsuariosJuridico SET " + colunaAtributo + " = ? WHERE cnpj = ?";

        if (caso == 1 || caso == 2 || caso == 3 || caso == 5 || caso == 6 || caso == 7 || caso == 9 || caso == 10
                || caso == 11) {
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setString(1, novoValor);
                stmt.setString(2, cnpj);

                int linhasAfetadas = stmt.executeUpdate();
                return linhasAfetadas > 0;
            }
        }

        if (caso == 4) {
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setDouble(1, novaReceita);
                stmt.setString(2, cnpj);

                int linhasAfetadas = stmt.executeUpdate();
                return linhasAfetadas > 0;
            }
        }

        if (caso == 8) {
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, novoNumeroCasa);
                stmt.setString(2, cnpj);

                int linhasAfetadas = stmt.executeUpdate();
                return linhasAfetadas > 0;
            }
        }

        return false;
    }

    // Método que VERIFICA SE EXISTE um UsuariosJuridico com o CNPJ no banco
    public boolean buscarPorCNPJBollean(String cnpj) throws SQLException {
        String sql = "SELECT cnpj FROM UsuariosJuridico WHERE cnpj = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cnpj);

            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next(); // Retorna true se o CNPJ existe no banco de dados, caso contrário, retorna
                                         // false
            }
        }
    }
}