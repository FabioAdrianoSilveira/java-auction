package org.openjfx.boilao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.openjfx.boilao.model.Usuario;
import org.openjfx.boilao.model.enums.TIPO;

public class UsuarioDAO {
    private Connection con;

    public UsuarioDAO() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    public boolean inserir(Usuario user) {
        String sql = "INSERT INTO usuario (cnpj, fazenda, senha, tipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement cmd = con.prepareStatement(sql)) {
            cmd.setString(1, user.getCnpj());
            cmd.setString(2, user.getFazenda());
            cmd.setString(3, user.getSenha());
            cmd.setString(4, user.getTipo().getValor());
            cmd.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean selecionarDadosLoginFazenda(Usuario user) {
        String sql = "SELECT cnpj, tipo FROM usuario WHERE fazenda = ? AND senha = ?";
        try (PreparedStatement cmd = con.prepareStatement(sql)) {
            cmd.setString(1, user.getFazenda());
            cmd.setString(2, user.getSenha());
            ResultSet rs = cmd.executeQuery();
            
            if (rs.next()) {
                user.setCnpj(rs.getString("cnpj"));
                
                String tipoBanco = rs.getString("tipo");
                user.setTipo("ADMINISTRADOR".equalsIgnoreCase(tipoBanco) ? TIPO.ADMINISTRADOR : TIPO.LICITANTE);
                
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String deletarUsuario(String cnpj) {
        try {
            // Regra: Administradores não podem ser deletados
            String sqlTipo = "SELECT tipo FROM usuario WHERE cnpj = ?";
            try (PreparedStatement cmd = con.prepareStatement(sqlTipo)) {
                cmd.setString(1, cnpj);
                ResultSet rs = cmd.executeQuery();
                
                if (rs.next() && "ADMINISTRADOR".equalsIgnoreCase(rs.getString("tipo"))) {
                    return "Erro: Administradores não podem ser deletados.";
                }
            }

            // Regra: Licitante com lance não pode ser deletado
            String sqlLance = "SELECT 1 FROM lance WHERE licitante_cnpj = ? LIMIT 1"; 
            try (PreparedStatement cmd = con.prepareStatement(sqlLance)) {
                cmd.setString(1, cnpj);
                if (cmd.executeQuery().next()) {
                    return "Erro: Licitante possui lances atrelados e não pode ser deletado.";
                }
            }

            String sqlDelete = "DELETE FROM usuario WHERE cnpj = ?";
            try (PreparedStatement cmd = con.prepareStatement(sqlDelete)) {
                cmd.setString(1, cnpj);
                cmd.executeUpdate();
                return "Usuário deletado com sucesso.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro de banco de dados.";
        }
    }

    public boolean alterarSenha(Usuario user) {
        String sql = "UPDATE usuario SET senha = ? WHERE cnpj = ?";
        try (PreparedStatement cmd = con.prepareStatement(sql)) {
            cmd.setString(1, user.getSenha());
            cmd.setString(2, user.getCnpj());
            return cmd.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}