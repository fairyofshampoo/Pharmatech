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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import pharmatech.GUI.Pharmatech;
import pharmatech.logic.ArticuloCatalogo;
import pharmatech.logic.DAO.ArticuloDAO;
import pharmatech.logic.DAO.DiaDAO;
import pharmatech.logic.DAO.DosPorUnoDAO;
import pharmatech.logic.Dia;
import pharmatech.logic.DosPorUno;

/**
 * FXML Controller class
 *
 * @author Star3oy
 */
public class ModificarDosPorUnoController implements Initializable {
    
    public static int identificador;
    
    @FXML
    private Button buttonCancelar;

    @FXML
    private Button buttonGuardar;

    @FXML
    private ChoiceBox<ArticuloCatalogo> choiceBoxArticulo;

    @FXML
    private ChoiceBox<Dia> choiceBoxDiaPromocion;

    @FXML
    private TextField txtCompra;

    @FXML
    private TextField txtRegalo;

    @FXML
    void buttonCancelar(ActionEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/ConsultarDosPorUno.fxml");
        } catch (IOException ex) {
            Logger.getLogger(ModificarDosPorUnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void buttonGuardar(ActionEvent event) {
        if (isValid()) {      
                DosPorUno dosPorUno = new DosPorUno();
               dosPorUno.setIdAriculoCatalogo(choiceBoxArticulo.getValue().getId());
                dosPorUno.setIdDiaPromocion(choiceBoxDiaPromocion.getValue().getIdDia());
                String compra = txtCompra.getText();
                String regalo = txtRegalo.getText();
                int compraInt = Integer.parseInt(compra);
                int regaloInt = Integer.parseInt(regalo);
                dosPorUno.setCantidadCompra(compraInt);
                dosPorUno.setCantidadRegalo(regaloInt);
                DosPorUnoDAO dosPorUnoDAO = new DosPorUnoDAO();
              try { 
                dosPorUnoDAO.modificarDescuento(dosPorUno, identificador);
            } catch (SQLException ex) {
                Logger.getLogger(DescuentoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private boolean isValid() {
        boolean result = true;
        if (choiceBoxArticulo.getItems().isEmpty() || choiceBoxDiaPromocion.getItems().isEmpty() || txtCompra.getText().isEmpty() || txtRegalo.getText().isEmpty()) {
            result = false;
        } 
        return result;
    }   
    
     private void fillChoiceBoxDiaPromocion() {
        DiaDAO diaDAO = new DiaDAO();
        List<Dia> diaList = null;
        ObservableList<Dia> diaObservableList = FXCollections.observableArrayList();
           try {
               diaList = diaDAO.getDias();
           } catch (SQLException ex) {
               Logger.getLogger(ModificarDosPorUnoController.class.getName()).log(Level.SEVERE, null, ex);
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
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillchoiceBoxArticulo();
        fillChoiceBoxDiaPromocion();
    }    
    
}
