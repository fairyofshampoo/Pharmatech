
package pharmatech.logic.DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pharmatech.DataAccess.DataBaseManager;
import pharmatech.logic.DAO.contracts.IDescuento;
import pharmatech.logic.Descuento;

/**
 *
 * @author Star3oy
 */
public class DescuentoDAO implements IDescuento{
    
    private final String ADD_DESCUENTO = "insert into pharmatech.descuentos (idarticulocatálogo, diaPromocion, porcentajeDescuento)"
            + "values (?,?,?)";
    private final String GET_ALL_DESCUENTOS = "Select * FROM pharmatech.descuentos";
    private final String MODIFY_DESCUENTOS = "UPDATE pharmatech.descuentos set idarticulocatálogo = ?, diaPromocion = ?, porcentajeDescuento = ? WHERE idDescuentos = ?";
    private final String DELETE_DESCUENTOS = "DELETE FROM pharmatech.descuentos WHERE idDescuentos = ?";
    private final String GET_DESCUENTO_PRODUCT_DAY = "SELECT * FROM pharmatech.descuentos WHERE idarticulocatálogo = ? AND diaPromocion = ?";
    private final int ERROR = -1;
    
    @Override
    public List<Descuento> getAllPromociones() throws SQLException {
        List<Descuento> listaDescuentos = new ArrayList<>();
        String query = GET_ALL_DESCUENTOS;
        Connection connection = DataBaseManager.getInstance(); 
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);              
            while(result.next()) {
                Descuento descuento = new Descuento();
                descuento.setIdDescuento(result.getInt("idDescuentos"));
                descuento.setIdArticuloCatalogo(result.getString("idarticulocatálogo"));
                descuento.setDiaPromocion(result.getInt("diaPromocion"));               
                descuento.setPorcentaje(result.getInt("porcentajeDescuento"));
                listaDescuentos.add(descuento);
            }
        DataBaseManager.close();
        return listaDescuentos;
    }

    @Override
    public int modificarDescuento(Descuento descuento, int idDescuento) throws SQLException {
              int result = 0;
     String query = MODIFY_DESCUENTOS;

      Connection connection = DataBaseManager.getInstance();
         PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, descuento.getIdArticuloCatalogo());
        statement.setInt(2, descuento.getDiaPromocion());
        statement.setInt(3, descuento.getPorcentaje());
        statement.setInt(4, idDescuento);
        result = statement.executeUpdate();
         if (result == 0) {
            throw new SQLException ("Error al modificar el descuento");
        }
         DataBaseManager.close();
     
     return result;    
    }

    @Override
    public int addDescuento(Descuento descuento) throws SQLException {
        int result;
        String query = ADD_DESCUENTO;
        Connection connection = DataBaseManager.getInstance();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, descuento.getIdArticuloCatalogo());
        statement.setInt(2, descuento.getDiaPromocion());
        statement.setInt(3, descuento.getPorcentaje());
        result = statement.executeUpdate();
        if (result == 0) {
            throw new SQLException ("Error al añadir descuento");
        } 
        DataBaseManager.close();
        return result;   
    }

    @Override
    public int deleteDescuento(int idDescuento) throws SQLException {
        int result;
        String query = DELETE_DESCUENTOS;
        Connection connection = DataBaseManager.getInstance();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, idDescuento);
        result = statement.executeUpdate();
        if (result == 0) {
            throw new SQLException ("Error al Eliminar descuento");
        } 
        DataBaseManager.close();
        return result;     
    }   

    @Override
    public Descuento getDescuento(int diaPromocion, String articulo) throws SQLException {
        Descuento descuento = new Descuento();
        descuento.setIdDescuento(ERROR);
        
        String query = GET_DESCUENTO_PRODUCT_DAY;
        Connection connection = DataBaseManager.getInstance();
        PreparedStatement statement = connection.prepareStatement(query);
        
        statement.setString(1, articulo);
        statement.setInt(2, diaPromocion);
        ResultSet result= statement.executeQuery();
        
        if(result.next()){
            descuento.setIdDescuento(result.getInt("idDescuentos"));
            descuento.setIdArticuloCatalogo(result.getString("idarticulocatálogo"));
            descuento.setDiaPromocion(result.getInt("diaPromocion"));
            descuento.setPorcentaje(result.getInt("porcentajeDescuento"));
        }
        
        DataBaseManager.close();
        return descuento;
    }
}
