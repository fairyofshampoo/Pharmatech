package pharmatech.logic.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pharmatech.DataAccess.DataBaseManager;
import pharmatech.logic.DAO.contracts.IDosPorUno;
import pharmatech.logic.DosPorUno;

/**
 *
 * @author Star3oy
 */
public class DosPorUnoDAO implements IDosPorUno{

     private final String ADD_DOS_POR_UNO = "insert into pharmatech.dosporuno (idarticuloCatalogo, idDiaPromocion, cantidadCompra, cantidadRegalo)"
            + "values (?,?,?,?)";
    private final String GET_ALL_DOS_POR_UNO = "Select * FROM pharmatech.dosporuno";
    private final String MODIFY_DOS_POR_UNO = "UPDATE pharmatech.dosporuno set idarticuloCatalogo = ?, idDiaPromocion = ?, cantidadCompra = ?, cantidadRegalo = ? WHERE idDosPorUno = ?";
    private final String DELETE_DOS_POR_UNO = "DELETE FROM pharmatech.dosporuno WHERE idDosPorUno = ?";
    private final String GET_DOS_POR_UNO_PRODUCT_DAY = "SELECT * FROM pharmatech.dosporuno WHERE idarticuloCatalogo = ? AND idDiaPromocion = ?";
    private final int ERROR = -1;
    
    @Override
    public List<DosPorUno> getAllPromociones() throws SQLException {
        List<DosPorUno> listaDosPorUno = new ArrayList<>();
        String query = GET_ALL_DOS_POR_UNO;
        Connection connection = DataBaseManager.getInstance(); 
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);              
            while(result.next()) {
                DosPorUno dosPorUno = new DosPorUno();
                dosPorUno.setIdDosPorUno(result.getInt("idDosPorUno"));
                dosPorUno.setIdAriculoCatalogo(result.getString("idarticulocatalogo"));
                dosPorUno.setIdDiaPromocion(result.getInt("idDiaPromocion"));               
                dosPorUno.setCantidadCompra(result.getInt("cantidadCompra"));
                dosPorUno.setCantidadCompra(result.getInt("cantidadRegalo"));
                listaDosPorUno.add(dosPorUno);
            }
        DataBaseManager.close();
        return listaDosPorUno;    
    }

    @Override
    public int modificarDescuento(DosPorUno dosPorUno, int idDosPorUno) throws SQLException {
        int result;
        String query = MODIFY_DOS_POR_UNO;
        Connection connection = DataBaseManager.getInstance();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, dosPorUno.getIdAriculoCatalogo());
        statement.setInt(2, dosPorUno.getIdDiaPromocion());
        statement.setInt(3, dosPorUno.getCantidadCompra());
        statement.setInt(4, dosPorUno.getCantidadRegalo());
        statement.setInt(5, idDosPorUno);
        result = statement.executeUpdate();
        if (result == 0) {
            throw new SQLException ("Error al modificar Promocion");
        } 
        DataBaseManager.close();
        return result;      }

    @Override
    public int addDescuento(DosPorUno dosPorUno) throws SQLException {
        int result;
        String query = ADD_DOS_POR_UNO;
        Connection connection = DataBaseManager.getInstance();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, dosPorUno.getIdAriculoCatalogo());
        statement.setInt(2, dosPorUno.getIdDiaPromocion());
        statement.setInt(3, dosPorUno.getCantidadCompra());
        statement.setInt(4, dosPorUno.getCantidadRegalo());
        result = statement.executeUpdate();
        if (result == 0) {
            throw new SQLException ("Error al añadir promocion");
        } 
        DataBaseManager.close();
        return result;       
    }

    @Override
    public int deleteDescuento(int idDescuento) throws SQLException {
        int result;
        String query = DELETE_DOS_POR_UNO;
        Connection connection = DataBaseManager.getInstance();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, idDescuento);
        result = statement.executeUpdate();
        if (result == 0) {
            throw new SQLException ("Error al Eliminar descuento");
        } 
        DataBaseManager.close();
        return result;      }

    @Override
    public DosPorUno getDosPorUno(int diaPromocion, String articulo) throws SQLException {
        DosPorUno dosPorUno = new DosPorUno();
        String query = GET_DOS_POR_UNO_PRODUCT_DAY;
        Connection connection = DataBaseManager.getInstance();
        PreparedStatement statement = connection.prepareStatement(query);
        
        statement.setString(1, articulo);
        statement.setInt(2, diaPromocion);
        
        ResultSet result= statement.executeQuery();
        dosPorUno.setIdDosPorUno(ERROR);

        if (result.next()) {
            dosPorUno.setIdDosPorUno(result.getInt("idDosPorUno"));
            dosPorUno.setIdAriculoCatalogo(result.getString("idArticuloCatalogo"));
            dosPorUno.setIdDiaPromocion(result.getInt("idDiaPromocion"));
            dosPorUno.setCantidadCompra(result.getInt("cantidadCompra"));
            dosPorUno.setCantidadRegalo(result.getInt("cantidadRegalo"));
        }
        DataBaseManager.close();
        return dosPorUno;
    }
     
}
