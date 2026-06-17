package org.openjfx.boilao;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.openjfx.boilao.model.Usuario;
import org.openjfx.boilao.model.UsuarioDAO;

public class AdminUsuariosController {

    @FXML
    private TextField txtCnpj;

    @FXML
    private TextField txtFazenda;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private ComboBox<String> cbTipo;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void initialize() {
        cbTipo.getItems().addAll("administrador", "licitante");
    }

    @FXML
    private void cadastrarUsuario() {
        String cnpj = txtCnpj.getText();
        String fazenda = txtFazenda.getText();
        String senha = txtSenha.getText();
        String tipo = cbTipo.getValue();

        if (cnpj.isEmpty() || fazenda.isEmpty() || senha.isEmpty() || tipo == null) {
            mostrarAlerta("Erro", "Preencha todos os campos.");
            return;
        }

        Usuario usuario = new Usuario(cnpj, fazenda, senha, tipo);

        try {
            usuarioDAO.cadastrarUsuario(usuario);
            mostrarAlerta("Sucesso", "Usuário cadastrado com sucesso!");
            limparCampos();
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    @FXML
    private void deletarUsuario() {
        mostrarAlerta("Aviso", "A função de deletar será feita na próxima parte.");
    }

    private void limparCampos() {
        txtCnpj.clear();
        txtFazenda.clear();
        txtSenha.clear();
        cbTipo.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}