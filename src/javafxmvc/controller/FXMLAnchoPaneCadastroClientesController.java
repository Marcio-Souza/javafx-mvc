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
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;

/**
 * FXML Controller class
 *
 * @author Escritorio
 */
public class FXMLAnchoPaneCadastroClientesController implements Initializable {

    @FXML 
    private Label lblCliCodigo;
    @FXML 
    private Label lblCliNome;
    @FXML 
    private Label lblCliCPF;
    @FXML 
    private Label lblCliTelefone;
    @FXML
    private TableView<Cliente> tblViewClientes;
    @FXML
    private TableColumn<Cliente, String> tblViewCliNome;
    @FXML
    private TableColumn<Cliente, String> tblViewCliCPF;
    @FXML
    private Button btnInserir;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnAlterar;

    private List<Cliente> listClientes;
    private ObservableList<Cliente> obsListCliente;
    
    //conexao com banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //abre a conexao
        clienteDAO.setConnection(connection);
        //carrega tabela com os dados
        carregaTabela();
        //ouvinte para alteracoes de selecao na tabela
        tblViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTblViewerClientes(newValue));
    }    
    public void carregaTabela(){
        tblViewCliNome.setCellValueFactory(new PropertyValueFactory("nome"));
        tblViewCliCPF.setCellValueFactory(new PropertyValueFactory("cpf") );
        
        listClientes = clienteDAO.listar();
        
        obsListCliente = FXCollections.observableArrayList(listClientes);
        tblViewClientes.setItems(obsListCliente);
    }
    //metodo que gerencia selecao na tabela
    public void selecionarItemTblViewerClientes(Cliente cliente){
        lblCliCodigo.setText(String.valueOf(cliente.getCdCliente()));
        lblCliNome.setText(cliente.getNome());
        lblCliCPF.setText(cliente.getCpf());
        lblCliTelefone.setText(cliente.getTelefone());
    }
    //eventos dos botões
    public void handleButtonInserir() throws IOException{
        Cliente cliente = new Cliente();
        if(cliente != null){
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroCliente(cliente);
            if(buttonConfirmarClicked){
                clienteDAO.inserir(cliente);
                carregaTabela();
            }
        }
    }
    public void handleButtonAlterar() throws IOException{
        Cliente cliente = tblViewClientes.getSelectionModel().getSelectedItem();
        if(cliente != null){
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroCliente(cliente);
            if(buttonConfirmarClicked){
                clienteDAO.alterar(cliente);
                carregaTabela();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na tabela! ");
            alert.show();
        }
        
    }
    public void handleButtonExcluir() throws IOException{
        Cliente cliente = tblViewClientes.getSelectionModel().getSelectedItem();
        if(cliente != null){
            clienteDAO.remover(cliente);
            carregaTabela();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor escolha um cliente na tabela!");
            alert.show();
        }
    }
    //botão alterar ser verdadeiro carrega o Dialog
    public boolean showFXMLAnchorPaneCadastroCliente(Cliente cliente) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneDialogClientesController.class.getResource("/javafxmvc/view/FXMLAnchorPaneDialogClientes.fxml"));
        AnchorPane page = (AnchorPane)loader.load();
        
        //cria um estágio do diálogo
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de clientes");
        dialogStage.setResizable(false);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //setando o cliente no controller
        FXMLAnchorPaneDialogClientesController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);
        
        //mostra o dialogo e espera o fechamento
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmaClicked();
    }
    
    
}
