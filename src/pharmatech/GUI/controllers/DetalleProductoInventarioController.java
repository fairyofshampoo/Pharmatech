package pharmatech.GUI.controllers;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import pharmatech.GUI.Pharmatech;
import pharmatech.logic.Articulo;
import pharmatech.logic.DAO.ArticuloDAO;
import pharmatech.logic.Status;


public class DetalleProductoInventarioController implements Initializable {
    public static int identificador;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    @FXML
    private DatePicker dpCaducidad;

    @FXML
    private Label lblCaducidadError;

    @FXML
    private Label lblCantidadError;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblTipo;

    @FXML
    private TextField txtCantidad;

    @FXML
    private Label lblCodigo;

    @FXML
    void updateArticulo(ActionEvent event) {
        if(!isEmpty()){
            guardar();
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        try {
            if(acceptGoBack()){
                Pharmatech.setRoot("/pharmatech/GUI/Inventario.fxml");
            }                 
        } catch (IOException ex) {
            Logger.getLogger(DetalleProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void setCantidadErrorInvisible(KeyEvent event) {
        if (Character.isDigit(event.getCharacter().charAt(0))) {
            lblCantidadError.setVisible(false);
            event.consume();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblCaducidadError.visibleProperty().bind(dpCaducidad.valueProperty().isNotNull());
        txtCantidad.setTextFormatter(new TextFormatter<>(createNumericFilter()));
        showArticuloData();
    }
    
    private void guardar(){
        ArticuloDAO articuloDAO = new ArticuloDAO();
        List<Articulo> listaArticulo = new ArrayList<>();
          
        try {
            listaArticulo = articuloDAO.getAllArticulos();
        } catch (SQLException ex) {
            Logger.getLogger(RegistroProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        boolean isRegistered = false;
        int contador = 0;
        
        if(listaArticulo.get(contador).getId() != -1){
            while(!isRegistered && contador < listaArticulo.size()){
                if(listaArticulo.get(contador).getFechaCaducidad().equals(setDate(dpCaducidad)) && listaArticulo.get(contador).getArticuloCatalogo().getId().equals(lblCodigo.getText()) && listaArticulo.get(contador).getId() != identificador){
                    DialogGenerator.getDialog(new AlertMessage ("Ya existe otro producto con las mismas características",Status.ERROR));
                    isRegistered = true;
                }
                contador++;
            }
        }
        
        if(!isRegistered){
            Articulo articulo = new Articulo();
            articulo.setCantidad(Integer.parseInt(txtCantidad.getText()));
            articulo.setFechaCaducidad(setDate(dpCaducidad));
            articulo.setId(identificador);
            int result = -1;

            try {
                result = articuloDAO.updateArticuloInventario(articulo);
            } catch (SQLException ex) {
                Logger.getLogger(RegistroProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(result > 0){
                DialogGenerator.getDialog(new AlertMessage ("Producto modificado correctamente",Status.SUCCESS));
                try {
                    Pharmatech.setRoot("/pharmatech/GUI/Inventario.fxml");
                } catch (IOException ex) {
                    Logger.getLogger(DetalleProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                DialogGenerator.getDialog(new AlertMessage ("Hubo un problema al momento de modificar",Status.ERROR));
            }
        }
    }
    
    private void showArticuloData(){
        Articulo articulo = new Articulo();
        ArticuloDAO articuloDAO = new ArticuloDAO();
        
        try {
            articulo = articuloDAO.getArticuloInventario(identificador);
        } catch (SQLException ex) {
            Logger.getLogger(DetalleProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(articulo.getId() != -1){
            lblCodigo.setText(articulo.getArticuloCatalogo().getId());
            lblNombre.setText(articulo.getArticuloCatalogo().getNombre());
            lblTipo.setText(articulo.getArticuloCatalogo().getTipo());
            txtCantidad.setText(Integer.toString(articulo.getCantidad()));
            
            Date date = (Date) articulo.getFechaCaducidad();
            LocalDate localDate = date.toLocalDate();
            dpCaducidad.setValue(localDate);
        }
    }
    
    private java.sql.Date setDate(DatePicker datePicker){
        LocalDate localDate = datePicker.getValue();   
        java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
        
        return sqlDate;
    }
    
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d*");
    private UnaryOperator<TextFormatter.Change> createNumericFilter() {
        return change -> {
            String newText = change.getControlNewText();
            if (NUMBER_PATTERN.matcher(newText).matches()) {
                return change;
            }
            return null;
        };
    }
    
    public boolean isEmpty(){
        boolean result = false;
        if(FieldValidation.isNullOrEmptyTxtField(txtCantidad)){
            lblCantidadError.setText("Campo vacío");
            lblCantidadError.setVisible(true);
            result = true;
        }
        if(dpCaducidad.getValue() == null){
            lblCaducidadError.setText("Campo vacío");
            lblCaducidadError.visibleProperty().bind(dpCaducidad.valueProperty().isNull());
            result = true;
        }
        return result;
    }
    
    public boolean acceptGoBack(){
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Deseas cancelar la modificación?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
}
