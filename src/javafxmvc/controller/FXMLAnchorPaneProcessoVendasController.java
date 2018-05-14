/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafxmvc.model.dao.ItemDeVendaDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.dao.VendaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.ItemDeVenda;
import javafxmvc.model.domain.Produto;
import javafxmvc.model.domain.Venda;

/**
 * FXML Controller class
 *
 * @author Escritorio
 */
public class FXMLAnchorPaneProcessoVendasController implements Initializable {

    @FXML
    private TableView<Venda> tblViewPedido;
    @FXML
    private TableColumn<Venda, Integer> tblColNumero;
    @FXML
    private TableColumn<Venda, LocalDate> tblColData;
    @FXML
    private TableColumn<Venda, Venda> tblColCliente;
    @FXML
    private Label lblCodigo;
    @FXML
    private Label lblData;
    @FXML
    private Label lblValor;
    @FXML
    private Label lblPago;
    @FXML
    private Label lblCliente;
    @FXML
    private Button btnInserir;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnAlterar;
    
    List<Venda> listaVendas;
    ObservableList<Venda> obsListVenda;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connetion = database.conectar();
    private final VendaDAO vendaDAO= new VendaDAO();
    private final ItemDeVendaDAO itemVendaDAO = new ItemDeVendaDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vendaDAO.setConnection(connetion);
        carregaTabela();
        tblViewPedido.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> selecionarItemTblViewVendas(newValue));
    }    

    private void carregaTabela() {
        tblColNumero.setCellValueFactory(new PropertyValueFactory<>("cdVenda"));
        tblColData.setCellValueFactory(new PropertyValueFactory<>("data"));
        tblColCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        
        listaVendas = vendaDAO.listar();
        
        obsListVenda = FXCollections.observableArrayList(listaVendas);
        tblViewPedido.setItems(obsListVenda);
    }
    public void selecionarItemTblViewVendas(Venda venda){
        if (venda != null){
            lblCodigo.setText(String.valueOf(venda.getCdVenda()));
            lblData.setText(String.valueOf(venda.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            lblValor.setText(String.format("%.2f", venda.getValor()));
            lblPago.setText(String.valueOf(venda.getPago()));
            lblCliente.setText(venda.getCliente().toString());
        }else{
            lblCodigo.setText("");
            lblData.setText("");
            lblPago.setText("");
            lblValor.setText("");
            lblCliente.setText("");
        }
    }
    public void handleButtonInserir() throws IOException{
        Venda venda = new Venda();
        List<ItemDeVenda> listItemDeVenda = new ArrayList<>();
        venda.setItensDeVenda(listItemDeVenda);
        boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessosVendaDialog(venda);
        if(buttonConfirmarClicked){
            try{
                connetion.setAutoCommit(false);
                vendaDAO.setConnection(connetion);
                vendaDAO.inserir(venda);
                itemVendaDAO.setConnection(connetion);
                produtoDAO.setConnection(connetion);
                for (ItemDeVenda listaItemDeVenda : venda.getItensDeVenda()){
                    Produto produto = listaItemDeVenda.getProduto();
                    listaItemDeVenda.setVenda(vendaDAO.buscarUltimaVenda());
                    itemVendaDAO.inserir(listaItemDeVenda);
                    produto.setQuantidade(produto.getQuantidade() - listaItemDeVenda.getQuantidade());
                    produtoDAO.alterar(produto);
                }
                connetion.commit();
                carregaTabela();
            }catch(SQLException ex){
                try{
                    connetion.rollback();
                }catch(SQLException e){
                    Logger.getLogger(FXMLAnchorPaneProcessoVendasController.class.getName()).log(Level.SEVERE, null, e);
                }
                Logger.getLogger(FXMLAnchorPaneProcessoVendasController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean showFXMLAnchorPaneProcessosVendaDialog(Venda venda) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneProcessosVendaDialogController.class.getResource("/javafxmvc/view/FXMLAnchorPaneProcessosVendaDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Registro de Vendas");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        FXMLAnchorPaneProcessosVendaDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVenda(venda);
        dialogStage.showAndWait();
        return controller.isBtnConfirmarClicked();
    }
    @FXML
    public void handleButtonRemover() throws IOException, SQLException {
        Venda venda = tblViewPedido.getSelectionModel().getSelectedItem();
        if(venda != null){
            connetion.setAutoCommit(false);
            vendaDAO.setConnection(connetion);
            itemVendaDAO.setConnection(connetion);
            produtoDAO.setConnection(connetion);
            for(ItemDeVenda listItemDeVenda : venda.getItensDeVenda()){
                Produto produto = listItemDeVenda.getProduto();
                produto.setQuantidade(produto.getQuantidade() + listItemDeVenda.getQuantidade());
                System.out.println(produto.getQuantidade());
                produtoDAO.alterar(produto);
                itemVendaDAO.remover(listItemDeVenda);
            }
            vendaDAO.remover(venda);
            connetion.commit();
            carregaTabela();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor escolha uma venda na tabela");
            alert.show();
        }
    }
}
