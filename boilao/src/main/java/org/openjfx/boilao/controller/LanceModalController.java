package org.openjfx.boilao.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;
import org.openjfx.boilao.controller.model.LanceModel;
import org.openjfx.boilao.model.Leilao;
import org.openjfx.boilao.model.SessaoUsuario;

public class LanceModalController {

    @FXML private Label lblIdLeilao;
    @FXML private Label lblNomeGado;
    @FXML private Label lblLanceMinimo;
    @FXML private Label lblMaiorLance;
    @FXML private TextField txtValorLance;
    @FXML private Label lblMensagem;

    private Leilao leilaoAtual;
    private boolean lanceEfetuadoComSucesso = false;

    @FXML
    public void initialize() {
        // Inicialização padrão limpa
    }

    /**
     * Método público usado pela HomeLicitanteController para passar o leilão selecionado
     * e povoar as labels informativas do modal.
     */
    public void setLeilao(Leilao leilao) {
        this.leilaoAtual = leilao;
        
        lblIdLeilao.setText(String.valueOf(leilao.getId()));
        lblNomeGado.setText(leilao.getGado().getNome());
        lblLanceMinimo.setText(String.format("R$ %.2f", leilao.getLanceMinimo()));
        lblMaiorLance.setText(String.format("R$ %.2f", leilao.getMaiorLance().getValor()));
        
        // Sugere automaticamente no campo um valor ligeiramente maior para facilitar a usabilidade
        double valorSugerido = Math.max(leilao.getLanceMinimo(), leilao.getMaiorLance().getValor()) + 100.0;
        txtValorLance.setText(String.format("%.2f", valorSugerido).replace(",", "."));
    }

    @FXML
    private void handleConfirmarLance() {
        String valorStr = txtValorLance.getText().trim();

        if (valorStr.isEmpty()) {
            mostrarMensagem("Por favor, digite o valor do seu lance.", true);
            return;
        }

        try {
            double valorDigitado = Double.parseDouble(valorStr);
            double maiorLanceAtual = leilaoAtual.getMaiorLance().getValor();
            double lanceMinimoExigido = leilaoAtual.getLanceMinimo();

            // Regra de Negócio 1: O lance deve ser maior ou igual ao lance mínimo do leilão
            if (valorDigitado < lanceMinimoExigido) {
                mostrarMensagem("Erro: O lance não pode ser inferior ao lance mínimo de R$ " + lanceMinimoExigido, true);
                return;
            }

            // Regra de Negócio 2: O lance deve superar o maior lance atual existente
            if (valorDigitado <= maiorLanceAtual) {
                mostrarMensagem("Erro: Sua proposta deve ser maior que o lance atual de R$ " + maiorLanceAtual, true);
                return;
            }

            // Captura o CNPJ de quem está logado no sistema através da Sessão Estática
            String cnpjLicitante = SessaoUsuario.getCnpjLogado();

            // Instancia o modelo auxiliar integrado ao seu backend estrutural
            LanceModel model = new LanceModel(valorDigitado, leilaoAtual.getId(), cnpjLicitante);
            LanceController lanceCtrl = new LanceController(model);
            
            String resultado = lanceCtrl.inserirLance();

            if (resultado.contains("sucesso")) {
                this.lanceEfetuadoComSucesso = true;
                fecharJanela(); // Fecha o modal imediatamente em caso de sucesso
            } else {
                mostrarMensagem(resultado, true);
            }

        } catch (NumberFormatException e) {
            mostrarMensagem("Erro: Digite um valor numérico válido (use ponto para centavos).", true);
        } catch (SQLException e) {
            mostrarMensagem("Erro de comunicação com o banco de dados.", true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) txtValorLance.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensagem(String msg, boolean isErro) {
        lblMensagem.setText(msg);
        if (isErro) {
            lblMensagem.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
        } else {
            lblMensagem.setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;");
        }
    }

    public boolean isLanceEfetuadoComSucesso() {
        return lanceEfetuadoComSucesso;
    }
}