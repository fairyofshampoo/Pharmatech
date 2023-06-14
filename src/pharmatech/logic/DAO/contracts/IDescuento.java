package pharmatech.logic.DAO.contracts;

import java.sql.SQLException;
import java.util.List;
import pharmatech.logic.Descuento;


/**
 *
 * @author Star3oy
 */
public interface IDescuento {
     public List<Descuento> getAllPromociones() throws SQLException;
     public int modificarDescuento(Descuento descuento, int idDescuento) throws SQLException;
     public int addDescuento(Descuento descuento) throws SQLException;
     public int deleteDescuento(int idDescuento) throws SQLException;
     public Descuento getDescuento(int diaPromocion, String articulo) throws SQLException;
}
