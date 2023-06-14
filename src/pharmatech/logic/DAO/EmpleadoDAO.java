package pharmatech.logic.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import pharmatech.logic.DAO.contracts.IEmpleado;
import java.sql.SQLException;
import pharmatech.DataAccess.DataBaseManager;

/**
 *
 * @author miche
 */
public class EmpleadoDAO implements IEmpleado{
    private final int ERROR_ADDITION = -1;
    private final String LOGIN_QUERY = "SELECT * FROM pharmatech.empleado WHERE noPersonal = ? AND password = SHA1(?)";

    @Override
    public int login(String numPersonal, String password) throws SQLException {
    int response = ERROR_ADDITION;
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(LOGIN_QUERY);
        
        statement.setString(1, numPersonal);
        statement.setString(2, password);
        
        ResultSet loginResult = statement.executeQuery();
        
        if(loginResult.next()){
            response = 1;
        }
        
        DataBaseManager.close();
        return response;
    }
    
}
