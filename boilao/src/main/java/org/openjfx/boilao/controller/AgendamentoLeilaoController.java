package org.openjfx.boilao.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.openjfx.boilao.controller.model.LeilaoModel;

public class AgendamentoLeilaoController {

    @FXML private TextField txtIdGado;
    @FXML private TextField txtLanceMinimo;
    @FXML private DatePicker dpDataInicio;
    @FXML private ComboBox<String> cbHora;
    @FXML private ComboBox<String> cbMinuto;
    @FXML private Label lblMensagem;

    @FXML
    public void initialize() {
        popularSeletoresHorario();
        
        // Bloqueia datas passadas para evitar agendamentos inválidos por usabilidade
        dpDataInicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee;");
                }
            }
        });
    }

    private void popularSeletoresHorario() {
        ObservableList<String> horas = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            horas.add(String.format("%02d", i));
        }
        cbHora.setItems(horas);
        cbHora.setValue("19"); // Padrão sugerido comum para leilões

        ObservableList<String> minutos = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i += 5) { // Intervalos de 5 em 5 minutos por conveniência
            minutos.add(String.format("%02d", i));
        }
        cbMinuto.setItems(minutos);
        cbMinuto.setValue("00");
    }

    @FXML
    private void handleAgendar() {
        if (!validarCampos()) return;

        try {
            int idGado = Integer.parseInt(txtIdGado.getText().trim());
            double lanceMinimo = Double.parseDouble(txtLanceMinimo.getText().trim());
            
            // Une a data do DatePicker com o horário selecionado nos ComboBoxes
            LocalDate data = dpDataInicio.getValue();
            LocalTime tempo = LocalTime.of(
                Integer.parseInt(cbHora.getValue()), 
                Integer.parseInt(cbMinuto.getValue())
            );
            LocalDateTime dataInicio = LocalDateTime.of(data, tempo);

            // Validação de negócio: o leilão não pode iniciar no passado imediato
            if (dataInicio.isBefore(LocalDateTime.now())) {
                mostrarMensagem("A data e hora de início não podem ser anteriores ao momento atual.", true);
                return;
            }

            // Instancia o modelo auxiliar estrutural integrado ao seu código original
            LeilaoModel model = new LeilaoModel(idGado, lanceMinimo, dataInicio);
            LeilaoController leilaoCtrl = new LeilaoController(model);
            
            String resultado = leilaoCtrl.inserirLeilao();

            if (resultado.contains("sucesso")) {
                mostrarMensagem(resultado, false);
                limparFormulario();
            } else {
                mostrarMensagem(resultado, true);
            }

        } catch (NumberFormatException e) {
            mostrarMensagem("Erro: ID do gado deve ser inteiro e lance mínimo deve ser numérico.", true);
        } catch (SQLException e) {
            mostrarMensagem("Erro de comunicação com o banco de dados.", true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar() {
        limparFormulario();
        lblMensagem.setText("");
        // Fechar janela/modal ou retornar à listagem
    }

    private boolean validarCampos() {
        if (txtIdGado.getText().trim().isEmpty() || txtLanceMinimo.getText().trim().isEmpty() || 
            dpDataInicio.getValue() == null || cbHora.getValue() == null || cbMinuto.getValue() == null) {
            mostrarMensagem("Por favor, preencha todos os campos obrigatórios.", true);
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
        txtIdGado.clear();
        txtLanceMinimo.clear();
        dpDataInicio.setValue(null);
        cbHora.setValue("19");
        cbMinuto.setValue("00");
    }
}