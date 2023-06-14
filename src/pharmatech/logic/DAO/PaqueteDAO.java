package pharmatech.logic.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pharmatech.DataAccess.DataBaseManager;
import pharmatech.logic.DAO.contracts.IPaquete;
import pharmatech.logic.Paquete;

/**
 *
 * @author Star3oy
 */
public class PaqueteDAO implements IPaquete{
    
   private final String ADD_PAQUETE = "insert into pharmatech.paquete (idArticuloCatalogoCompra, idArticuloCatalogoRegalo, idDiaPromocion)"
            + "values (?,?,?)";
    private final String GET_ALL_PAQUETES = "Select * FROM pharmatech.paquete";
    private final String MODIFY_PAQUETE = "UPDATE pharmatech.paquete set idArticuloCatalogoCompra = ?, idArticuloCatalogoRegalo = ?, idDiaPromocion = ? WHERE idpaquete = ?";
    private final String DELETE_PAQUETE = "DELETE FROM pharmatech.paquete WHERE idpaquete = ?";
    private final String GET_PAQUETE_PRODUCT_DAY = "SELECT * FROM pharmatech.paquete WHERE idArticuloCatalogoCompra = ? AND idDiaPromocion = ?";
    private final int ERROR = -1;
    
    @Override
    public List<Paquete> getAllPromociones() throws SQLException {
        List<Paquete> listaPaquete = new ArrayList<>();
        String query = GET_ALL_PAQUETES;
        Connection connection = DataBaseManager.getInstance(); 
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);              
            while(result.next()) {
                Paquete paquete = new Paquete();
                paquete.setIdPaquete(result.getInt("idpaquete"));
                paquete.setIdArticuloCatalogoCompra(result.getString("idArticuloCatalogoCompra"));
                paquete.setIdArticuloCatalogoRegalo(result.getString("idArticuloCatalogoRegalo"));
                paquete.setIdDiaPromocion(result.getInt("idDiaPromocion"));               
                listaPaquete.add(paquete);
            }
        DataBaseManager.close();
        return listaPaquete;    
    }

    @Override
    public int modificarDescuento(Paquete paquete, int idPaquete) throws SQLException {
        int result;
        String query = MODIFY_PAQUETE;
        Connection connection = DataBaseManager.getInstance();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, paquete.getIdArticuloCatalogoCompra());
        statement.setString(2, paquete.getIdArticuloCatalogoRegalo());
        statement.setInt(3, paquete.getIdDiaPromocion());
        statement.setInt(4, idPaquete);
        result = statement.executeUpdate();
        if (result == 0) {
            throw new SQLException ("Error al modificar paquete");
        } 
        DataBaseManager.close();
        return result;       
    }

    @Override
    public int addDescuento(Paquete paquete) throws SQLException {
        int result;
        String query = ADD_PAQUETE;
        Connection connection = DataBaseManager.getInstance();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, paquete.getIdArticuloCatalogoCompra());
        statement.setString(2, paquete.getIdArticuloCatalogoRegalo());
        statement.setInt(3, paquete.getIdDiaPromocion());
        result = statement.executeUpdate();
        if (result == 0) {
            throw new SQLException ("Error al añadir descuento");
        } 
        DataBaseManager.close();
        return result;       
    }

    @Override
    public int deleteDescuento(int idPaquete) throws SQLException {
        int result;
        String query = DELETE_PAQUETE;
        Connection connection = DataBaseManager.getInstance();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, idPaquete);
        result = statement.executeUpdate();
        if (result == 0) {
            throw new SQLException ("Error al Eliminar paquete");
        } 
        DataBaseManager.close();
        return result;         }

    @Override
    public Paquete getPaquete(int diaPromocion, String articulo) throws SQLException {
        Paquete paquete = new Paquete();
        String query = GET_PAQUETE_PRODUCT_DAY;
        Connection connection = DataBaseManager.getInstance();
        PreparedStatement statement = connection.prepareStatement(query);
        
        statement.setString(1, articulo);
        statement.setInt(2, diaPromocion);
        
        ResultSet result= statement.executeQuery();
        paquete.setIdPaquete(ERROR);

        if (result.next()) {
            paquete.setIdPaquete(result.getInt("idpaquete"));
            paquete.setIdArticuloCatalogoCompra(result.getString("idArticuloCatalogoCompra"));
            paquete.setIdArticuloCatalogoRegalo(result.getString("idArticuloCatalogoRegalo"));
            paquete.setIdDiaPromocion(result.getInt("idDiaPromocion"));
        }
        DataBaseManager.close();
        return paquete;
    }
    
}
