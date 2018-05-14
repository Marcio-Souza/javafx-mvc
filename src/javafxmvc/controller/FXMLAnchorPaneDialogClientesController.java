/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.domain.Cliente;

/**
 * FXML Controller class
 *
 * @author Escritorio
 */
public class FXMLAnchorPaneDialogClientesController implements Initializable {

    /**
     * @return the dialogStage
     */
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmaClicked
     */
    public boolean isButtonConfirmaClicked() {
        return buttonConfirmaClicked;
    }

    /**
     * @param buttonConfirmaClicked the buttonConfirmaClicked to set
     */
    public void setButtonConfirmaClicked(boolean buttonConfirmaClicked) {
        this.buttonConfirmaClicked = buttonConfirmaClicked;
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.txtCliNome.setText(cliente.getNome());
        this.txtCliCPF.setText(cliente.getCpf());
        this.txtCliTelefone.setText(cliente.getTelefone());
    }

    @FXML
    private TextField txtCliNome;
    @FXML
    private TextField txtCliCPF;
    @FXML
    private TextField txtCliTelefone;
    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;

    private Stage dialogStage;
    private boolean buttonConfirmaClicked = false;
    private Cliente cliente;

    @FXML
    public void handleButtonConfirmar() {
        if (validaEntradaDeDados()) {
            cliente.setNome(txtCliNome.getText());
            cliente.setCpf(txtCliCPF.getText());
            cliente.setTelefone(txtCliTelefone.getText());

            buttonConfirmaClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar() {
        dialogStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    boolean validaEntradaDeDados() {
        String errorMessage = "";

        if (txtCliNome.getText() == null || txtCliNome.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (txtCliCPF.getText() == null || txtCliCPF.getText().length() == 0) {
            errorMessage += "CPF inválido!\n";
        }
        if (txtCliTelefone.getText() == null || txtCliTelefone.getText().length() == 0) {
            errorMessage += "Telefone inválido!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor corrija.");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
