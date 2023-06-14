
package pharmatech.logic.DAO.contracts;

import java.util.List;
import pharmatech.logic.Dia;
import java.sql.SQLException;

/**
 *
 * @author Star3oy
 */
public interface IDia {
    public List<Dia> getDias() throws SQLException;
}
