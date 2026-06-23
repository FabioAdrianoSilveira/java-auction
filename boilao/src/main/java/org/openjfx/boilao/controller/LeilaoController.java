package org.openjfx.boilao.controller;

import java.sql.SQLException;
import java.util.List;
import org.openjfx.boilao.controller.model.LeilaoModel;
import org.openjfx.boilao.model.Leilao;
import org.openjfx.boilao.model.Macho;
import org.openjfx.boilao.model.dao.LeilaoDAO;

public class LeilaoController {

    private LeilaoDAO dao;

    public LeilaoController() {
    }

    public LeilaoController(LeilaoModel m) throws SQLException {
        Leilao l = new Leilao();
        Macho gadoPlaceholder = new Macho();
        gadoPlaceholder.setId(m.getIdGado());
        l.setGado(gadoPlaceholder);
        l.setLanceMinimo(m.getLanceMinimo());
        l.setDataInicio(m.getDataInicio());
        this.dao = new LeilaoDAO(l);
    }

    public String inserirLeilao() {
        return (this.dao.inserir()) ? "Leilão cadastrado com sucesso!" : "Erro ao cadastrar leilão";
    }

    public List<Leilao> listarLeilaoGestao() throws SQLException {
        LeilaoDAO consultaDao = new LeilaoDAO(new Leilao());
        return consultaDao.listarTodosGestao();
    }

    public List<Leilao> listarLeilaoLances() throws SQLException {
        LeilaoDAO consultaDao = new LeilaoDAO(new Leilao());
        return consultaDao.listarTodosParaLance();
    }
}
