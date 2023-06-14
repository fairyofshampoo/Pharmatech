
package pharmatech.GUI.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
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
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import pharmatech.GUI.Pharmatech;
import pharmatech.logic.Articulo;
import pharmatech.logic.DAO.ArticuloDAO;
import pharmatech.logic.DAO.DescuentoDAO;
import pharmatech.logic.DAO.DosPorUnoDAO;
import pharmatech.logic.DAO.PaqueteDAO;
import pharmatech.logic.DAO.VentaDAO;
import pharmatech.logic.Descuento;
import pharmatech.logic.DosPorUno;
import pharmatech.logic.Paquete;
import pharmatech.logic.Venta;

/**
 * FXML Controller class
 *
 * @author miche
 */
public class VentaController implements Initializable {
    @FXML
    private Button btnAgregarProducto;
    
    @FXML
    private Button btnBuscarProducto;

    @FXML
    private Button btnCancelarVenta;

    @FXML
    private Button btnGenerarVenta;
    
    @FXML
    private ChoiceBox<Articulo> cbxProductos;

    @FXML
    private TableColumn<VentaTable, Integer> clmnCantidad;

    @FXML
    private TableColumn<VentaTable, String> clmnCodigo;

    @FXML
    private TableColumn<VentaTable, Float> clmnPrecio;

    @FXML
    private TableColumn<VentaTable, String> clmnProducto;

    @FXML
    private TableColumn<VentaTable, Float> clmnTotal;

    @FXML
    private Label lblFechaVenta;

    @FXML
    private Label lblInicio;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblProductoNombre;

    @FXML
    private Label lblProductos;

    @FXML
    private Label lblStock;

    @FXML
    private Label lblTotalPago;

    @FXML
    private Label lblVentas;

    @FXML
    private Spinner<Integer> spnCantidad;

    @FXML
    private TextField txtSearchProducto;
    
    @FXML
    private TableView<VentaTable> tblVenta;
    
    ObservableList<VentaTable> ventaListTable = FXCollections.observableArrayList();
    List<Articulo> articulosList = new ArrayList<>();
    private float totalPago = 0;
    Articulo articulo = null;
    Articulo articuloRegalado = null;
    private final int ERROR = -1;
    private Date currentDate = new Date();
    private float precioRegalo = 0f;
    private int cantidadARegalar = 0;
    private String idArticuloRegalado = "";
    @FXML
    void buscarProducto(ActionEvent event) {
        setProductsChoiceBox();
    }
    
    @FXML
    void agregarProducto(ActionEvent event) {
        if(articulo != null){
            buscarPromocion();
            fillVentaTable();
            setTotalPago();
            articulo.setCantidad(spnCantidad.getValue());
            articulo.getArticuloCatalogo().setPrecio(articulo.calcularSubTotal(spnCantidad.getValue()));
            addProductToVenta();
        } else{
            JOptionPane.showMessageDialog(null, "Seleccione producto y cantidad",
                        "Sin producto", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void setProductsChoiceBox(){
        List<Articulo>productsList = new ArrayList();
        ObservableList<Articulo> productsObservableList = FXCollections.observableArrayList();
            productsList = obtenerArticulosBusqueda();
            
            if(productsList.size() > 0){
                for(Articulo articulo : productsList){
                    productsObservableList.add(articulo);
                }
                cbxProductos.setItems(productsObservableList);
            } else{
                JOptionPane.showMessageDialog(null, "No se encontró producto con ese nombre",
                        "No hay resultados para su búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }
    }
    
    @FXML
    void seleccionarProducto(MouseEvent event) {
        articulo = cbxProductos.getValue();
        if(articulo!= null){
            fillArticuloData(articulo);
        }
    }
    
    private void buscarPromocion(){
        if(!buscarDescuento()){
            if(!buscarDosPorUno()){
                buscarPaquete();
            }
        }
    }
    
    private boolean buscarPaquete(){
        boolean response = false;
        try {
            Paquete paquete = new Paquete();
            PaqueteDAO paqueteDAO = new PaqueteDAO();
            paquete = paqueteDAO.getPaquete(getNumDiaActual(), articulo.getArticuloCatalogo().getId());
            if(paquete.getIdPaquete() != ERROR){
                String idArticuloComprado = paquete.getIdArticuloCatalogoCompra();
                idArticuloRegalado = paquete.getIdArticuloCatalogoRegalo();
                if(articulo.getArticuloCatalogo().getId().equals(idArticuloComprado)){
                    response = true;
                    ArticuloDAO articuloDAO = new ArticuloDAO();
                    articuloRegalado = articuloDAO.getArticulo(idArticuloRegalado);
                    articuloRegalado.getArticuloCatalogo().setPrecio(precioRegalo);
                    cantidadARegalar = 1;
                    articuloRegalado.setCantidad(cantidadARegalar);
                    JOptionPane.showMessageDialog(null, "Se lleva de regalo otro producto",
                        "Paquete", JOptionPane.INFORMATION_MESSAGE);
                    ventaListTable.add(new VentaTable(articuloRegalado.getArticuloCatalogo().getId(), articuloRegalado.getArticuloCatalogo().getNombre(), cantidadARegalar, articuloRegalado.getArticuloCatalogo().getPrecio(), articuloRegalado.calcularSubTotal(cantidadARegalar)));
                    tblVenta.setItems(ventaListTable);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(VentaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    private boolean buscarDosPorUno(){
        boolean response = false;
        try {
            DosPorUno dosPorUno = new DosPorUno();
            DosPorUnoDAO dosPorUnoDAO = new DosPorUnoDAO();
            dosPorUno = dosPorUnoDAO.getDosPorUno(getNumDiaActual(), articulo.getArticuloCatalogo().getId());
            if(dosPorUno.getIdDosPorUno() != ERROR){
                int cantidadAComprar = dosPorUno.getCantidadCompra();
                cantidadARegalar = dosPorUno.getCantidadRegalo();
                if(spnCantidad.getValue() == cantidadAComprar){
                    response = true;
                    JOptionPane.showMessageDialog(null, "Se lleva de regalo " + String.valueOf(cantidadARegalar) +" del mismo producto",
                        "Dos por uno del día", JOptionPane.INFORMATION_MESSAGE);
                    articuloRegalado = articulo;
                    float precioNormal = articulo.getArticuloCatalogo().getPrecio();
                    articuloRegalado.getArticuloCatalogo().setPrecio(precioRegalo);
                    ventaListTable.add(new VentaTable(articuloRegalado.getArticuloCatalogo().getId(), articuloRegalado.getArticuloCatalogo().getNombre(), cantidadARegalar, articuloRegalado.getArticuloCatalogo().getPrecio(), articuloRegalado.calcularSubTotal(cantidadARegalar)));
                    tblVenta.setItems(ventaListTable);
                    articulo.getArticuloCatalogo().setPrecio(precioNormal);
                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(VentaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    private boolean buscarDescuento(){
        boolean response = false;
        try {
            Descuento descuento = new Descuento();
            DescuentoDAO descuentoDAO = new DescuentoDAO();
            descuento = descuentoDAO.getDescuento(getNumDiaActual(), articulo.getArticuloCatalogo().getId());
            if(descuento.getIdDescuento() != ERROR){
                int descuentoPorcentaje = descuento.getPorcentaje();
                JOptionPane.showMessageDialog(null, "Se ha aplicado un descuento del " + String.valueOf(descuentoPorcentaje),
                        "Descuento del día", JOptionPane.INFORMATION_MESSAGE);
                response = true;
                float precio = articulo.getArticuloCatalogo().getPrecio();
                float nuevoPrecio = precio - (precio * descuentoPorcentaje) / 100.0f;
                articulo.getArticuloCatalogo().setPrecio(nuevoPrecio);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VentaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    private int getNumDiaActual(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    
    @FXML
    void cancelarVenta(ActionEvent event) {
        regresarVentana();
    }
    
    private void regresarVentana(){
        try {
            Pharmatech.setRoot("/pharmatech/GUI/Home.fxml");
        } catch (IOException ex) {
            Logger.getLogger(VentaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void vender(ActionEvent event) {
        if(!articulosList.isEmpty()){
            try {
                Venta venta = new Venta();
                venta.setArticulos(articulosList);
                venta.setFechaVenta(currentDate);
                venta.setTotalPago(totalPago);
                VentaDAO ventaDAO = new VentaDAO();
                if(ventaDAO.addVentaTransaction(venta) == ERROR){
                    JOptionPane.showMessageDialog(null, "No se pudo registrar esta venta",
                        "Error base datos", JOptionPane.ERROR_MESSAGE);
                } else{
                    JOptionPane.showMessageDialog(null, "Venta realizada",
                        "Venta exitosa", JOptionPane.INFORMATION_MESSAGE);
                    regresarVentana();
                }
            } catch (SQLException ex) {
                Logger.getLogger(VentaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            JOptionPane.showMessageDialog(null, "No se pudo registrar esta venta, no hay articulos seleccionados",
                        "Articulos no seleccionados", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private List<Articulo> obtenerArticulosBusqueda(){
        List<Articulo> articulosBusqueda = new ArrayList();
        ArticuloDAO articuloDAO = new ArticuloDAO();
        String nombre = txtSearchProducto.getText();
        try {
            articulosBusqueda = articuloDAO.getArticulosBusqueda(nombre);
        } catch (SQLException ex) {
            Logger.getLogger(VentaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return articulosBusqueda;
    }
    
    private void fillArticuloData(Articulo articuloBuscado){
        lblPrecio.setText(String.valueOf(articuloBuscado.getArticuloCatalogo().getPrecio()));
        lblProductoNombre.setText((articuloBuscado.getArticuloCatalogo().getNombre()));
        lblStock.setText(String.valueOf(articuloBuscado.getCantidad()));
        setSpinner(articuloBuscado.getCantidad());
    }
    
    private void addProductToVenta(){
        if(articulo.getId()!=ERROR){
            articulosList.add(articulo);
            if(articuloRegalado!=null){
                articuloRegalado.setCantidad(cantidadARegalar);
                articuloRegalado.getArticuloCatalogo().setPrecio(precioRegalo);
                articulosList.add(articuloRegalado);
            }
        }
    }
    
    private void fillVentaTable(){
        ventaListTable.add(new VentaTable(articulo.getArticuloCatalogo().getId(), articulo.getArticuloCatalogo().getNombre(), spnCantidad.getValue(), articulo.getArticuloCatalogo().getPrecio(), articulo.calcularSubTotal(spnCantidad.getValue())));
        tblVenta.setItems(ventaListTable);
        clmnTotal.setCellValueFactory(new PropertyValueFactory<VentaTable, Float>("totalPago"));
        clmnProducto.setCellValueFactory(new PropertyValueFactory<VentaTable, String>("nombreProducto"));
        clmnPrecio.setCellValueFactory(new PropertyValueFactory<VentaTable, Float>("precio"));
        clmnCodigo.setCellValueFactory(new PropertyValueFactory<VentaTable, String>("codigoProducto"));
        clmnCantidad.setCellValueFactory(new PropertyValueFactory<VentaTable, Integer>("cantidadProducto"));
    }
    
    
    private void setTotalPago(){
        totalPago = totalPago + articulo.calcularSubTotal(spnCantidad.getValue());
        lblTotalPago.setText(String.valueOf(totalPago));
    }
    
    private void setSpinner(int stock){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, stock, 1);
        spnCantidad.setValueFactory(valueFactory);
        spnCantidad.setEditable(true);
    }
    private void getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        lblFechaVenta.setText(formatter.format(currentDate));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getDate();
    }    
    
}
