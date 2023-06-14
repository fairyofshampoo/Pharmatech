package pharmatech.DataAccess;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;


public class DataBaseManager {

    private static Connection connection;
    private static final String URL_PROPERTY_FIELD = "mysql.db.url";
    private static final String USER_PROPERTY_FIELD = "mysql.db.user";
    private static final String PASSWORD_PROPERTY_FIELD = "mysql.db.password";

    private DataBaseManager() {
    }

    private static Connection getConnection() throws SQLException {
        Connection newConnection = null;
        Properties properties = new DataBaseManager().getPropertiesFile();
        if (properties != null) {
            newConnection = DriverManager.getConnection(
                    properties.getProperty(URL_PROPERTY_FIELD),
                    properties.getProperty(USER_PROPERTY_FIELD),
                    properties.getProperty(PASSWORD_PROPERTY_FIELD));

        } else {
            throw new SQLException("No fue posible encontrar las credenciales de la base de datos");
        }
        return newConnection;
    }

    public static Connection getInstance() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Error en base de datos");
        }
        return connection;
    }

    public static boolean close() throws SQLException {
        boolean isClosed = false;
        try {
            if (connection != null) {
                connection.close();
            }
            isClosed = true;
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Lo sentimos, algo va mal con el sistema");
        }
        return isClosed;
    }

    private Properties getPropertiesFile() {
        Properties properties = null;
        try {
            InputStream file = new FileInputStream("src/pharmatech/DataAccess/databaseconfig.properties");
            if (file != null) {
                properties = new Properties();
                properties.load(file);
            }
            file.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataBaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return properties;
    }

}
