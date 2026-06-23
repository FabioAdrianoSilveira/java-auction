package org.openjfx.boilao.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.openjfx.boilao.model.DatabaseConnection;
import org.openjfx.boilao.model.Usuario;
import org.openjfx.boilao.model.enums.TIPO;

public class UsuarioDAO {

    private final Connection con;
    private PreparedStatement cmd;
    private Usuario user;

    public UsuarioDAO(Usuario u) throws SQLException {
        this.user = u;
        this.con = DatabaseConnection.getInstance().getConnection();
        this.con.setAutoCommit(false);
    }

    public boolean login() {
        try {
            String sql = "SELECT cnpj, tipo, razao FROM usuario WHERE cnpj = ? AND senha = ?";
            this.cmd = this.con.prepareStatement(sql);
            this.cmd.setString(1, this.user.getCnpj());
            this.cmd.setString(2, this.user.getSenha());
            ResultSet rs = this.cmd.executeQuery();
            
            if (rs.next()) {
                this.user.setTipo(TIPO.valueOf(rs.getString("tipo")));
                this.user.setRazao(rs.getString("razao"));
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (this.cmd != null) { try { this.cmd.close(); } catch (SQLException e) {} }
        }
    }

    public boolean inserir() {
        try {
            String sql = "INSERT INTO usuario (cnpj, razao, senha, tipo) VALUES (?, ?, ?, ?)";
            this.cmd = this.con.prepareStatement(sql);
            this.cmd.setString(1, this.user.getCnpj());
            this.cmd.setString(2, this.user.getRazao());
            this.cmd.setString(3, this.user.getSenha());
            this.cmd.setString(4, this.user.getTipo().name());

            if (this.cmd.executeUpdate() > 0) {
                this.con.commit();
                return true;
            }
            this.con.rollback();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            try { this.con.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            if (this.cmd != null) { try { this.cmd.close(); } catch (SQLException e) {} }
        }
    }

    public boolean excluir() {
        try {
            String sql = "DELETE FROM usuario WHERE cnpj = ?";
            this.cmd = this.con.prepareStatement(sql);
            this.cmd.setString(1, this.user.getCnpj());

            if (this.cmd.executeUpdate() > 0) {
                this.con.commit();
                return true;
            }
            this.con.rollback();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            try { this.con.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            if (this.cmd != null) { try { this.cmd.close(); } catch (SQLException e) {} }
        }
    }

    public boolean atualizarSenha() {
        try {
            String sql = "UPDATE usuario SET senha = ? WHERE cnpj = ?";
            this.cmd = this.con.prepareStatement(sql);
            this.cmd.setString(1, this.user.getSenha());
            this.cmd.setString(2, this.user.getCnpj());

            if (this.cmd.executeUpdate() > 0) {
                this.con.commit();
                return true;
            }
            this.con.rollback();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            try { this.con.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            if (this.cmd != null) { try { this.cmd.close(); } catch (SQLException e) {} }
        }
    }

    public List<Usuario> listarLicitantes() {
        try {
            // Removido o filtro WHERE tipo = 'LICITANTE' para listar TODOS no Painel do Admin
            String sql = "SELECT cnpj, razao, tipo FROM usuario ORDER BY razao";
            this.cmd = this.con.prepareStatement(sql);
            ResultSet rs = this.cmd.executeQuery();

            List<Usuario> lista = new ArrayList<>();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setCnpj(rs.getString("cnpj"));
                u.setRazao(rs.getString("razao"));
                u.setTipo(TIPO.valueOf(rs.getString("tipo")));
                lista.add(u);
            }
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (this.cmd != null) { try { this.cmd.close(); } catch (SQLException e) {} }
        }
    }

    public Usuario getUser() {
        return user;
    }
}