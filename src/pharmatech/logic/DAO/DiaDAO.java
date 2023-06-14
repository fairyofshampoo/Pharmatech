package pharmatech.logic.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pharmatech.DataAccess.DataBaseManager;
import pharmatech.logic.DAO.contracts.IDia;
import pharmatech.logic.Dia;

public class DiaDAO implements IDia{
    private final String GET_DIAS_SEMANA = "SELECT * FROM  pharmatech.dias";
    
    @Override
    public List<Dia> getDias() throws SQLException {
           List<Dia> listaDias = new ArrayList<>();
        String query = GET_DIAS_SEMANA;
        Connection connection = DataBaseManager.getInstance(); 
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(query);              
            while(result.next()) {
                Dia dia = new Dia();
                dia.setIdDia(result.getInt("idDia"));
                dia.setDia(result.getString("dia"));
                listaDias.add(dia);
            }
        DataBaseManager.close();
        return listaDias; 
    }
    
}
