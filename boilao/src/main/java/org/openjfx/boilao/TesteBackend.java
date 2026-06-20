package org.openjfx.boilao;

import org.openjfx.boilao.controller.UsuarioController;
import org.openjfx.boilao.model.UsuarioModel;
import org.openjfx.boilao.model.enums.TIPO;
import org.openjfx.boilao.util.SessaoUsuario;

public class TesteBackend {

    public static void main(String[] args) {
        UsuarioController controller = new UsuarioController();

        System.out.println("=== INICIANDO TESTES DO BACKEND ===");

        // 1. Teste de Inserção (Administrador e Licitante)
        System.out.println("\n--- Teste 1: Cadastro ---");
        //UsuarioModel admin = new UsuarioModel("11111111111111", "Fazenda Admin", "senha123", TIPO.ADMINISTRADOR);
        UsuarioModel licitante = new UsuarioModel("22222222222222", "Fazenda Boi Gordo", "senha456", TIPO.LICITANTE);
        
        //System.out.println("Admin: " + controller.inserirUsuario(admin));
        System.out.println("Licitante: " + controller.inserirUsuario(licitante));

        // 2. Teste de Login com Fazenda (Falha)
        System.out.println("\n--- Teste 2: Login Incorreto ---");
        UsuarioModel loginErrado = UsuarioModel.criarComFazendaESenha("Fazenda Boi Gordo", "senhaErrada");
        System.out.println("Resultado: " + controller.realizarLoginFazenda(loginErrado)); // Esperado: Login incorreto...

        // 3. Teste de Login com Fazenda (Sucesso)
        System.out.println("\n--- Teste 3: Login Correto (Licitante) ---");
        UsuarioModel loginCorreto = UsuarioModel.criarComFazendaESenha("Fazenda Boi Gordo", "senha456");
        String rota = controller.realizarLoginFazenda(loginCorreto);
        System.out.println("Rota retornada: " + rota); // Esperado: HOME_LICITANTE
        System.out.println("Sessão Global salva (CNPJ): " + SessaoUsuario.getCnpjLogado());

        // 4. Teste de Alteração de Senha (usando a sessão do passo anterior)
        System.out.println("\n--- Teste 4: Alterar Própria Senha ---");
        System.out.println(controller.alterarSenha("novaSenha789")); // Esperado: Senha alterada com sucesso

        // 5. Teste de Deleção (Regra: Admin não pode ser deletado)
        System.out.println("\n--- Teste 5: Deletar Administrador ---");
        System.out.println(controller.deletarUsuario("11111111111111")); // Esperado: Erro: Administradores não podem ser deletados.

        // 6. Limpeza (Deletando o licitante para não sujar o banco)
        System.out.println("\n--- Teste 6: Deletar Licitante ---");
        System.out.println(controller.deletarUsuario("22222222222222")); // Esperado: Usuário deletado com sucesso. (se não houver lances)
    }
}