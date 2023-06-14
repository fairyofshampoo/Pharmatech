package pharmatech.GUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import pharmatech.GUI.Pharmatech;
import pharmatech.logic.ArticuloCatalogo;
import pharmatech.logic.DAO.ArticuloDAO;
import pharmatech.logic.DAO.DescuentoDAO;
import pharmatech.logic.Descuento;

/**
 * FXML Controller class
 *
 * @author Star3oy
 */
public class ConsultarDescuentosController implements Initializable {

     ObservableList<TablaPromociones> list = FXCollections.observableArrayList();
    
    @FXML
    private Button botonEliminar;

    @FXML
    private Button botonModificar;

    @FXML
    private Button botonadd;

    @FXML
    private TableColumn<TablaPromociones, String> columnaArticulo;

    @FXML
    private TableColumn<TablaPromociones, String> columnaDia;

    @FXML
    private TableColumn<TablaPromociones, Integer> columnaIdPromocion;

    @FXML
    private TableView<TablaPromociones> tablaviewPromociones;

    @FXML
    void botonEliminar(ActionEvent event) {
        if (!tablaviewPromociones.getSelectionModel().isEmpty()) {
            int identificator = this.tablaviewPromociones.getSelectionModel().getSelectedItem().getIdentificador();
        DescuentoDAO descuentoDAO = new DescuentoDAO();
         try {
             descuentoDAO.deleteDescuento(identificator);
         } catch (SQLException ex) {
             Logger.getLogger(ConsultarDescuentosController.class.getName()).log(Level.SEVERE, null, ex);
         }
         tablaviewPromociones.getItems().clear();
         llenarTabla();       
        } else {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Debe seleccionar un descuento");
            alert.showAndWait();  
        }
    }
    
    @FXML
    void goBack(MouseEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/OpcionesOfertas.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ConsultarDescuentosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void botonModificar(ActionEvent event) {
        if(!tablaviewPromociones.getSelectionModel().isEmpty()) {
        int identificator = this.tablaviewPromociones.getSelectionModel().getSelectedItem().getIdentificador();  
        ModificarDescuentoController.identificador = identificator;
        try {
            Pharmatech.setRoot("/pharmatech/GUI/ModificarDescuento.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ModificarDescuentoController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        } else {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Debe seleccionar un descuento");
            alert.showAndWait();
        }
    }

    @FXML
    void botonadd(ActionEvent event) {
         try {
             Pharmatech.setRoot("/pharmatech/GUI/Descuento.fxml");
         } catch (IOException ex) {
             Logger.getLogger(ConsultarDescuentosController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    public void llenarTabla() {
     DescuentoDAO descuentoDAO = new DescuentoDAO();
     ArticuloDAO articuloDAO = new ArticuloDAO();
     List<Descuento> listaDescuento;
        try {
            listaDescuento = descuentoDAO.getAllPromociones();
        for (int i = 0; i < listaDescuento.size(); i++) {
            Descuento descuento = listaDescuento.get(i);
            ArticuloCatalogo catalogo = articuloDAO.getArticuloCatalogo(descuento.getIdArticuloCatalogo());
            String dia = "";
            switch(descuento.getDiaPromocion()){
                case 1 -> dia = "Domingo";
                case 2 -> dia = "Lunes";
                case 3 -> dia = "Martes";
                case 4 -> dia = "Miércoles";
                case 5 -> dia = "Jueves";
                case 6 -> dia = "Viernes";
                case 7 -> dia = "Sábado";
            }
            list.add(new TablaPromociones (descuento.getIdDescuento(), catalogo.getNombre(), dia));
        }
          } catch (SQLException ex) {
            Logger.getLogger(ConsultarDescuentosController.class.getName()).log(Level.SEVERE, null, ex);
        }
     tablaviewPromociones.setItems(list);
     columnaIdPromocion.setCellValueFactory(new PropertyValueFactory<TablaPromociones, Integer>("identificador"));
     columnaArticulo.setCellValueFactory(new PropertyValueFactory<TablaPromociones, String>("articulo"));
     columnaDia.setCellValueFactory(new PropertyValueFactory<TablaPromociones, String>("dia"));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        llenarTabla();
    }    
    
}
