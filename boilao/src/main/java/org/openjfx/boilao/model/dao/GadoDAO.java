package org.openjfx.boilao.model.dao;

import java.sql.*;
import org.openjfx.boilao.model.DatabaseConnection;
import org.openjfx.boilao.model.Femea;
import org.openjfx.boilao.model.Gado;
import org.openjfx.boilao.model.Macho;

public class GadoDAO {

    private final Connection con;
    private PreparedStatement cmd;
    private Gado gado;

    public GadoDAO(Gado g) throws SQLException {
        this.gado = g;
        this.con = DatabaseConnection.getInstance().getConnection();
        this.con.setAutoCommit(false);
    }

    public boolean inserirMacho() {
        try {
            String sql = "INSERT INTO gado (nome, idade, raca, peso, nomepai, nomemae, observacoes, tipo_gado, taxa_sucesso, inseminacoes) VALUES (?, ?, ?, ?, ?, ?, ?, 'MACHO', ?, ?)";
            this.cmd = this.con.prepareStatement(sql);
            Macho m = (Macho) this.gado;
            this.cmd.setString(1, m.getNome());
            this.cmd.setInt(2, m.getIdade());
            this.cmd.setString(3, m.getRaca().name());
            this.cmd.setDouble(4, m.getPeso());
            this.cmd.setString(5, m.getNomePai());
            this.cmd.setString(6, m.getNomeMae());
            this.cmd.setString(7, m.getObservacoes());
            this.cmd.setDouble(8, m.getTaxaSucessoFertilizacao());
            this.cmd.setInt(9, m.getInseminacoesRealizadas());

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

    public boolean inserirFemea() {
        try {
            String sql = "INSERT INTO gado (nome, idade, raca, peso, nomepai, nomemae, observacoes, tipo_gado, prenhez, crias_anteriores) VALUES (?, ?, ?, ?, ?, ?, ?, 'FEMEA', ?, ?)";
            this.cmd = this.con.prepareStatement(sql);
            Femea f = (Femea) this.gado;
            this.cmd.setString(1, f.getNome());
            this.cmd.setInt(2, f.getIdade());
            this.cmd.setString(3, f.getRaca().name());
            this.cmd.setDouble(4, f.getPeso());
            this.cmd.setString(5, f.getNomePai());
            this.cmd.setString(6, f.getNomeMae());
            this.cmd.setString(7, f.getObservacoes());
            this.cmd.setString(8, f.getPrenhez().name());
            this.cmd.setInt(9, f.getCriasAnteriores());

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
            String sql = "DELETE FROM gado WHERE id = ?";
            this.cmd = this.con.prepareStatement(sql);
            this.cmd.setInt(1, this.gado.getId());

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

    public Gado listar(int id) {
        try {
            String sql = "SELECT id, tipo_gado, observacoes FROM gado WHERE id = ?";
            this.cmd = this.con.prepareStatement(sql);
            this.cmd.setInt(1, id);
            ResultSet rs = this.cmd.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo_gado");
                Gado g = tipo.equals("MACHO") ? new Macho() : new Femea();
                g.setId(rs.getInt("id"));
                g.setObservacoes(rs.getString("observacoes"));
                return g;
            }
            return null;
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
