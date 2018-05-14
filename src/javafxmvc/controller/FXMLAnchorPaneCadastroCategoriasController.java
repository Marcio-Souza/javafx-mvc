/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;

/**
 * FXML Controller class
 *
 * @author Escritorio
 */
public class FXMLAnchorPaneCadastroCategoriasController implements Initializable {

    @FXML 
    private Label lblCatCodigo;
    @FXML 
    private Label lblCatDescricao;
    @FXML
    private TableView<Categoria> tblViewCategorias;
    @FXML
    private TableColumn<Categoria, String> tblColCodigo;
    @FXML
    private TableColumn<Categoria, String> tblColDescricao;
    @FXML
    private Button btnCatInserir;
    @FXML
    private Button btnCatAlterar;
    @FXML
    private Button btnCatExcluir;

    private List<Categoria> listCategoria;
    private ObservableList<Categoria> obsListCategoria;
    
    //conexao com banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //abre a conexao
        categoriaDAO.setConnection(connection);
        //carrega tabela com os dados
        carregaTabela();
        //ouvinte para alteracoes de selecao na tabela
        tblViewCategorias.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTblViewerClientes(newValue));
    }    
    public void carregaTabela(){
        tblColCodigo.setCellValueFactory(new PropertyValueFactory<>("cdCategoria"));
        tblColDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao") );
        
        listCategoria = categoriaDAO.listar();
        
        obsListCategoria = FXCollections.observableArrayList(listCategoria);
        tblViewCategorias.setItems(obsListCategoria);
    }
    //metodo que gerencia selecao na tabela
    public void selecionarItemTblViewerClientes(Categoria categoria){
        lblCatCodigo.setText(String.valueOf(categoria.getCdCategoria()));
        lblCatDescricao.setText(categoria.getDescricao());

    }
    //eventos dos botões
    public void handleButtonInserir() throws IOException{
        Categoria categoria = new Categoria();
        if(categoria != null){
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroCategoria(categoria);
            if(buttonConfirmarClicked){
                categoriaDAO.inserir(categoria);
                carregaTabela();
            }
        }
    }
    public void handleButtonAlterar() throws IOException{
        Categoria categoria = tblViewCategorias.getSelectionModel().getSelectedItem();
        if(categoria != null){
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroCategoria(categoria);
            if(buttonConfirmarClicked){
                categoriaDAO.alterar(categoria);
                carregaTabela();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um categoria na tabela! ");
            alert.show();
        }
        
    }
    public void handleButtonExcluir() throws IOException{
        Categoria categoria = tblViewCategorias.getSelectionModel().getSelectedItem();
        if(categoria != null){
            categoriaDAO.remover(categoria);
            carregaTabela();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor escolha um cliente na tabela!");
            alert.show();
        }
    }
    //botão alterar ser verdadeiro carrega o Dialog
    public boolean showFXMLAnchorPaneCadastroCategoria(Categoria categoria) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneDialogClientesController.class.getResource("/javafxmvc/view/FXMLAnchorPaneDialogCategoria.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //cria um estágio do diálogo
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de categorias");
        dialogStage.setResizable(false);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //setando o categoria no controller
        FXMLAnchorPaneDialogCategoriaController controller = loader.getController();
        controller.setStage(dialogStage);
        controller.setCategoria(categoria);
        
        //mostra o dialogo e espera o fechamento
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmaClicked();
    }
    
    
}
