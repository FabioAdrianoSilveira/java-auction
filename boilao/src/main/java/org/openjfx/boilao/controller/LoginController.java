package org.openjfx.boilao.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.boilao.controller.model.UsuarioModel;
import org.openjfx.boilao.model.SessaoUsuario;

public class LoginController {

    @FXML private TextField txtCnpj;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblMensagem;

    private boolean isUpdating = false;

    @FXML
    public void initialize() {
        // Aplica a máscara do CNPJ logo que a tela carrega
        aplicarMascaraCnpj();
    }

    private void aplicarMascaraCnpj() {
        txtCnpj.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdating || newValue == null) return;
            isUpdating = true;
            
            // Remove tudo que não for número
            String apenasNumeros = newValue.replaceAll("[^0-9]", "");
            
            // Limita a 14 dígitos (tamanho do CNPJ)
            if (apenasNumeros.length() > 14) {
                apenasNumeros = apenasNumeros.substring(0, 14);
            }
            
            // Constrói a máscara: 00.000.000/0000-00
            StringBuilder formatado = new StringBuilder();
            for (int i = 0; i < apenasNumeros.length(); i++) {
                if (i == 2 || i == 5) formatado.append(".");
                else if (i == 8) formatado.append("/");
                else if (i == 12) formatado.append("-");
                formatado.append(apenasNumeros.charAt(i));
            }
            
            txtCnpj.setText(formatado.toString());
            txtCnpj.positionCaret(formatado.length()); // Mantém o cursor no final
            
            isUpdating = false;
        });
    }

    @FXML
private void handleLogin() {
    String cnpjMascarado = txtCnpj.getText();
    String senha = txtSenha.getText();

    if (cnpjMascarado == null || cnpjMascarado.isEmpty() || senha == null || senha.isEmpty()) {
        mostrarErro("Por favor, preencha todos os campos.");
        return;
    }

    String cnpjLimpo = cnpjMascarado.replaceAll("[^0-9]", "");

    try {
        UsuarioModel m = new UsuarioModel(cnpjLimpo, senha);
        UsuarioController usuarioCtrl = new UsuarioController(m);
        
        // Executa o login alterado
        org.openjfx.boilao.model.Usuario usuarioLogado = usuarioCtrl.getDao().getUser(); // dependendo de como estruturou
        
        if (usuarioCtrl.getDao().login()) { // Se o login der certo
            
            // 1. Salva tudo na Sessão Global
            SessaoUsuario.setCnpjLogado(cnpjLimpo);
            SessaoUsuario.setTipoLogado(usuarioLogado.getTipo()); 
            
            // 2. Decide para onde ir baseado no Tipo do usuário
            switch (SessaoUsuario.getTipoLogado()) {
                case ADMINISTRADOR:
                    // O nome correto do arquivo é painel_admin.fxml
                    redirecionarParaTela("/org/openjfx/boilao/view/painel_admin.fxml");
                    break;
                case LICITANTE:
                    // O nome correto do arquivo é home_licitante.fxml
                    redirecionarParaTela("/org/openjfx/boilao/view/home_licitante.fxml");
                    break;
            }
            
        } else {
            mostrarErro("CNPJ ou Senha inválidos.");
        }
        
    } catch (SQLException e) {
        mostrarErro("Erro de conexão com o banco de dados.");
        e.printStackTrace();
    }
}

// Método auxiliar para trocar de tela no JavaFX
private void redirecionarParaTela(String fxmlPath) {
    try {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(fxmlPath));
        javafx.scene.Parent root = loader.load();
        
        // Obtém a janela (Stage) atual através de qualquer componente da tela (ex: txtCnpj)
        javafx.scene.Scene currentScene = txtCnpj.getScene();
        currentScene.setRoot(root);
        
    } catch (java.io.IOException e) {
        mostrarErro("Erro ao carregar a próxima tela.");
        e.printStackTrace();
    }
}

    @FXML
private void irParaCadastro(ActionEvent event) {
    System.out.println("Navegando para a tela de Cadastro...");
    trocarTela(event, "/org/openjfx/boilao/view/cadastro.fxml");
}

    private void mostrarErro(String msg) {
        lblMensagem.setText(msg);
        lblMensagem.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;"); // Vermelho bonito
    }
    
    private void trocarTela(ActionEvent event, String caminhoFxml) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFxml));
        Parent root = loader.load();
        
        // Pega a janela atual através do botão que foi clicado
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        System.err.println("Erro ao carregar a tela: " + caminhoFxml);
        e.printStackTrace();
    }
    }
}