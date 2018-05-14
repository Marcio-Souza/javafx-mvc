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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.domain.Categoria;

/**
 * FXML Controller class
 *
 * @author Escritorio
 */
public class FXMLAnchorPaneDialogCategoriaController implements Initializable {

    /**
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
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
     * @return the categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        this.txtCategoria.setText(categoria.getDescricao());
    }

    @FXML
    private TextField txtCategoria;
    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnCancelar;
    
    private Stage stage;
    private boolean buttonConfirmaClicked;
    private Categoria categoria;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void handleButtonConfirma(){
        categoria.setDescricao(txtCategoria.getText());
        
        buttonConfirmaClicked = true;
        stage.close();
    }
    public void handleButtonCancelar(){
        stage.close();
    }
}
