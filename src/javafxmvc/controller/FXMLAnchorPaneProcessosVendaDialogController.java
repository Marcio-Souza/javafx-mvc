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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.dao.VendaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Produto;
import javafxmvc.model.domain.Venda;

/**
 * FXML Controller class
 *
 * @author Escritorio
 */
public class FXMLAnchorPaneProcessosVendaDialogController implements Initializable {

    /**
     * @return the venda
     */
    public Venda getVenda() {
        return venda;
    }

    /**
     * @param venda the venda to set
     */
    public void setVenda(Venda venda) {
        this.venda = venda;
    }

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
     * @return the btnConfirmarClicked
     */
    public boolean isBtnConfirmarClicked() {
        return btnConfirmarClicked;
    }

    /**
     * @param btnConfirmarClicked the btnConfirmarClicked to set
     */
    public void setBtnConfirmarClicked(boolean btnConfirmarClicked) {
        this.btnConfirmarClicked = btnConfirmarClicked;
    }

    @FXML
    private ComboBox cboVendaCliente;
    @FXML
    private DatePicker dtVenda;
    @FXML
    private CheckBox cbxVendaPago; 
    @FXML
    private ComboBox cboVendaProduto;
    @FXML
    private TextField tfVendaQuantidade;
    @FXML
    private TextField tfVendaValor;
    @FXML
    private Button btnVendaAdiconar;
    @FXML
    private TableView<ItemDeVenda> tbvVendaItens;
    @FXML
    private Button btnVendaConfirmar;
    @FXML
    private Button btnVendaCancelar;
    @FXML
    private TableColumn<ItemDeVenda, Produto> tbvColProduto;
    @FXML
    private TableColumn<ItemDeVenda, Integer> tbvColQuantidade;
    @FXML
    private TableColumn<ItemDeVenda, Double> tbvColValor;
    
    private List<Cliente> listaCliente;
    private List<Produto> listaProduto;
   // private List<ItemDeVenda> listaItemDeVenda;
    
    private ObservableList<Cliente> obsListCliente;
    private ObservableList<Produto> obsListProduto;
    private ObservableList<ItemDeVenda> obsListItemDeVenda;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final VendaDAO vendaDAO = new VendaDAO();
   
    private Stage dialogStage;
    private boolean btnConfirmarClicked = false;
    private Venda venda;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        produtoDAO.setConnection(connection);
        carregaComboCliente();
        carregaComboProduto();
        tbvColProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        tbvColQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tbvColValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }    
    
    public void carregaComboCliente(){
        listaCliente = clienteDAO.listar();
        obsListCliente = FXCollections.observableArrayList(listaCliente);
        cboVendaCliente.setItems(obsListCliente);
    }
    public void carregaComboProduto(){
        listaProduto = produtoDAO.listar();
        obsListProduto = FXCollections.observableArrayList(listaProduto);
        cboVendaProduto.setItems(obsListProduto);
    }
    
    @FXML
    public void handleButtonAdicionar(){
        Produto produto;
        ItemDeVenda itemDeVenda = new ItemDeVenda();
        if(cboVendaProduto.getSelectionModel().getSelectedItem() != null ){
            produto = (Produto)cboVendaProduto.getSelectionModel().getSelectedItem();
            if(produto.getQuantidade() >= Integer.parseInt(tfVendaQuantidade.getText())){
                itemDeVenda.setProduto((Produto)cboVendaProduto.getSelectionModel().getSelectedItem());
                itemDeVenda.setQuantidade(Integer.parseInt(tfVendaQuantidade.getText()));
                itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());
                venda.getItensDeVenda().add(itemDeVenda);
                venda.setValor(venda.getValor() + itemDeVenda.getValor());
                obsListItemDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
                tbvVendaItens.setItems(obsListItemDeVenda);
                tfVendaValor.setText(String.format("%.2f", venda.getValor()));
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Problemas na escolha de produtos");
                alert.setContentText("Não existe a quantidade de produtos em estoque!");
                alert.show();
            }
        }
    }
    @FXML
    public void handleButtonConfirmar(){
        if(validarEntradaDeDados()){
            venda.setCliente((Cliente) cboVendaCliente.getSelectionModel().getSelectedItem());
            venda.setPago(cbxVendaPago.isSelected());
            venda.setData(dtVenda.getValue());
            btnConfirmarClicked = true;
            dialogStage.close();
        }
    }
    @FXML
    public void handleButtonCancelar(){
        getDialogStage().close();
    }
    private boolean validarEntradaDeDados(){
        String errorMessage = "";
        if (cboVendaCliente.getSelectionModel().getSelectedItem() == null){
            errorMessage += "Cliente inválido!\n";
        }
        if (dtVenda.getValue() == null){
            errorMessage += "Data inválida!\n";
        }
        if (obsListItemDeVenda == null){
            errorMessage += "Itens de venda inválidos!\n";
        }
        if (errorMessage.length() == 0){
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro do Pedido!");
            alert.setHeaderText("Campos Inválidos");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
