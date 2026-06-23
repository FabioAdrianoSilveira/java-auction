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
import org.openjfx.boilao.model.enums.TIPO;

public class CadastroController {

    @FXML private TextField txtCnpj;
    @FXML private TextField txtRazao;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblMensagem;

    private boolean isUpdating = false;

    @FXML
    public void initialize() {
        aplicarMascaraCnpj();
    }

    private void aplicarMascaraCnpj() {
        txtCnpj.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isUpdating || newValue == null) return;
            isUpdating = true;
            String apenasNumeros = newValue.replaceAll("[^0-9]", "");
            if (apenasNumeros.length() > 14) apenasNumeros = apenasNumeros.substring(0, 14);
            StringBuilder formatado = new StringBuilder();
            for (int i = 0; i < apenasNumeros.length(); i++) {
                if (i == 2 || i == 5) formatado.append(".");
                else if (i == 8) formatado.append("/");
                else if (i == 12) formatado.append("-");
                formatado.append(apenasNumeros.charAt(i));
            }
            txtCnpj.setText(formatado.toString());
            txtCnpj.positionCaret(formatado.length());
            isUpdating = false;
        });
    }

    @FXML
    private void handleCadastrar() {
        String cnpj = txtCnpj.getText().replaceAll("[^0-9]", "");
        String razao = txtRazao.getText();
        String senha = txtSenha.getText();

        if (cnpj.isEmpty() || razao.isEmpty() || senha.isEmpty()) {
            mostrarErro("Todos os campos são obrigatórios.");
            return;
        }

        try {
            UsuarioModel m = new UsuarioModel(cnpj, razao, TIPO.LICITANTE); // Assumindo LICITANTE por padrão
            m.setSenha(senha); // Como seu construtor Model tem 2 opções, usei os setters ou recrie a lógica

            UsuarioController ctrl = new UsuarioController(m);
            String resultado = ctrl.inserirUsuario();

            if (resultado.equals("Usuário cadastrado com sucesso!")) {
                lblMensagem.setText("Cadastro realizado! Você já pode fazer login.");
                lblMensagem.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                txtCnpj.clear(); txtRazao.clear(); txtSenha.clear();
            } else {
                mostrarErro(resultado);
            }
        } catch (SQLException e) {
            mostrarErro("Erro ao registrar no banco de dados.");
        }
    }

    @FXML
private void voltarParaLogin(ActionEvent event) {
    System.out.println("Voltando para a tela de Login...");
    trocarTela(event, "/org/openjfx/boilao/view/login.fxml");
}

    private void mostrarErro(String msg) {
        lblMensagem.setText(msg);
        lblMensagem.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
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