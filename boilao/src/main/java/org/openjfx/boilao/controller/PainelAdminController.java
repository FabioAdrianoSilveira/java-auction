package org.openjfx.boilao.controller;

import java.io.IOException;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openjfx.boilao.controller.model.UsuarioModel;
import org.openjfx.boilao.model.Usuario;
import org.openjfx.boilao.model.SessaoUsuario;
import org.openjfx.boilao.model.enums.TIPO;

public class PainelAdminController {

    @FXML private Label lblAdminLogado;
    @FXML private TableView<Usuario> tblUsuarios;
    @FXML private TableColumn<Usuario, String> colCnpj;
    @FXML private TableColumn<Usuario, String> colRazao;
    @FXML private TableColumn<Usuario, String> colTipo;

    private UsuarioController usuarioController;
    private ObservableList<Usuario> obsUsuarios;

    @FXML
    public void initialize() {
        this.usuarioController = new UsuarioController();

        if (SessaoUsuario.getCnpjLogado() != null) {
            lblAdminLogado.setText("Admin CNPJ: " + formatarCnpj(SessaoUsuario.getCnpjLogado()));
        }

        // Configuração das colunas mapeando os dados do modelo Usuario
        colCnpj.setCellValueFactory(cellData -> new SimpleStringProperty(formatarCnpj(cellData.getValue().getCnpj())));
        colRazao.setCellValueFactory(new PropertyValueFactory<>("razao"));
        colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipo().name()));

        handleAtualizarTabela();
    }

    @FXML
    private void handleAtualizarTabela() {
        try {
            // Utiliza o método existente no seu UsuarioController
            List<Usuario> lista = usuarioController.listarLicitantes();
            if (lista != null) {
                obsUsuarios = FXCollections.observableArrayList(lista);
                tblUsuarios.setItems(obsUsuarios);
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Falha ao ler a lista de usuários do banco de dados.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleExcluirUsuario() {
        Usuario selecionado = tblUsuarios.getSelectionModel().getSelectedItem();
        
        if (selecionado == null) {
            mostrarAlerta("Aviso", "Selecione um usuário na tabela para excluí-lo.", Alert.AlertType.WARNING);
            return;
        }

        // Confirmação de exclusão por segurança
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Tem certeza que deseja remover o usuário '" + selecionado.getRazao() + "'?");
        
        Optional<ButtonType> resposta = confirmacao.showAndWait();
        if (resposta.isPresent() && resposta.get() == ButtonType.OK) {
            try {
                // Montamos a Model auxiliar baseada no usuário selecionado da tabela
                UsuarioModel modelAux = new UsuarioModel();
                modelAux.setCnpj(selecionado.getCnpj());
                modelAux.setRazao(selecionado.getRazao());
                modelAux.setTipo(selecionado.getTipo());

                // Instancia o controlador com o estado do usuário que deseja deletar
                UsuarioController ctrlExclusao = new UsuarioController(modelAux);
                String resultadoMsg = ctrlExclusao.excluirUsuario();

                // Exibe o retorno das validações de negócio feitas no backend
                if (resultadoMsg.contains("Erro")) {
                    mostrarAlerta("Restrição de Segurança", resultadoMsg, Alert.AlertType.ERROR);
                } else {
                    mostrarAlerta("Sucesso", resultadoMsg, Alert.AlertType.INFORMATION);
                    handleAtualizarTabela(); // Recarrega a tabela de usuários
                }

            } catch (SQLException e) {
                mostrarAlerta("Erro", "Erro ao processar exclusão no banco de dados.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
private void handleNovoUsuario(ActionEvent event) {
    System.out.println("Abrindo tela de cadastro de novo usuário...");
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/boilao/view/cadastro.fxml"));
        Parent root = loader.load();
        
        Stage modalStage = new Stage();
        modalStage.setTitle("Cadastrar Novo Usuário");
        modalStage.initModality(Modality.WINDOW_MODAL);
        
        // Pega a janela principal para travar o fundo
        Stage janelaPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modalStage.initOwner(janelaPrincipal);
        
        modalStage.setScene(new Scene(root));
        modalStage.showAndWait(); // Fica aguardando o admin fechar o cadastro
        
        // Atualiza a tabela assim que a janela de cadastro fechar
        handleAtualizarTabela();
        
    } catch (IOException e) {
        System.err.println("Erro ao abrir modal de cadastro.");
        e.printStackTrace();
    }
}

    @FXML
    private void handleAlterarSenha() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Alterar Senha do Admin");
        dialog.setHeaderText("Modificar senha da conta de administração atual");
        dialog.setContentText("Nova Senha:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            try {
                UsuarioModel model = new UsuarioModel();
                model.setCnpj(SessaoUsuario.getCnpjLogado());
                model.setSenha(result.get().trim());
                
                UsuarioController usuarioCtrl = new UsuarioController(model);
                String msg = usuarioCtrl.atualizarSenha();
                
                // Correção Visual: Verifica a palavra "sucesso" na resposta do banco
                if (msg.contains("sucesso")) {
                    mostrarAlerta("Sucesso", msg, Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Erro na Atualização", msg, Alert.AlertType.ERROR);
                }
                
            } catch (SQLException e) {
                mostrarAlerta("Erro", "Erro ao atualizar senha no banco.", Alert.AlertType.ERROR);
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
        if (cnpj == null || cnpj.length() != 14) return cnpj;
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
@FXML
    private void handleAgendarLeilao(ActionEvent event) {
        System.out.println("Abrindo tela de agendamento de leilão...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/boilao/view/agendamento_leilao.fxml"));
            Parent root = loader.load();
            
            Stage modalStage = new Stage();
            modalStage.setTitle("Agendar Leilão - BOILÃO");
            modalStage.initModality(Modality.WINDOW_MODAL);
            
            Stage janelaPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modalStage.initOwner(janelaPrincipal);
            
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait(); 
            
        } catch (IOException e) {
            System.err.println("Erro ao abrir a tela de agendamento.");
            e.printStackTrace();
        }
    }
    
@FXML
private void handleNovoGado(ActionEvent event) {
    System.out.println("Abrindo tela de cadastro de novo gado...");
    try {
        // Carrega o FXML de cadastro de gado de dentro da nova pasta view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/boilao/view/cadastro_gado.fxml"));
        Parent root = loader.load();
        
        // Cria uma nova janela (Stage) para funcionar como Pop-up
        Stage modalStage = new Stage();
        modalStage.setTitle("Cadastrar Novo Gado - BOILÃO");
        modalStage.initModality(Modality.WINDOW_MODAL);
        
        // Vincula a janela principal para travar a tela de fundo enquanto o cadastro estiver aberto
        Stage janelaPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
        modalStage.initOwner(janelaPrincipal);
        
        modalStage.setScene(new Scene(root));
        modalStage.showAndWait(); // Abre a janela e pausa a execução do painel de trás
        
    } catch (IOException e) {
        System.err.println("Erro ao abrir a tela de cadastro de gado.");
        e.printStackTrace();
    }
}
}