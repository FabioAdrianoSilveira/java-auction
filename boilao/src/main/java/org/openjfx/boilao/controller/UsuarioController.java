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

    // Mude o retorno de String para Usuario (da sua model original) ou retorne null se falhar
    public Usuario efetuarLogin() {
    if (this.dao.login()) {
        // Retorna o objeto usuario que agora contém o TIPO vindo do banco
        return this.dao.getUser(); // Certifique-se de ter um getter para 'user' na sua UsuarioDAO
    }
    return null;
}

    public String inserirUsuario() {
        return (this.dao.inserir()) ? "Usuário cadastrado com sucesso!" : "Erro ao cadastrar usuário";
    }

    public String excluirUsuario() {
    try {
        // 1. Regra de Negócio: Administradores não podem ser deletados
        if (this.dao.getUser().getTipo() == org.openjfx.boilao.model.enums.TIPO.ADMINISTRADOR) {
            return "Erro: Usuários do tipo ADMINISTRADOR não podem ser removidos do sistema.";
        }

        // 2. Regra de Negócio: Licitantes com lances não podem ser deletados
        if (verificarSeLicitantePossuiLances(this.dao.getUser().getCnpj())) {
            return "Erro: Este licitante possui lances atrelados ao seu nome e não pode ser deletado.";
        }

        // Se passar por todas as regras, prossegue com a exclusão física no banco
        return (this.dao.excluir()) ? "Usuário excluído com sucesso!" : "Erro ao deletar usuário no banco de dados.";
        
    } catch (SQLException e) {
        return "Erro de comunicação com o banco de dados ao tentar validar exclusão.";
    }
}

    // Método auxiliar corrigido para não matar a conexão Singleton
    private boolean verificarSeLicitantePossuiLances(String cnpj) throws SQLException {
        // CORRIGIDO PARA licitante_cnpj
        String sql = "SELECT COUNT(*) FROM lance WHERE licitante_cnpj = ?"; 
        
        java.sql.Connection con = org.openjfx.boilao.model.DatabaseConnection.getInstance().getConnection();
        
        try (java.sql.PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, cnpj);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public String atualizarSenha() {
        return (this.dao.atualizarSenha()) ? "Senha alterada com sucesso!" : "Erro ao mudar senha";
    }

    public List<Usuario> listarLicitantes() throws SQLException {
        UsuarioDAO listagemDao = new UsuarioDAO(new Usuario());
        return listagemDao.listarLicitantes();
    }

    public UsuarioDAO getDao() {
        return dao;
    }
}
