package org.openjfx.boilao.controller;

import java.io.IOException;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.boilao.controller.model.UsuarioModel;
import org.openjfx.boilao.model.Leilao;
import org.openjfx.boilao.model.SessaoUsuario;

public class HomeLicitanteController {

    @FXML private Label lblCnpjSessao;
    @FXML private TableView<Leilao> tblLeiloes;
    @FXML private TableColumn<Leilao, Integer> colId;
    @FXML private TableColumn<Leilao, String> colNomeGado;
    @FXML private TableColumn<Leilao, String> colRaca;
    @FXML private TableColumn<Leilao, Double> colPeso;
    @FXML private TableColumn<Leilao, Double> colLanceMinimo;
    @FXML private TableColumn<Leilao, Double> colMaiorLance;

    private LeilaoController leilaoController;
    private ObservableList<Leilao> obsLeiloes;

    @FXML
    public void initialize() {
        this.leilaoController = new LeilaoController();
        
        // 1. Exibe o CNPJ formatado da sessão atual
        if (SessaoUsuario.getCnpjLogado() != null) {
            lblCnpjSessao.setText("CNPJ: " + formatarCnpj(SessaoUsuario.getCnpjLogado()));
        }

        // 2. Configura como cada coluna vai extrair o dado do objeto Leilao
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        // Extrações de objetos aninhados (Leilao -> Gado -> Atributos)
        colNomeGado.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getGado().getNome()));
            
        colRaca.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getGado().getRaca().name()));
            
        colPeso.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getGado().getPeso()).asObject());
            
        colLanceMinimo.setCellValueFactory(new PropertyValueFactory<>("lanceMinimo"));
        
        // Extração do maior lance atual (Leilao -> Lance -> Valor)
        colMaiorLance.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getMaiorLance().getValor()).asObject());

        // 3. Carrega os dados na tabela
        handleAtualizarTabela();
    }

    @FXML
    private void handleAtualizarTabela() {
        try {
            List<Leilao> lista = leilaoController.listarLeilaoLances();
            if (lista != null) {
                obsLeiloes = FXCollections.observableArrayList(lista);
                tblLeiloes.setItems(obsLeiloes);
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Não foi possível carregar os leilões ativos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAbrirModalLance() {
        Leilao leilaoSelecionado = tblLeiloes.getSelectionModel().getSelectedItem();
        if (leilaoSelecionado == null) {
            mostrarAlerta("Aviso", "Selecione um leilão na tabela para dar um lance.", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Carrega o FXML do modal de lances
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/org/openjfx/boilao/view/lance_modal.fxml"));
            javafx.scene.Parent root = loader.load();

            // Obtém o controlador do modal para injetar o leilão selecionado
            LanceModalController modalCtrl = loader.getController();
            modalCtrl.setLeilao(leilaoSelecionado);

            // Cria e configura a nova janela (Stage estilo Modal/Pop-up)
            Stage modalStage = new Stage();
            modalStage.setTitle("Enviar Proposta - BOILÃO");
            modalStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            modalStage.initOwner(tblLeiloes.getScene().getWindow()); // Trava a janela de trás
            modalStage.setScene(new javafx.scene.Scene(root));

            // Abre e espera o usuário fechar a janela
            modalStage.showAndWait();

            // Se o usuário confirmou o lance com sucesso lá dentro, atualiza a tabela da Home automaticamente
            if (modalCtrl.isLanceEfetuadoComSucesso()) {
                handleAtualizarTabela();
                mostrarAlerta("Sucesso", "Seu lance foi computado com sucesso!", Alert.AlertType.INFORMATION);
            }

        } catch (java.io.IOException e) {
            mostrarAlerta("Erro", "Não foi possível abrir a janela de lances.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAlterarSenha() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Alterar Senha");
        dialog.setHeaderText("Defina sua nova senha secreta");
        dialog.setContentText("Nova Senha:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            try {
                UsuarioModel model = new UsuarioModel();
                model.setCnpj(SessaoUsuario.getCnpjLogado());
                model.setSenha(result.get().trim());
                
                UsuarioController usuarioCtrl = new UsuarioController(model);
                String msg = usuarioCtrl.atualizarSenha();
                
                mostrarAlerta("Sucesso", msg, Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                mostrarAlerta("Erro", "Erro ao conectar com o banco para atualizar senha.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
private void handleLogout(ActionEvent event) {
    SessaoUsuario.limparSessao();
    System.out.println("Logout efetuado com sucesso.");
    trocarTela(event, "/org/openjfx/boilao/view/login.fxml");
}

    private String formatarCnpj(String cnpj) {
        if (cnpj.length() != 14) return cnpj;
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }

    private void mostrarAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
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