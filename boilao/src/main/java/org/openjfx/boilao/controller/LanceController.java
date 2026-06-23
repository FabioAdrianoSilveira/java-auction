package org.openjfx.boilao.controller;

import java.sql.SQLException;
import org.openjfx.boilao.controller.model.LanceModel;
import org.openjfx.boilao.model.Lance;
import org.openjfx.boilao.model.Leilao;
import org.openjfx.boilao.model.Usuario;
import org.openjfx.boilao.model.dao.LanceDAO;

public class LanceController {

    private LanceDAO dao;

    public LanceController() {
    }

    public LanceController(LanceModel m) throws SQLException {
        Leilao leilaoRef = new Leilao();
        leilaoRef.setId(m.getIdLeilao());

        Usuario licitanteRef = new Usuario();
        licitanteRef.setCnpj(m.getCnpjLicitante());

        this.dao = new LanceDAO(new Lance(m.getValor(), leilaoRef, licitanteRef));
    }

    public String inserirLance() {
        return (this.dao.inserir()) ? "Lance cadastrado com sucesso!" : "Erro ao registrar lance";
    }
}
