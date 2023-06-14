package pharmatech.GUI.controllers;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pharmatech.GUI.Pharmatech;
import pharmatech.logic.Articulo;
import pharmatech.logic.DAO.ArticuloDAO;
import pharmatech.logic.Status;


public class InventarioController implements Initializable {
    @FXML
    private Button btnEliminar;
    
    @FXML
    private Button btnModificar;
    
    @FXML
    private ImageView imgAdd;
    
    @FXML
    private ImageView imgRegresar;
    
    @FXML
    private TableColumn tblCCantidad;

    @FXML
    private TableColumn tblCCodigo;
    
    @FXML
    private TableColumn tblCEstado;

    @FXML
    private TableColumn tblCFechaCaducidad;
    
    @FXML
    private TableColumn tblCId;

    @FXML
    private TableColumn tblCNombreProducto;
    
    @FXML
    private TableColumn tblCTipo;
    
    @FXML
    private TableView<TableProductosInventario> tblProducto;
    
    @FXML
    void addArticulo(MouseEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/RegistroProductoInventario.fxml");
        } catch (IOException ex) {
            Logger.getLogger(InventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void eliminar(ActionEvent event) {
        if(this.tblProducto.getSelectionModel().getSelectedItem() != null){
            int identificador = this.tblProducto.getSelectionModel().getSelectedItem().getId();
            ArticuloDAO articuloDAO =  new ArticuloDAO();
            if(acceptDelete()){
                int response = -1;
                try {
                    response = articuloDAO.deleteArticuloInventario(identificador);
                } catch (SQLException ex) {
                    Logger.getLogger(InventarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if(response > 0){
                    DialogGenerator.getDialog(new AlertMessage ("Producto eliminado correctamente",Status.SUCCESS));
                    tblProducto.getItems().clear();
                    showProducts();
                }else{
                    DialogGenerator.getDialog(new AlertMessage ("Hubo un problema al momento de eliminar",Status.ERROR));
                }
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Debe seleccionar un producto");
            alert.showAndWait();
        }
    }
    
    public boolean acceptDelete(){
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Deseas eliminar el producto?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
    
    @FXML
    void modificar(ActionEvent event) {
        if(this.tblProducto.getSelectionModel().getSelectedItem() != null){
            int identificador = this.tblProducto.getSelectionModel().getSelectedItem().getId();
            DetalleProductoInventarioController.identificador = identificador;
            try {
                Pharmatech.setRoot("/pharmatech/GUI/DetalleProductoInventario.fxml");
            } catch (IOException ex) {
                Logger.getLogger(InventarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Debe seleccionar un producto");
            alert.showAndWait();
        }
    }
    
    private ObservableList <TableProductosInventario> obLProducto;
    
    public void showProducts(){
        List<Articulo> listaArticulos = new ArrayList<>();
        ArticuloDAO articuloDAO = new ArticuloDAO();
        
        try {
            listaArticulos = articuloDAO.getAllArticulos();
        } catch (SQLException ex) {
            Logger.getLogger(InventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        obLProducto = FXCollections.observableArrayList();
        
        this.tblCCodigo.setCellValueFactory(new PropertyValueFactory("codigo"));
        this.tblCCantidad.setCellValueFactory(new PropertyValueFactory("cantidad"));
        this.tblCNombreProducto.setCellValueFactory(new PropertyValueFactory("nombreProducto"));
        this.tblCFechaCaducidad.setCellValueFactory(new PropertyValueFactory("fechaCaducidad"));
        this.tblCTipo.setCellValueFactory(new PropertyValueFactory("tipo"));
        this.tblCEstado.setCellValueFactory(new PropertyValueFactory("estado"));
        this.tblCId.setCellValueFactory(new PropertyValueFactory("id"));
        
        
        
        
        if(listaArticulos.get(0).getId() == -1){
            DialogGenerator.getDialog(new AlertMessage ("No hay productos registrados en esta sede",Status.WARNING));
        }else{
            for(int i = 0 ; i < listaArticulos.size(); i++){
                TableProductosInventario tblAuxiliar = new TableProductosInventario();
                tblAuxiliar.setCantidad(listaArticulos.get(i).getCantidad());
                tblAuxiliar.setCodigo(listaArticulos.get(i).getArticuloCatalogo().getId());
                tblAuxiliar.setNombreProducto(listaArticulos.get(i).getArticuloCatalogo().getNombre());
                tblAuxiliar.setFechaCaducidad(listaArticulos.get(i).getFechaCaducidad());
                tblAuxiliar.setTipo(listaArticulos.get(i).getArticuloCatalogo().getTipo());
                tblAuxiliar.setId(listaArticulos.get(i).getId());
                LocalDate fechaActual = LocalDate.now();
                Date fechaCaducidad = listaArticulos.get(i).getFechaCaducidad();
                java.util.Date utilDate = new java.util.Date(fechaCaducidad.getTime());
                Instant instant = utilDate.toInstant();
                
                LocalDate fechaCaducidadLD = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                if(fechaActual.isBefore(fechaCaducidadLD)){
                    tblAuxiliar.setEstado("Disponible");
                }else{
                    tblAuxiliar.setEstado("Caducado");
                }

                this.obLProducto.add(tblAuxiliar);
                this.tblProducto.setItems(obLProducto);
            }
        }
    }
    
    @FXML
    void regresar(MouseEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/Home.fxml");
        } catch (IOException ex) {
            Logger.getLogger(InventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showProducts();
    }    
    
}
