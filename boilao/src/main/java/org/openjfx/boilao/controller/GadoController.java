package org.openjfx.boilao.controller;

import java.sql.SQLException;
import org.openjfx.boilao.controller.model.GadoModel;
import org.openjfx.boilao.model.Femea;
import org.openjfx.boilao.model.Gado;
import org.openjfx.boilao.model.Macho;
import org.openjfx.boilao.model.dao.GadoDAO;

public class GadoController {

    private GadoDAO dao;

    public GadoController() {
    }

    public GadoController(GadoModel m, String tipoMachoOuFemea) throws SQLException {
        Gado g;
        if ("MACHO".equalsIgnoreCase(tipoMachoOuFemea)) {
            Macho macho = new Macho();
            macho.setTaxaSucessoFertilizacao(m.getTaxaSucessoFertilizacao());
            macho.setInseminacoesRealizadas(m.getInseminacoesRealizadas());
            g = macho;
        } else {
            Femea femea = new Femea();
            femea.setPrenhez(m.getPrenhez());
            femea.setCriasAnteriores(m.getCriasAnteriores());
            g = femea;
        }
        g.setId(m.getId());
        g.setNome(m.getNome());
        g.setIdade(m.getIdade());
        g.setRaca(m.getRaca());
        g.setPeso(m.getPeso());
        g.setNomePai(m.getNomePai());
        g.setNomeMae(m.getNomeMae());
        g.setObservacoes(m.getObservacoes());
        this.dao = new GadoDAO(g);
    }

    public String inserirMacho() {
        return (this.dao.inserirMacho()) ? "Gado Macho cadastrado com sucesso!" : "Erro ao cadastrar gado macho";
    }

    public String inserirFemea() {
        return (this.dao.inserirFemea()) ? "Gado Fêmea cadastrado com sucesso!" : "Erro ao cadastrar gado fêmea";
    }

    public String excluirGado() {
        return (this.dao.excluir()) ? "Gado removido com sucesso!" : "Erro ao deletar gado";
    }

    public Gado listarGado(int id) throws SQLException {
        // Instanciação vazia padrão para listagem sem estado prévio no DAO
        GadoDAO buscaDao = new GadoDAO(new Macho());
        return buscaDao.listar(id);
    }
}
