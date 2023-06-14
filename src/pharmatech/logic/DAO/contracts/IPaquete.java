package pharmatech.logic.DAO.contracts;

import java.sql.SQLException;
import java.util.List;
import pharmatech.logic.Paquete;

/**
 *
 * @author Star3oy
 */
public interface IPaquete {
     public List<Paquete> getAllPromociones() throws SQLException;
     public int modificarDescuento(Paquete paquete, int idPaquete) throws SQLException;
     public int addDescuento(Paquete paquete) throws SQLException;
     public int deleteDescuento(int idPaquete) throws SQLException;
     public Paquete getPaquete(int diaPromocion, String articulo) throws SQLException;
}
