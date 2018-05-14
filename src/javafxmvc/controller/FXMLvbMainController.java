/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Escritorio
 */
public class FXMLvbMainController implements Initializable {

    @FXML
    private MenuItem mniCadastrosClientes;
    @FXML
    private MenuItem mniCadastrosProdutos;
    @FXML
    private MenuItem mniCadastrosCategorias;
    @FXML
    private MenuItem mniProcessosVendas;
    @FXML
    private MenuItem mniGraficosVendasMes;
    @FXML
    private MenuItem mniRelatoriosEstoque;
    @FXML
    private AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void handleMniCadastroClientes() throws IOException{
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastroClientes.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    public void handleMniCadastroProdutos() throws IOException{
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastroProdutos.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    public void handleMniCadatroCaterorias() throws IOException{
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneCadastroCategorias.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    public void handleMniProcessoVendas() throws IOException{
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/javafxmvc/view/FXMLAnchorPaneProcessoVendas.fxml"));
        anchorPane.getChildren().setAll(a);
    }
}
