package pharmatech.logic.DAO.contracts;

import pharmatech.logic.Venta;
import java.sql.SQLException;
import pharmatech.logic.Articulo;

/**
 *
 * @author miche
 */
public interface IVenta {
    int addVenta(Venta venta) throws SQLException;
    int addVentaTransaction(Venta venta) throws SQLException;
    int updateProduct(Articulo articulo) throws SQLException;
    int addProductoToVenta(Articulo articulo, int idVenta) throws SQLException;
}
