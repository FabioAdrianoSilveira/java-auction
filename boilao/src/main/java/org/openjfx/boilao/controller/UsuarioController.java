package org.openjfx.boilao.controller;

import org.openjfx.boilao.dao.UsuarioDAO;
import org.openjfx.boilao.model.Usuario;
import org.openjfx.boilao.model.UsuarioModel;
import org.openjfx.boilao.model.enums.TIPO;
import org.openjfx.boilao.util.SessaoUsuario; 

public class UsuarioController {
    private UsuarioDAO dao;

    public UsuarioController() {
        this.dao = new UsuarioDAO();
    }

    // Solicitação 1: Administradores cadastram usuários
    public String inserirUsuario(UsuarioModel model) {
        TIPO tipoEnum = model.getTipo().getValor().equalsIgnoreCase("ADMINISTRADOR") ? TIPO.ADMINISTRADOR : TIPO.LICITANTE;
        Usuario user = new Usuario(model.getCnpj(), model.getFazenda(), model.getSenha(), tipoEnum);
        
        if (dao.inserir(user)) {
            return "Usuário cadastrado com sucesso.";
        } else {
            return "Erro ao cadastrar usuário.";
        }
    }

    // Solicitação 1 e 2: Deletar usuário com validações
    public String deletarUsuario(String cnpjAlvo) {
        return dao.deletarUsuario(cnpjAlvo);
    }

    // Solicitação 2: Login com nome da fazenda e senha
    public String realizarLoginFazenda(UsuarioModel model) {
        Usuario loginUser = new Usuario();
        loginUser.setFazenda(model.getFazenda());
        loginUser.setSenha(model.getSenha());

        boolean sucesso = dao.selecionarDadosLoginFazenda(loginUser);

        if (sucesso) {
            SessaoUsuario.setCnpjLogado(loginUser.getCnpj()); // Salva na global
            
            if (loginUser.getTipo() == TIPO.ADMINISTRADOR) {
                return "ADMIN_PANEL"; 
            } else {
                return "HOME_LICITANTE";
            }
        } else {
            return "Login incorreto, tente novamente"; // Mensagem exigida
        }
    }

    // Solicitação 3: Alterar a própria senha
    public String alterarSenha(String novaSenha) {
        String cnpjLogado = SessaoUsuario.getCnpjLogado();
        if (cnpjLogado == null) return "Erro de sessão.";

        Usuario user = new Usuario();
        user.setCnpj(cnpjLogado);
        user.setSenha(novaSenha);

        if (dao.alterarSenha(user)) {
            return "Senha alterada com sucesso"; // Mensagem exigida
        } else {
            return "Erro ao alterar senha.";
        }
    }
}