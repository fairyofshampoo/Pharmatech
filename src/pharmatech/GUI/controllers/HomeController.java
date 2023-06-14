/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package pharmatech.GUI.controllers;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import pharmatech.GUI.Pharmatech;
import javafx.event.ActionEvent;

/**
 * FXML Controller class
 *
 * @author miche
 */
public class HomeController implements Initializable {
    @FXML
    private Button btnInventario;

    @FXML
    private Button btnPromos;

    @FXML
    private Button btnVenta;
    
    @FXML
    void showVentaView(ActionEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/Venta.fxml");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void showPromosView(ActionEvent event) {
        try {
            Pharmatech.setRoot("/pharmatech/GUI/OpcionesOfertas.fxml");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void showInventarioView(ActionEvent event){
        try {
            Pharmatech.setRoot("/pharmatech/GUI/Inventario.fxml");
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
