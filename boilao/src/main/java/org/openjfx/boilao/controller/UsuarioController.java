package org.openjfx.boilao.controller;

import java.sql.SQLException;
import java.util.List;
import org.openjfx.boilao.controller.model.UsuarioModel;
import org.openjfx.boilao.model.Usuario;
import org.openjfx.boilao.model.dao.UsuarioDAO;

public class UsuarioController {

    private UsuarioDAO dao;

    public UsuarioController() {
    }

    public UsuarioController(UsuarioModel m) throws SQLException {
        // Conversão idêntica ao padrão estrutural do ClienteController
        this.dao = new UsuarioDAO(new Usuario(m.getCnpj(), m.getRazao(), m.getSenha(), m.getTipo()));
    }

    public String efetuarLogin() {
        return (this.dao.login()) ? "Login efetuado com sucesso!" : "CNPJ ou Senha inválidos";
    }

    public String inserirUsuario() {
        return (this.dao.inserir()) ? "Usuário cadastrado com sucesso!" : "Erro ao cadastrar usuário";
    }

    public String excluirUsuario() {
        return (this.dao.excluir()) ? "Usuário excluído com sucesso!" : "Erro ao deletar usuário";
    }

    public String atualizarSenha() {
        return (this.dao.atualizarSenha()) ? "Senha alterada com sucesso!" : "Erro ao mudar senha";
    }

    public List<Usuario> listarLicitantes() throws SQLException {
        UsuarioDAO listagemDao = new UsuarioDAO(new Usuario());
        return listagemDao.listarLicitantes();
    }
}
