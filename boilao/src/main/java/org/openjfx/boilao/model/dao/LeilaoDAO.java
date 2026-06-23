package org.openjfx.boilao.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.openjfx.boilao.model.DatabaseConnection;
import org.openjfx.boilao.model.Femea;
import org.openjfx.boilao.model.Gado;
import org.openjfx.boilao.model.Lance;
import org.openjfx.boilao.model.Leilao;
import org.openjfx.boilao.model.Macho;

public class LeilaoDAO {

    private final Connection con;
    private PreparedStatement cmd;
    private final Leilao leilao;

    public LeilaoDAO(Leilao l) throws SQLException {
        this.leilao = l;
        this.con = DatabaseConnection.getInstance().getConnection();
        this.con.setAutoCommit(false);
    }

    public boolean inserir() {
        try {
            String sql = "INSERT INTO leilao (id_gado, lance_minimo, datainicio, datafim) VALUES (?, ?, ?, ?)";
            this.cmd = this.con.prepareStatement(sql);
            this.cmd.setInt(1, this.leilao.getGado().getId());
            this.cmd.setDouble(2, this.leilao.getLanceMinimo());
            this.cmd.setTimestamp(3, Timestamp.valueOf(this.leilao.getDataInicio()));
            this.cmd.setTimestamp(4, Timestamp.valueOf(this.leilao.getDataFim())); 

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

    public List<Leilao> listarTodosGestao() {
        try {
            String sql = "SELECT l.id, l.id_gado, l.lance_minimo, COALESCE(MAX(ln.valor), 0) AS maior_lance, "
                    + "CASE "
                    + "  WHEN CURRENT_TIMESTAMP < l.datainicio THEN 'AGENDADO' "
                    + "  WHEN CURRENT_TIMESTAMP BETWEEN l.datainicio AND l.datafim THEN 'EM ANDAMENTO' "
                    + "  ELSE 'ENCERRADO' "
                    + "END AS situacao "
                    + "FROM leilao l LEFT JOIN lance ln ON l.id = ln.id_leilao "
                    + "GROUP BY l.id, l.id_gado, l.lance_minimo, l.datainicio, l.datafim ORDER BY l.id";
            this.cmd = this.con.prepareStatement(sql);
            ResultSet rs = this.cmd.executeQuery();

            List<Leilao> lista = new ArrayList<>();
            while (rs.next()) {
                Leilao l = new Leilao();
                l.setId(rs.getInt("id"));

                Macho gadoPlaceholder = new Macho();
                gadoPlaceholder.setId(rs.getInt("id_gado"));
                l.setGado(gadoPlaceholder);

                l.setLanceMinimo(rs.getDouble("lance_minimo"));

                Lance lancePlaceholder = new Lance();
                lancePlaceholder.setValor(rs.getDouble("maior_lance"));
                l.setMaiorLance(lancePlaceholder);

                gadoPlaceholder.setObservacoes(rs.getString("situacao"));

                lista.add(l);
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

    public List<Leilao> listarTodosParaLance() {
        try {
            String sql = "SELECT l.id, g.tipo_gado, l.lance_minimo, COALESCE(MAX(ln.valor), 0) AS maior_lance "
                    + "FROM leilao l INNER JOIN gado g ON l.id_gado = g.id "
                    + "LEFT JOIN lance ln ON l.id = ln.id_leilao "
                    + "WHERE CURRENT_TIMESTAMP BETWEEN l.datainicio AND l.datafim "
                    + "GROUP BY l.id, g.tipo_gado, l.lance_minimo ORDER BY l.id";
            this.cmd = this.con.prepareStatement(sql);
            ResultSet rs = this.cmd.executeQuery();

            List<Leilao> lista = new ArrayList<>();
            while (rs.next()) {
                Leilao l = new Leilao();
                l.setId(rs.getInt("id"));

                String tipo = rs.getString("tipo_gado");
                Gado g = tipo.equals("MACHO") ? new Macho() : new Femea();
                l.setGado(g);

                l.setLanceMinimo(rs.getDouble("lance_minimo"));

                Lance lancePlaceholder = new Lance();
                lancePlaceholder.setValor(rs.getDouble("maior_lance"));
                l.setMaiorLance(lancePlaceholder);

                lista.add(l);
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
