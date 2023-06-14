package pharmatech.GUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import pharmatech.GUI.Pharmatech;
import pharmatech.logic.DAO.EmpleadoDAO;

/**
 * FXML Controller class
 *
 * @author miche
 */
public class LoginController implements Initializable {
    @FXML
    private Button btnLogin;

    @FXML
    private ImageView imgLogin;

    @FXML
    private Label lblWrongEMail;

    @FXML
    private Label lblWrongPassword;

    @FXML
    private TextField txtEMail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    void logIn(MouseEvent event) {
        if(verifyFields()){
            EmpleadoDAO empleadoDAO = new EmpleadoDAO();
            try {
                int userExistence = empleadoDAO.login(txtEMail.getText(), txtPassword.getText());
                continueLogin(userExistence == 1);
            } catch (SQLException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "No hay conexión a la base de datos",
                        "Error conexión", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void continueLogin(boolean isLoginCorrect){
        if(isLoginCorrect){
            displayHome();
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró cuenta de acceso con los datos proporcionados",
                        "Datos erróneos", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void displayHome(){
        try {
            Pharmatech.setRoot("/pharmatech/GUI/Home.fxml");
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean verifyFields(){
        boolean validation = true;
        if (!emailVerification()){
            validation = false;
        }
        if(!passwordVerification()){
            validation = false;
        }
        return validation;
    }
    
    private boolean emailVerification(){
        boolean validation = true;
        
        if (txtEMail.getText().isEmpty() || txtEMail.getText().isBlank()){
            lblWrongEMail.setVisible(true);
            lblWrongEMail.setText("Número de personal inválido");
            validation = false;
        }
        return validation;
    }
    private boolean passwordVerification(){
        boolean validation = true;
        
        if(txtPassword.getText().isEmpty() || txtPassword.getText().isBlank()){
            lblWrongPassword.setVisible(true);
            lblWrongPassword.setText("Contraseña inválida");
            validation = false;
        }
        return validation;
    }
    
    @FXML
    void typingEMail(KeyEvent event) {
        lblWrongEMail.setVisible(false);
    }

    @FXML
    void typingPassword(KeyEvent event) {
        lblWrongPassword.setVisible(false);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
