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
            // Nomes de colunas ajustados
            String sql = "INSERT INTO leilao (gado_id, lance_minimo, data_inicio, data_fim) VALUES (?, ?, ?, ?)";
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
            e.printStackTrace();
            try { this.con.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            if (this.cmd != null) { try { this.cmd.close(); } catch (SQLException e) {} }
        }
    }

    public List<Leilao> listarTodosGestao() {
        try {
            // Nomes de colunas ajustados no SELECT e nos JOINs
            String sql = "SELECT l.id, l.gado_id, l.lance_minimo, COALESCE(MAX(ln.valor), 0) AS maior_lance, "
                    + "CASE "
                    + "  WHEN CURRENT_TIMESTAMP < l.data_inicio THEN 'AGENDADO' "
                    + "  WHEN CURRENT_TIMESTAMP BETWEEN l.data_inicio AND l.data_fim THEN 'EM ANDAMENTO' "
                    + "  ELSE 'ENCERRADO' "
                    + "END AS situacao "
                    + "FROM leilao l LEFT JOIN lance ln ON l.id = ln.leilao_id "
                    + "GROUP BY l.id, l.gado_id, l.lance_minimo, l.data_inicio, l.data_fim ORDER BY l.id";
            this.cmd = this.con.prepareStatement(sql);
            ResultSet rs = this.cmd.executeQuery();

            List<Leilao> lista = new ArrayList<>();
            while (rs.next()) {
                Leilao l = new Leilao();
                l.setId(rs.getInt("id"));

                Macho gadoPlaceholder = new Macho();
                gadoPlaceholder.setId(rs.getInt("gado_id"));
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
            e.printStackTrace();
            return null;
        } finally {
            if (this.cmd != null) { try { this.cmd.close(); } catch (SQLException e) {} }
        }
    }

    public List<Leilao> listarTodosParaLance() {
        try {
            // JOIN adicional com 'macho' para descobrir o sexo do gado no leilão
            String sql = "SELECT l.id, l.lance_minimo, g.id AS id_gado, g.nome, g.raca, g.peso, "
                    + "CASE WHEN m.gado_id IS NOT NULL THEN 'MACHO' ELSE 'FEMEA' END AS tipo_gado, "
                    + "COALESCE(MAX(ln.valor), 0) AS maior_lance "
                    + "FROM leilao l "
                    + "INNER JOIN gado g ON l.gado_id = g.id "
                    + "LEFT JOIN macho m ON g.id = m.gado_id "
                    + "LEFT JOIN lance ln ON l.id = ln.leilao_id "
                    + "WHERE CURRENT_TIMESTAMP BETWEEN l.data_inicio AND l.data_fim "
                    + "GROUP BY l.id, l.lance_minimo, g.id, g.nome, g.raca, g.peso, m.gado_id "
                    + "ORDER BY l.id";
                
            this.cmd = this.con.prepareStatement(sql);
            ResultSet rs = this.cmd.executeQuery();

            List<Leilao> lista = new ArrayList<>();
            while (rs.next()) {
                Leilao l = new Leilao();
                l.setId(rs.getInt("id"));
                l.setLanceMinimo(rs.getDouble("lance_minimo"));

                String tipo = rs.getString("tipo_gado");
                Gado g = tipo.equals("MACHO") ? new Macho() : new Femea();
                g.setId(rs.getInt("id_gado"));
                g.setNome(rs.getString("nome"));
                g.setRaca(org.openjfx.boilao.model.enums.RACA.valueOf(rs.getString("raca")));
                g.setPeso(rs.getDouble("peso"));
                l.setGado(g);

                Lance lancePlaceholder = new Lance();
                lancePlaceholder.setValor(rs.getDouble("maior_lance"));
                l.setMaiorLance(lancePlaceholder);

                lista.add(l);
            }
            return lista;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (this.cmd != null) { try { this.cmd.close(); } catch (SQLException e) {} }
        }
    }
}