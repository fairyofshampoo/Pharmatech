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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import pharmatech.GUI.Pharmatech;
import pharmatech.logic.ArticuloCatalogo;
import pharmatech.logic.DAO.ArticuloDAO;
import pharmatech.logic.DAO.DescuentoDAO;
import pharmatech.logic.DAO.DiaDAO;
import pharmatech.logic.DAO.PaqueteDAO;
import pharmatech.logic.Descuento;
import pharmatech.logic.Dia;
import pharmatech.logic.Paquete;

/**
 * FXML Controller class
 *
 * @author Star3oy
 */
public class PaqueteController implements Initializable {

    @FXML
    private Button buttonCancelar;

    @FXML
    private Button buttonGuardar;

    @FXML
    private ChoiceBox<ArticuloCatalogo> choiceBoxCompra;

    @FXML
    private ChoiceBox<Dia> choiceBoxDiaPromocion;

    @FXML
    private ChoiceBox<ArticuloCatalogo> choiceBoxRegalo;

    @FXML
    void buttonCancelar(ActionEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/ConsultarPaquetes.fxml");
        } catch (IOException ex) {
            Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void buttonGuardar(ActionEvent event) {
           if (isValid()) {      
                Paquete paquete = new Paquete();
                paquete.setIdArticuloCatalogoCompra(choiceBoxCompra.getValue().getId());
                paquete.setIdArticuloCatalogoRegalo(choiceBoxRegalo.getValue().getId());
                paquete.setIdDiaPromocion(choiceBoxDiaPromocion.getValue().getIdDia());
                PaqueteDAO paqueteDAO = new PaqueteDAO();
              try { 
                paqueteDAO.addDescuento(paquete);
            } catch (SQLException ex) {
                Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
     private void fillChoiceBoxDiaPromocion() {
        DiaDAO diaDAO = new DiaDAO();
        List<Dia> diaList = null;
        ObservableList<Dia> diaObservableList = FXCollections.observableArrayList();
           try {
               diaList = diaDAO.getDias();
           } catch (SQLException ex) {
               Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
           }
           if (diaList != null) {
               diaObservableList.addAll(diaList);
           }
           choiceBoxDiaPromocion.setItems(diaObservableList);
    }
    
    private void fillchoiceBoxArticulo() {
        ArticuloDAO articuloDAO = new ArticuloDAO();
        List<ArticuloCatalogo> articuloCatalogoList = null;
        ObservableList<ArticuloCatalogo> articuloCatalogoObservableList = FXCollections.observableArrayList();
        try {
            articuloCatalogoList = articuloDAO.getAllArticuloCatalogo();
        } catch (SQLException ex) {
            Logger.getLogger(PaqueteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(articuloCatalogoList != null) {
            articuloCatalogoObservableList.addAll(articuloCatalogoList);
        }
        choiceBoxCompra.setItems(articuloCatalogoObservableList);
        choiceBoxRegalo.setItems(articuloCatalogoObservableList);
    }
    
    private boolean isValid() {
        boolean result = true;
        if (choiceBoxCompra.getItems().isEmpty() || choiceBoxDiaPromocion.getItems().isEmpty() || choiceBoxRegalo.getItems().isEmpty() ) {
            result = false;
        } 
        return result;
    }   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillchoiceBoxArticulo();
        fillChoiceBoxDiaPromocion();
    }    
    
}

