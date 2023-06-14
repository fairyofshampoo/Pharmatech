package pharmatech.logic.DAO;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import pharmatech.DataAccess.DataBaseManager;
import pharmatech.logic.Articulo;
import pharmatech.logic.DAO.contracts.IVenta;
import pharmatech.logic.Venta;

/**
 *
 * @author miche
 */
public class VentaDAO implements IVenta{
    private final int ERROR = -1;
    private final String ADD_VENTA_COMMAND = "INSERT INTO pharmatech.venta (fechaVenta, totalPagar) VALUES (?, ?)";
    private final String UPDATE_PRODUCT = "UPDATE pharmatech.artículo SET cantidad = cantidad - ? WHERE idArtículo = ?";
    private final String ADD_PRODUCT_TO_VENTA = "INSERT INTO pharmatech.articulo_venta (idVenta, idArticulo, cantidad, subTotal) VALUES (?,?,?,?)";

    @Override
    public int addVenta(Venta venta) throws SQLException {
        int result = ERROR;
        String query = ADD_VENTA_COMMAND;
    
        DataBaseManager.getInstance().setAutoCommit(false);
        PreparedStatement ventaStatement = DataBaseManager.getInstance().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        
        ventaStatement.setDate(1, new java.sql.Date(venta.getFechaVenta().getTime()));
        ventaStatement.setFloat(2, venta.getTotalPago());
    
        ventaStatement.executeUpdate();
        ResultSet ventaResult = ventaStatement.getGeneratedKeys();
        
        while(ventaResult.next()){
                result = ventaResult.getInt(1);
            }
        
        return result;
    }

    @Override
    public int addVentaTransaction(Venta venta) throws SQLException {
        int result = ERROR;
        
        try {
            DataBaseManager.getInstance().setAutoCommit(false);
            result = addVenta(venta);
            venta.setIdVenta(result);
            
            
            for(int i = 0; i < venta.getArticulos().size(); i++){
                result += updateProduct(venta.getArticulos().get(i));
                result += addProductoToVenta(venta.getArticulos().get(i), venta.getIdVenta());
            }
            
            DataBaseManager.getInstance().commit();
        } catch (SQLException ex) {
            Logger.getLogger(VentaDAO.class.getName()).log(Level.SEVERE, null, ex);
            DataBaseManager.getInstance().rollback();
        } finally {
            DataBaseManager.getInstance().close();
        }
        
        return result;
    }

    @Override
    public int updateProduct(Articulo articulo) throws SQLException {
        int result = ERROR;
        String query = UPDATE_PRODUCT;
    
        DataBaseManager.getInstance().setAutoCommit(false);
        PreparedStatement productoStatement = DataBaseManager.getInstance().prepareStatement(query);
        
        productoStatement.setInt(1, articulo.getCantidad());
        productoStatement.setInt(2, articulo.getId());
    
        result = productoStatement.executeUpdate();
    
        return result;
    }

    @Override
    public int addProductoToVenta(Articulo articulo, int idVenta) throws SQLException {
        int result = ERROR;
        String query = ADD_PRODUCT_TO_VENTA;
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(query);

        statement.setInt(1, idVenta);
        statement.setInt(2, articulo.getId());
        statement.setInt(3, articulo.getCantidad());
        statement.setFloat(4, articulo.getArticuloCatalogo().getPrecio());

        result = statement.executeUpdate();

        return result;
    }
    
    
}
