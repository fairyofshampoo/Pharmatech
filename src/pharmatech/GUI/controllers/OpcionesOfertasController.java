package pharmatech.GUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import pharmatech.GUI.Pharmatech;


/**
 * FXML Controller class
 *
 * @author Star3oy
 */
public class OpcionesOfertasController implements Initializable {

    @FXML
    private Button buttonDescuento;

    @FXML
    private Button buttonDosPorUno;

    @FXML
    private Button buttonPaquetes;

    @FXML
    private ImageView imgBack;

    @FXML
    void buttonDescuento(ActionEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/ConsultarDescuentos.fxml");
        } catch (IOException ex) {
            Logger.getLogger(OpcionesOfertasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void buttonDosPorUno(ActionEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/ConsultarDosPorUno.fxml");
        } catch (IOException ex) {
            Logger.getLogger(OpcionesOfertasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void buttonPaquetes(ActionEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/ConsultarPaquetes.fxml");
        } catch (IOException ex) {
            Logger.getLogger(OpcionesOfertasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void goBack(MouseEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/Home.fxml");
        } catch (IOException ex) {
            Logger.getLogger(OpcionesOfertasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
