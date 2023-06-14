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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import pharmatech.GUI.Pharmatech;
import pharmatech.logic.ArticuloCatalogo;
import pharmatech.logic.DAO.ArticuloDAO;
import pharmatech.logic.DAO.PaqueteDAO;
import pharmatech.logic.Paquete;

/**
 * FXML Controller class
 *
 * @author Star3oy
 */
public class ConsultarPaquetesController implements Initializable {
    
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
    private TableColumn<TablaPromociones, Integer> columnaDia;

    @FXML
    private TableColumn<TablaPromociones, Integer> columnaIdPromocion;

    @FXML
    private TableView<TablaPromociones> tablaviewPromociones;
    
    @FXML
    void botonEliminar(ActionEvent event) {
        if (!tablaviewPromociones.getSelectionModel().isEmpty()) {
                   int identificator = this.tablaviewPromociones.getSelectionModel().getSelectedItem().getIdentificador();
        PaqueteDAO paqueteDAO = new PaqueteDAO();
         try {
             paqueteDAO.deleteDescuento(identificator);
         } catch (SQLException ex) {
             Logger.getLogger(ConsultarDescuentosController.class.getName()).log(Level.SEVERE, null, ex);
         }
         tablaviewPromociones.getItems().clear();
         llenarTabla(); 
        } else {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Debe seleccionar un usuario");
            alert.showAndWait();
        }

    }
    
    @FXML
    void goBack(MouseEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/OpcionesOfertas.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ConsultarDosPorUnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     @FXML
    void botonModificar(ActionEvent event) {  
        if(!tablaviewPromociones.getSelectionModel().isEmpty()) {
        int identificator = this.tablaviewPromociones.getSelectionModel().getSelectedItem().getIdentificador();
        ModificarPaqueteController.identificador = identificator;
        try {
            Pharmatech.setRoot("/pharmatech/GUI/ModificarPaquete.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ConsultarDosPorUnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        } else {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Debe seleccionar un usuario");
            alert.showAndWait();
        }
    }
    
    @FXML
    void botonadd(ActionEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/Paquete.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ConsultarPaquetesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void llenarTabla() {
     PaqueteDAO paqueteDAO = new PaqueteDAO();
     ArticuloDAO articuloDAO = new ArticuloDAO();
     List<Paquete> listaPaquete;
        try {
            listaPaquete = paqueteDAO.getAllPromociones();
        for (int i = 0; i < listaPaquete.size(); i++) {
            Paquete paquete = listaPaquete.get(i);
            ArticuloCatalogo catalogo = articuloDAO.getArticuloCatalogo(paquete.getIdArticuloCatalogoCompra());
            String dia = "";
            switch(paquete.getIdDiaPromocion()){
                case 1 -> dia = "Domingo";
                case 2 -> dia = "Lunes";
                case 3 -> dia = "Martes";
                case 4 -> dia = "Miércoles";
                case 5 -> dia = "Jueves";
                case 6 -> dia = "Viernes";
                case 7 -> dia = "Sábado";
            }
           list.add(new TablaPromociones (paquete.getIdPaquete(), catalogo.getNombre(), dia));
        }
          } catch (SQLException ex) {
            Logger.getLogger(ConsultarDescuentosController.class.getName()).log(Level.SEVERE, null, ex);
        }
     tablaviewPromociones.setItems(list);
     columnaIdPromocion.setCellValueFactory(new PropertyValueFactory<TablaPromociones, Integer>("identificador"));
     columnaArticulo.setCellValueFactory(new PropertyValueFactory<TablaPromociones, String>("articulo"));
     columnaDia.setCellValueFactory(new PropertyValueFactory<TablaPromociones, Integer>("dia"));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      llenarTabla();
    }    
    
}