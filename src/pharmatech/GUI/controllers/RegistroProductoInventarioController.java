package pharmatech.GUI.controllers;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import pharmatech.GUI.Pharmatech;
import pharmatech.logic.Articulo;
import pharmatech.logic.ArticuloCatalogo;
import pharmatech.logic.DAO.ArticuloDAO;
import pharmatech.logic.Status;


public class RegistroProductoInventarioController implements Initializable {
    
    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnCancelar;
    
    @FXML
    private ChoiceBox<ArticuloCatalogo> cbxArticulo;

    @FXML
    private DatePicker dpCaducidad;
    
    @FXML
    private ImageView imgSearch;
    
    @FXML
    private Label lblCaducidadError;
    
    @FXML
    private Label lblCantidadError;

    @FXML
    private Label lblNombreError;
    
    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtNombre;
    
    private String codigo = "000";
    
    @FXML
    void addArticulo(ActionEvent event) {
        if(!isEmpty()){
            agregar();
        }
    }

    @FXML
    void goBack(ActionEvent  event) {
        try {
            if(acceptGoBack()){
                Pharmatech.setRoot("/pharmatech/GUI/Inventario.fxml");
            }                 
        } catch (IOException ex) {
            Logger.getLogger(RegistroProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void searchArticulo(MouseEvent event) {
        if(FieldValidation.isNullOrEmptyTxtField(txtNombre)){
            lblNombreError.setText("No has ingresado un nombre");
            lblNombreError.setVisible(true);
            setArticulosChoiceBox(getArticulosCatalogo());
        }else{
            searchNombreArticulo();
        }
    }
    
    @FXML
    void setCantidadErrorInvisible(KeyEvent event) {
        if (Character.isDigit(event.getCharacter().charAt(0))) {
            lblCantidadError.setVisible(false);
            event.consume();
        }
    }
    
    @FXML
    void setCodigoErrorInvisible(KeyEvent event) {
        lblNombreError.setVisible(false);
    }
    
    private void setArticulosChoiceBox(List<ArticuloCatalogo> listaArticulos){
        ObservableList<ArticuloCatalogo> articulosObservableList = FXCollections.observableArrayList();
        
        if(!listaArticulos.isEmpty()){
            for(ArticuloCatalogo articulo : listaArticulos){
                articulosObservableList.add(articulo);
            }
            cbxArticulo.setItems(articulosObservableList);
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblCaducidadError.visibleProperty().bind(dpCaducidad.valueProperty().isNotNull());
        txtCantidad.setTextFormatter(new TextFormatter<>(createNumericFilter()));
        setArticulosChoiceBox(getArticulosCatalogo());
        cbxArticulo.setOnAction(event -> {
            if (cbxArticulo.getValue() != null) {
                lblNombreError.setVisible(false);
                codigo = getCodigo();
            }else{
                setArticulosChoiceBox(getArticulosCatalogo());
            }
        });
    }
    
    private String getCodigo(){
        ArticuloDAO articuloDAO = new ArticuloDAO();
        try {
            codigo = articuloDAO.getCodigoByName(cbxArticulo.getValue());
        } catch (SQLException ex) {
            Logger.getLogger(RegistroProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return codigo;
    }
    
    private List<ArticuloCatalogo> getArticulosCatalogo(){
        ArticuloDAO articuloDAO = new ArticuloDAO();
        List<ArticuloCatalogo> listaArticulo = new ArrayList<>();
          
        try {
            listaArticulo = articuloDAO.getAllArticuloCatalogo();
        } catch (SQLException ex) {
            Logger.getLogger(RegistroProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaArticulo;
    }
    
    private List<Articulo> getArticulos(){
        ArticuloDAO articuloDAO = new ArticuloDAO();
        List<Articulo> listaArticulo = new ArrayList<>();
          
        try {
            listaArticulo = articuloDAO.getAllArticulos();
        } catch (SQLException ex) {
            Logger.getLogger(RegistroProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return listaArticulo;
    }
    
    public void agregar(){
        ArticuloDAO articuloDAO = new ArticuloDAO();
        List<Articulo> listaArticulo = getArticulos();
        
        boolean isRegistered = false;
        int contador = 0;
        
        if(listaArticulo.get(contador).getId() != -1){
            while(!isRegistered && contador < listaArticulo.size()){
                if(listaArticulo.get(contador).getFechaCaducidad().equals(setDate(dpCaducidad)) && listaArticulo.get(contador).getArticuloCatalogo().getId().equals(codigo)){
                    DialogGenerator.getDialog(new AlertMessage ("Ya existe otro producto con las mismas características",Status.ERROR));
                    isRegistered = true;
                }
                contador++;
            }
        }
        
        if(!isRegistered){
            Articulo articulo = new Articulo();
            ArticuloCatalogo articuloCatalogo = new ArticuloCatalogo();
            articuloCatalogo.setId(getCodigo());
            articulo.setCantidad(Integer.parseInt(txtCantidad.getText()));
            articulo.setFechaCaducidad(setDate(dpCaducidad));
            articulo.setArticuloCatalogo(articuloCatalogo);
            int result = -1;

            try {
                result = articuloDAO.addArticuloInventario(articulo);
            } catch (SQLException ex) {
                Logger.getLogger(RegistroProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(result > 0){
                DialogGenerator.getDialog(new AlertMessage ("Producto agregado correctamente",Status.SUCCESS));
                try {
                    Pharmatech.setRoot("/pharmatech/GUI/Inventario.fxml");
                } catch (IOException ex) {
                    Logger.getLogger(RegistroProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                DialogGenerator.getDialog(new AlertMessage ("Hubo un problema al momento de agregar",Status.ERROR));
            }
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
    
    
    
    public void searchNombreArticulo(){
        ArticuloDAO articuloDAO = new ArticuloDAO();
        List<ArticuloCatalogo> articuloCatalogoList = new ArrayList<>();

        try {
            articuloCatalogoList = articuloDAO.getArticulosCatalogoByName(txtNombre.getText());
        } catch (SQLException ex) {
            Logger.getLogger(RegistroProductoInventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(articuloCatalogoList.isEmpty()){
            DialogGenerator.getDialog(new AlertMessage ("No se encontró el producto buscado",Status.ERROR));
            setArticulosChoiceBox(getArticulosCatalogo());
        }else{
            DialogGenerator.getDialog(new AlertMessage ("Se ha encontrado el producto",Status.SUCCESS));
            setArticulosChoiceBox(articuloCatalogoList);
        }
    }
    
    public boolean isEmpty(){
        boolean result = false;
        if(cbxArticulo.getValue() == null){
            lblNombreError.setText("Debes seleccionar un producto");
            lblNombreError.setVisible(true);
            result = true;
        }
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
        Optional<ButtonType> response = DialogGenerator.getConfirmationDialog("¿Deseas cancelar el registro?");
        return (response.get() == DialogGenerator.BUTTON_YES);
    }
}
