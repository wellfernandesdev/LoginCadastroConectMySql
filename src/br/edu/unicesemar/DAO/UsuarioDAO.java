package br.edu.unicesemar.DAO;

import br.edu.unicesumar.core.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author welli
 */
public class UsuarioDAO {
    private Connection conexao;

    public UsuarioDAO() {
        
        try {
            conexao = Conexao.getConexao();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a conexão com o banco de dados.");
        }
    }

    public void criarUsuario(Usuario u1) throws SQLException {
        String nome = u1.getNome();
        String login = u1.getLogin();
        String senha = u1.getSenha();
        String email = u1.getEmail();

        String sql = "INSERT INTO usuario (nome, login, senha, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, login);
            stmt.setString(3, senha);
            stmt.setString(4, email);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Cadastrado efetuado com sucesso.");
            } else {
                System.out.println("Falha ao cadastrar o usuário.");
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao criar o usuário.");
        }
    }

    public boolean verificarCredenciais(String login, String senha) throws SQLException {
    String sql = "SELECT * FROM usuario WHERE BINARY login = ? AND BINARY senha = ?";
    try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        stmt.setString(1, login);
        stmt.setString(2, senha);

        try (ResultSet resultSet = stmt.executeQuery()) {
            boolean found = resultSet.next();
            if (found) {
                System.out.println("Acesso autorizado: " + login);
            } else {
                System.out.println("Acesso negado: " + login);
            }
            return found;
        }
    } catch (SQLException e) {
        throw new SQLException("Erro ao verificar as credenciais do usuário: " + e.getMessage());
    }
}

    public void fecharConexao() throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }

    public void setConexao(Connection connection) {
        this.conexao = connection;
    }
}
