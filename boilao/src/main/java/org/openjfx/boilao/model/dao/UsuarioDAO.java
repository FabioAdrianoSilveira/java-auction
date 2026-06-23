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
            String sql = "SELECT cnpj FROM usuario WHERE cnpj = ? AND senha = ?";
            this.cmd = this.con.prepareStatement(sql);
            this.cmd.setString(1, this.user.getCnpj());
            this.cmd.setString(2, this.user.getSenha());
            ResultSet rs = this.cmd.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            // A conexão Singleton não deve ser fechada depois da operação, apenas quando o programa for fechado
            if (this.cmd != null) {
                try {
                    this.cmd.close();
                } catch (SQLException e) {
                }
            }
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
            try {
                this.con.rollback();
            } catch (SQLException ex) {
            }
            return false;
        } finally {
            if (this.cmd != null) {
                try {
                    this.cmd.close();
                } catch (SQLException e) {
                }
            }
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
            try {
                this.con.rollback();
            } catch (SQLException ex) {
            }
            return false;
        } finally {
            if (this.cmd != null) {
                try {
                    this.cmd.close();
                } catch (SQLException e) {
                }
            }
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
            try {
                this.con.rollback();
            } catch (SQLException ex) {
            }
            return false;
        } finally {
            if (this.cmd != null) {
                try {
                    this.cmd.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public List<Usuario> listarLicitantes() {
        try {
            String sql = "SELECT cnpj, razao FROM usuario WHERE tipo = 'LICITANTE' ORDER BY razao";
            this.cmd = this.con.prepareStatement(sql);
            ResultSet rs = this.cmd.executeQuery();

            List<Usuario> lista = new ArrayList<>();
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setCnpj(rs.getString("cnpj"));
                u.setRazao(rs.getString("razao"));
                u.setTipo(TIPO.LICITANTE);
                lista.add(u);
            }
            return lista;
        } catch (SQLException e) {
            return null;
        } finally {
            if (this.cmd != null) {
                try {
                    this.cmd.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
