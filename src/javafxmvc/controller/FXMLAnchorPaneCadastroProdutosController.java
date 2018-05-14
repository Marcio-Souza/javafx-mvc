/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Produto;


/**
 * FXML Controller class
 *
 * @author Escritorio
 */
public class FXMLAnchorPaneCadastroProdutosController implements Initializable {

    @FXML
    private TableView<Produto> tblViewProdutos;
    @FXML   
    private TableColumn<Produto, String> tblColCodigo;
    @FXML
    private TableColumn<Produto, String> tblColNome;
    @FXML
    private Label lblProdCodigo;
    @FXML
    private Label lblProNome;
    @FXML
    private Label lblProdPreco;
    @FXML
    private Label lblProQuantidade;
    @FXML
    private Label lblProCategoria;
    @FXML
    private Button btnProInserir;
    @FXML
    private Button btnProAlterar;
    @FXML
    private Button btnProExcluir;
    
    private List<Produto> listProdutos;
    private ObservableList<Produto> obsListProdutos;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        produtoDAO.setConnection(connection);
        carregaTabela();
        tblViewProdutos.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> selecionarItemTblViewProduto(newValue));
    }    

    private void carregaTabela() {
        tblColCodigo.setCellValueFactory(new PropertyValueFactory("cdProduto"));
        tblColNome.setCellValueFactory(new PropertyValueFactory("nome") );
        
        listProdutos = produtoDAO.listar();
        obsListProdutos = FXCollections.observableArrayList(listProdutos);
        tblViewProdutos.setItems(obsListProdutos);
    }

    private void selecionarItemTblViewProduto(Produto produto) {
        lblProdCodigo.setText(String.valueOf(produto.getCdProduto()));
        lblProNome.setText(produto.getNome());
        lblProdPreco.setText(String.valueOf(produto.getPreco()));
        lblProQuantidade.setText(String.valueOf(produto.getQuantidade()));
        lblProCategoria.setText(produto.getCategoria().toString());
    }
    
}
