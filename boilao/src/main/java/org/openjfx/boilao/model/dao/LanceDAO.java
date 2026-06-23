package org.openjfx.boilao.model.dao;

import java.sql.*;
import org.openjfx.boilao.model.DatabaseConnection;
import org.openjfx.boilao.model.Lance;

public class LanceDAO {

    private final Connection con;
    private PreparedStatement cmd;
    private final Lance lance;

    public LanceDAO(Lance l) throws SQLException {
        this.lance = l;
        this.con = DatabaseConnection.getInstance().getConnection();
        this.con.setAutoCommit(false);
    }

    public boolean inserir() {
        try {
            String sql = "INSERT INTO lance (valor, id_leilao, cnpj_licitante, datalance) VALUES (?, ?, ?, ?)";
            this.cmd = this.con.prepareStatement(sql);
            this.cmd.setDouble(1, this.lance.getValor());
            this.cmd.setInt(2, this.lance.getLeilao().getId());
            this.cmd.setString(3, this.lance.getLicitante().getCnpj());
            this.cmd.setTimestamp(4, Timestamp.valueOf(this.lance.getDataLance()));

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
}
