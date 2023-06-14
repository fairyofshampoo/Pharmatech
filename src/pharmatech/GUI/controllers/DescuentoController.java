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
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import pharmatech.GUI.Pharmatech;
import pharmatech.logic.ArticuloCatalogo;
import pharmatech.logic.DAO.ArticuloDAO;
import pharmatech.logic.DAO.DescuentoDAO;
import pharmatech.logic.DAO.DiaDAO;
import pharmatech.logic.Descuento;
import pharmatech.logic.Dia;

/**
 * FXML Controller class
 *
 * @author Star3oy
 */
public class DescuentoController implements Initializable {
    
    @FXML
    private Button buttonCancelar;

    @FXML
    private Button buttonGuardar;

    @FXML
    private ChoiceBox<ArticuloCatalogo> choiceBoxArticulo;

    @FXML
    private ChoiceBox<Dia> choiceBoxDiaPromocion;

    @FXML
    private TextField textFieldPorcentajeDescuento;
    
    private void fillChoiceBoxDiaPromocion() {
        DiaDAO diaDAO = new DiaDAO();
        List<Dia> diaList = null;
        ObservableList<Dia> diaObservableList = FXCollections.observableArrayList();
           try {
               diaList = diaDAO.getDias();
           } catch (SQLException ex) {
               Logger.getLogger(DescuentoController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DescuentoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(articuloCatalogoList != null) {
            articuloCatalogoObservableList.addAll(articuloCatalogoList);
        }
        choiceBoxArticulo.setItems(articuloCatalogoObservableList);
    }
    
    
    private boolean isValid() {
        boolean result = true;
        if (choiceBoxArticulo.getItems().isEmpty() || choiceBoxDiaPromocion.getItems().isEmpty() || textFieldPorcentajeDescuento.getText().isEmpty() ) {
            result = false;
        } 
        return result;
    }   
    
    @FXML
    void buttonCancelar(ActionEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/ConsultarDescuentos.fxml");
        } catch (IOException ex) {
            Logger.getLogger(DescuentoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void buttonGuardar(ActionEvent event) {
        if (isValid()) {      
                Descuento descuento = new Descuento();
                descuento.setIdArticuloCatalogo(choiceBoxArticulo.getValue().getId());
                descuento.setDiaPromocion(choiceBoxDiaPromocion.getValue().getIdDia());
                String cadenaDescuento = textFieldPorcentajeDescuento.getText();
                int porcentajeDescuento = Integer.parseInt(cadenaDescuento);
                descuento.setPorcentaje(porcentajeDescuento);
                DescuentoDAO descuentoDAO = new DescuentoDAO();
              try { 
                descuentoDAO.addDescuento(descuento);
            } catch (SQLException ex) {
                Logger.getLogger(DescuentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       fillchoiceBoxArticulo();
       fillChoiceBoxDiaPromocion();
    }    
    
}
