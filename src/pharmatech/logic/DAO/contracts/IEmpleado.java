package pharmatech.logic.DAO.contracts;

import java.sql.SQLException;

/**
 *
 * @author miche
 */
public interface IEmpleado {
    int login(String numPersonal, String password) throws SQLException;
}
