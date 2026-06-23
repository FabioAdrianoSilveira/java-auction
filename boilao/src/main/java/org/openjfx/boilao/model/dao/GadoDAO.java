package org.openjfx.boilao.model.dao;

import java.sql.*;
import org.openjfx.boilao.model.DatabaseConnection;
import org.openjfx.boilao.model.Femea;
import org.openjfx.boilao.model.Gado;
import org.openjfx.boilao.model.Macho;
import org.openjfx.boilao.model.enums.PRENHEZ;
import org.openjfx.boilao.model.enums.RACA;

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
            // 1. Gera o próximo ID manualmente (resolve o problema da tabela não ter auto-incremento)
            int novoId = 1;
            try (Statement st = this.con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(id), 0) + 1 FROM gado")) {
                if (rs.next()) {
                    novoId = rs.getInt(1);
                }
            }

            // 2. Insere na tabela gado já enviando o ID calculado
            String sqlGado = "INSERT INTO gado (id, nome, idade, raca, peso, nome_pai, nome_mae, observacoes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            this.cmd = this.con.prepareStatement(sqlGado);
            Macho m = (Macho) this.gado;
            this.cmd.setInt(1, novoId);
            this.cmd.setString(2, m.getNome());
            this.cmd.setInt(3, m.getIdade());
            this.cmd.setString(4, m.getRaca().name());
            this.cmd.setDouble(5, m.getPeso());
            this.cmd.setString(6, m.getNomePai());
            this.cmd.setString(7, m.getNomeMae());
            this.cmd.setString(8, m.getObservacoes());

            if (this.cmd.executeUpdate() > 0) {
                // 3. Usa o mesmo ID para inserir na tabela macho
                String sqlMacho = "INSERT INTO macho (gado_id, taxa_sucesso_fertilizacao, inseminacoes_realizadas) VALUES (?, ?, ?)";
                try (PreparedStatement cmdMacho = this.con.prepareStatement(sqlMacho)) {
                    cmdMacho.setInt(1, novoId);
                    cmdMacho.setDouble(2, m.getTaxaSucessoFertilizacao());
                    cmdMacho.setInt(3, m.getInseminacoesRealizadas());
                    cmdMacho.executeUpdate();
                }
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

    public boolean inserirFemea() {
        try {
            int novoId = 1;
            try (Statement st = this.con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(id), 0) + 1 FROM gado")) {
                if (rs.next()) {
                    novoId = rs.getInt(1);
                }
            }

            String sqlGado = "INSERT INTO gado (id, nome, idade, raca, peso, nome_pai, nome_mae, observacoes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            this.cmd = this.con.prepareStatement(sqlGado);
            Femea f = (Femea) this.gado;
            this.cmd.setInt(1, novoId);
            this.cmd.setString(2, f.getNome());
            this.cmd.setInt(3, f.getIdade());
            this.cmd.setString(4, f.getRaca().name());
            this.cmd.setDouble(5, f.getPeso());
            this.cmd.setString(6, f.getNomePai());
            this.cmd.setString(7, f.getNomeMae());
            this.cmd.setString(8, f.getObservacoes());

            if (this.cmd.executeUpdate() > 0) {
                String sqlFemea = "INSERT INTO femea (gado_id, prenhez, crias_anteriores) VALUES (?, ?, ?)";
                try (PreparedStatement cmdFemea = this.con.prepareStatement(sqlFemea)) {
                    cmdFemea.setInt(1, novoId);
                    cmdFemea.setString(2, f.getPrenhez().name());
                    cmdFemea.setInt(3, f.getCriasAnteriores());
                    cmdFemea.executeUpdate();
                }
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
            // Apaga das tabelas filhas primeiro para não dar erro de chave estrangeira
            String sqlMacho = "DELETE FROM macho WHERE gado_id = ?";
            try (PreparedStatement cmd1 = this.con.prepareStatement(sqlMacho)) {
                cmd1.setInt(1, this.gado.getId());
                cmd1.executeUpdate();
            }
            String sqlFemea = "DELETE FROM femea WHERE gado_id = ?";
            try (PreparedStatement cmd2 = this.con.prepareStatement(sqlFemea)) {
                cmd2.setInt(1, this.gado.getId());
                cmd2.executeUpdate();
            }

            // Depois apaga o gado
            String sqlGado = "DELETE FROM gado WHERE id = ?";
            this.cmd = this.con.prepareStatement(sqlGado);
            this.cmd.setInt(1, this.gado.getId());

            if (this.cmd.executeUpdate() > 0) {
                this.con.commit();
                return true;
            }
            this.con.rollback();
            return false;
        } catch (SQLException e) {
            try { this.con.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            if (this.cmd != null) { try { this.cmd.close(); } catch (SQLException e) {} }
        }
    }

    public Gado listar(int id) {
        try {
            // Fazemos JOIN com macho e femea para pegar os dados e descobrir qual é o tipo
            String sql = "SELECT g.id, g.nome, g.idade, g.raca, g.peso, g.nome_pai, g.nome_mae, g.observacoes, "
                       + "m.taxa_sucesso_fertilizacao, m.inseminacoes_realizadas, "
                       + "f.prenhez, f.crias_anteriores, "
                       + "CASE WHEN m.gado_id IS NOT NULL THEN 'MACHO' ELSE 'FEMEA' END AS tipo_gado "
                       + "FROM gado g "
                       + "LEFT JOIN macho m ON g.id = m.gado_id "
                       + "LEFT JOIN femea f ON g.id = f.gado_id "
                       + "WHERE g.id = ?";
            this.cmd = this.con.prepareStatement(sql);
            this.cmd.setInt(1, id);
            ResultSet rs = this.cmd.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo_gado");
                Gado g;

                if ("MACHO".equals(tipo)) {
                    Macho m = new Macho();
                    m.setTaxaSucessoFertilizacao(rs.getDouble("taxa_sucesso_fertilizacao"));
                    m.setInseminacoesRealizadas(rs.getInt("inseminacoes_realizadas"));
                    g = m;
                } else {
                    Femea f = new Femea();
                    f.setPrenhez(PRENHEZ.valueOf(rs.getString("prenhez")));
                    f.setCriasAnteriores(rs.getInt("crias_anteriores"));
                    g = f;
                }

                g.setId(rs.getInt("id"));
                g.setNome(rs.getString("nome"));
                g.setIdade(rs.getInt("idade"));
                g.setRaca(RACA.valueOf(rs.getString("raca")));
                g.setPeso(rs.getDouble("peso"));
                g.setNomePai(rs.getString("nome_pai"));
                g.setNomeMae(rs.getString("nome_mae"));
                g.setObservacoes(rs.getString("observacoes"));

                return g;
            }
            return null;
        } catch (SQLException e) {
            return null;
        } finally {
            if (this.cmd != null) { try { this.cmd.close(); } catch (SQLException e) {} }
        }
    }
}