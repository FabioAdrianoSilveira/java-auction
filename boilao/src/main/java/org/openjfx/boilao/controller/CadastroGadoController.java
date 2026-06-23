package org.openjfx.boilao.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.SQLException;
import javafx.stage.Stage;
import org.openjfx.boilao.controller.model.GadoModel;
import org.openjfx.boilao.model.enums.PRENHEZ;
import org.openjfx.boilao.model.enums.RACA;

public class CadastroGadoController {

    @FXML private TextField txtNome;
    @FXML private TextField txtIdade;
    @FXML private TextField txtPeso;
    @FXML private ComboBox<RACA> cbRaca;
    @FXML private ComboBox<String> cbTipoGado;
    @FXML private TextField txtNomePai;
    @FXML private TextField txtNomeMae;
    @FXML private TextArea txtObservacoes;
    
    // Elementos dinâmicos do Macho
    @FXML private VBox boxMacho;
    @FXML private TextField txtTaxaFertilizacao;
    @FXML private TextField txtInseminacoes;

    // Elementos dinâmicos da Fêmea
    @FXML private VBox boxFemea;
    @FXML private ComboBox<PRENHEZ> cbPrenhez;
    @FXML private TextField txtCriasAnteriores;
    
    @FXML private Label lblMensagem;

    @FXML
    public void initialize() {
        // 1. Popula as listas de Enums nos ComboBoxes
        cbRaca.setItems(FXCollections.observableArrayList(RACA.values()));
        cbPrenhez.setItems(FXCollections.observableArrayList(PRENHEZ.values()));

        // 2. Vincula visibilidade e gerenciamento do espaço físico do layout
        boxMacho.visibleProperty().bind(cbTipoGado.valueProperty().isEqualTo("MACHO"));
        boxMacho.managedProperty().bind(boxMacho.visibleProperty());

        boxFemea.visibleProperty().bind(cbTipoGado.valueProperty().isEqualTo("FEMEA"));
        boxFemea.managedProperty().bind(boxFemea.visibleProperty());

        // REMOVA OU COMENTE ESTAS DUAS LINHAS ABAIXO:
        // boxMacho.setVisible(false);
        // boxFemea.setVisible(false);
    }

    @FXML
    private void handleSalvar() {
        if (!validarCamposGerais()) return;

        String tipoStr = cbTipoGado.getValue();
        GadoModel model = new GadoModel();
        
        try {
            // Mapeando dados em comum
            model.setNome(txtNome.getText().trim());
            model.setIdade(Integer.parseInt(txtIdade.getText().trim()));
            model.setPeso(Double.parseDouble(txtPeso.getText().trim()));
            model.setRaca(cbRaca.getValue());
            model.setNomePai(txtNomePai.getText().trim());
            model.setNomeMae(txtNomeMae.getText().trim());
            model.setObservacoes(txtObservacoes.getText().trim());

            String resultado = "";
            
            if ("MACHO".equals(tipoStr)) {
                if (txtTaxaFertilizacao.getText().isEmpty() || txtInseminacoes.getText().isEmpty()) {
                    mostrarMensagem("Preencha as especificações do macho.", true);
                    return;
                }
                model.setTaxaSucessoFertilizacao(Double.parseDouble(txtTaxaFertilizacao.getText().trim()));
                model.setInseminacoesRealizadas(Integer.parseInt(txtInseminacoes.getText().trim()));
                
                // Instancia o controller estrutural que você forneceu
                GadoController gadoCtrl = new GadoController(model, "MACHO");
                resultado = gadoCtrl.inserirMacho();
                
            } else { // FEMEA
                if (cbPrenhez.getValue() == null || txtCriasAnteriores.getText().isEmpty()) {
                    mostrarMensagem("Preencha as especificações da fêmea.", true);
                    return;
                }
                model.setPrenhez(cbPrenhez.getValue());
                model.setCriasAnteriores(Integer.parseInt(txtCriasAnteriores.getText().trim()));
                
                GadoController gadoCtrl = new GadoController(model, "FEMEA");
                resultado = gadoCtrl.inserirFemea();
            }

            if (resultado.contains("sucesso")) {
                mostrarMensagem(resultado, false);
                limparFormulario();
            } else {
                mostrarMensagem(resultado, true);
            }

        } catch (NumberFormatException e) {
            mostrarMensagem("Erro: Idade, Peso, Taxas e Crias devem conter apenas números válidos.", true);
        } catch (SQLException e) {
            mostrarMensagem("Erro ao conectar com o banco de dados.", true);
            e.printStackTrace();
        }
    }

    @FXML
private void handleCancelar() {
    limparFormulario();
    lblMensagem.setText("");
    
    // Captura a janela atual (pop-up) e fecha
    Stage stage = (Stage) txtNome.getScene().getWindow();
    stage.close();
}

    private boolean validarCamposGerais() {
        if (txtNome.getText().trim().isEmpty() || txtIdade.getText().trim().isEmpty() || 
            txtPeso.getText().trim().isEmpty() || cbRaca.getValue() == null || cbTipoGado.getValue() == null) {
            mostrarMensagem("Por favor, preencha todos os campos obrigatórios (*).", true);
            return false;
        }
        return true;
    }

    private void mostrarMensagem(String msg, boolean isErro) {
        lblMensagem.setText(msg);
        if (isErro) {
            lblMensagem.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
        } else {
            lblMensagem.setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;");
        }
    }

    private void limparFormulario() {
        txtNome.clear();
        txtIdade.clear();
        txtPeso.clear();
        cbRaca.setValue(null);
        cbTipoGado.setValue(null);
        txtNomePai.clear();
        txtNomeMae.clear();
        txtObservacoes.clear();
        txtTaxaFertilizacao.clear();
        txtInseminacoes.clear();
        cbPrenhez.setValue(null);
        txtCriasAnteriores.clear();
    }
}