package pharmatech.logic.DAO.contracts;

import java.sql.SQLException;
import java.util.List;
import pharmatech.logic.DosPorUno;

/**
 *
 * @author Star3oy
 */
public interface IDosPorUno {
    public List<DosPorUno> getAllPromociones() throws SQLException;
     public int modificarDescuento(DosPorUno dosPorUno, int idDosPorUno) throws SQLException;
     public int addDescuento(DosPorUno dosPorUno) throws SQLException;
     public int deleteDescuento(int idDescuento) throws SQLException;
     public DosPorUno getDosPorUno(int diaPromocion, String articulo) throws SQLException;
}
