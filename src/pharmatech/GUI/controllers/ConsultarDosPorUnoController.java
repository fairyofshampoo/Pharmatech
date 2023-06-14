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
import pharmatech.logic.DAO.DosPorUnoDAO;
import pharmatech.logic.DosPorUno;

/**
 * FXML Controller class
 *
 * @author Star3oy
 */
public class ConsultarDosPorUnoController implements Initializable {

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
        DosPorUnoDAO dosPorUnoDAO = new DosPorUnoDAO();
         try {
             dosPorUnoDAO.deleteDescuento(identificator);
         } catch (SQLException ex) {
             Logger.getLogger(ConsultarDosPorUnoController.class.getName()).log(Level.SEVERE, null, ex);
         }
         tablaviewPromociones.getItems().clear();
         llenarTabla();  
        } else {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Debe seleccionar un dos por uno");
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
        ModificarDosPorUnoController.identificador = identificator;
        try {
            Pharmatech.setRoot("/pharmatech/GUI/ModificarDosPorUno.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ConsultarDosPorUnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        } else {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Debe seleccionar un dos por uno");
            alert.showAndWait();
        }
    }

    @FXML
    void botonadd(ActionEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/DosPorUno.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ConsultarDosPorUnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void llenarTabla() {
     DosPorUnoDAO dosPorUnoDAO = new DosPorUnoDAO();
     ArticuloDAO articuloDAO = new ArticuloDAO();
     List<DosPorUno> listaDosPorUno;
        try {
            listaDosPorUno = dosPorUnoDAO.getAllPromociones();
        for (int i = 0; i < listaDosPorUno.size(); i++) {
            DosPorUno dosPorUno = listaDosPorUno.get(i);
            ArticuloCatalogo catalogo = articuloDAO.getArticuloCatalogo(dosPorUno.getIdAriculoCatalogo());
            String dia = "";
            switch(dosPorUno.getIdDiaPromocion()){
                case 1 -> dia = "Domingo";
                case 2 -> dia = "Lunes";
                case 3 -> dia = "Martes";
                case 4 -> dia = "Miércoles";
                case 5 -> dia = "Jueves";
                case 6 -> dia = "Viernes";
                case 7 -> dia = "Sábado";
            }
           list.add(new TablaPromociones (dosPorUno.getIdDosPorUno(), catalogo.getNombre(), dia));
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
