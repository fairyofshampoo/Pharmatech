package pharmatech.logic.DAO;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pharmatech.DataAccess.DataBaseManager;
import pharmatech.logic.Articulo;
import pharmatech.logic.ArticuloCatalogo;
import pharmatech.logic.DAO.contracts.IArticulo;


public class ArticuloDAO implements IArticulo {
    private final int ERROR = -1;
    private final String CODIGO_ERROR = "000";
    private final String ADD_ARTICULO_INVENTARIO = "INSERT INTO pharmatech.artículo(cantidad, fechaCaducidad, idArtículoCatálogo) VALUES (?,?,?)";
    private final String GET_ALL_ARTICULOS_INVENTARIO = "SELECT idArtículo, cantidad, fechaCaducidad, artículocatálogo.idArtículoCatálogo, artículocatálogo.precio, artículocatálogo.nombre, artículocatálogo.tipo "
        + "FROM pharmatech.artículo INNER JOIN pharmatech.artículocatálogo ON artículo.idArtículoCatálogo = artículocatálogo.idArtículoCatálogo";
    private final String GET_ARTICULO_BY_CODIGO = "SELECT idArtículo, cantidad, fechaCaducidad, artículocatálogo.idArtículoCatálogo, artículocatálogo.precio, artículocatálogo.nombre, artículocatálogo.tipo "
        + "FROM pharmatech.artículo INNER JOIN pharmatech.artículocatálogo ON artículo.idArtículoCatálogo = artículocatálogo.idArtículoCatálogo "
        + "WHERE artículocatálogo.idArtículoCatálogo = ? AND fechaCaducidad > CURDATE() AND cantidad > 0";
    private final String GET_ARTICULO_CATALOGO = "SELECT idArtículoCatálogo, nombre, precio, tipo FROM pharmatech.artículocatálogo WHERE idArtículoCatálogo = ?";
    private final String GET_ALL_ARTICULOS_CATALOGO = "SELECT * FROM pharmatech.artículocatálogo";
    private final String UPDATE_ARTICULO_INVENTARIO = "UPDATE pharmatech.artículo SET cantidad = ?, fechaCaducidad = ? WHERE idArtículo = ?";
    private final String DELETE_ARTICULO_INVENTARIO = "DELETE FROM pharmatech.artículo WHERE idArtículo = ?";
    private final String GET_ARTICULO_BY_ID = "SELECT idArtículo, cantidad, fechaCaducidad, artículocatálogo.idArtículoCatálogo, artículocatálogo.precio, artículocatálogo.nombre, artículocatálogo.tipo "
        + "FROM pharmatech.artículo INNER JOIN pharmatech.artículocatálogo ON artículo.idArtículoCatálogo = artículocatálogo.idArtículoCatálogo WHERE artículo.idArtículo = ?";
    private final String GET_ARTICULOS_BY_NOMBRE = "SELECT artículo.idArtículo, artículo.cantidad, artículo.fechaCaducidad, artículocatálogo.idArtículoCatálogo, artículocatálogo.precio, artículocatálogo.nombre, artículocatálogo.tipo "
        + "FROM pharmatech.artículo INNER JOIN pharmatech.artículocatálogo ON artículo.idArtículoCatálogo = artículocatálogo.idArtículoCatálogo WHERE artículocatálogo.nombre LIKE ?";
    private final String GET_CODIGO_BY_NAME = "SELECT artículocatálogo.idArtículoCatálogo FROM pharmatech.artículocatálogo WHERE artículocatálogo.nombre = ?";
    private final String GET_ARTICULOS_CATALOGO_BY_NOMBRE = "SELECT * FROM pharmatech.artículocatálogo WHERE artículocatálogo.nombre LIKE ?";

    @Override
    public Articulo getArticulo(String idArticuloCatalogo) throws SQLException {
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(GET_ARTICULO_BY_CODIGO);

        statement.setString(1, idArticuloCatalogo);

        ResultSet articuloResult = statement.executeQuery();
        
        Articulo articulo = new Articulo();
        
        
        if(articuloResult.next()){
            ArticuloCatalogo articuloCatalogo = new ArticuloCatalogo();
            articulo.setId(articuloResult.getInt("idArtículo"));
            articulo.setCantidad(articuloResult.getInt("cantidad"));
            articulo.setFechaCaducidad(articuloResult.getDate("fechaCaducidad"));
            articuloCatalogo.setId(articuloResult.getString("idArtículoCatálogo"));
            articuloCatalogo.setNombre(articuloResult.getString("nombre"));
            articuloCatalogo.setPrecio(articuloResult.getFloat("precio"));
            articulo.setArticuloCatalogo(articuloCatalogo);
        } else{
            articulo.setId(ERROR);
        }

        DataBaseManager.close();

        return articulo;
    }

    @Override
    public ArticuloCatalogo getArticuloCatalogo(String idArticuloCatalogo) throws SQLException {
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(GET_ARTICULO_CATALOGO);

        statement.setString(1, idArticuloCatalogo);

        ResultSet articuloResult = statement.executeQuery();
        
        ArticuloCatalogo articuloCatalogo = new ArticuloCatalogo();
        
        
        if(articuloResult.next()){
            articuloCatalogo.setId(articuloResult.getString("idArtículoCatálogo"));
            articuloCatalogo.setNombre(articuloResult.getString("nombre"));
            articuloCatalogo.setPrecio(articuloResult.getFloat("precio"));
            articuloCatalogo.setTipo(articuloResult.getString("tipo"));
        } else{
            articuloCatalogo.setId(CODIGO_ERROR);
        }

        DataBaseManager.close();

        return articuloCatalogo;
    }

    @Override
    public int addArticuloInventario(Articulo articulo) throws SQLException {
        int response = ERROR;
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(ADD_ARTICULO_INVENTARIO);
        
        statement.setInt(1, articulo.getCantidad());
        statement.setDate(2, (Date) articulo.getFechaCaducidad());
        statement.setString(3, articulo.getArticuloCatalogo().getId());
        
        response = statement.executeUpdate();
        
        DataBaseManager.close();
        return response;
    }
    
    @Override
    public List<Articulo> getAllArticulos() throws SQLException{
        List<Articulo> listaArticulos = new ArrayList<>();
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(GET_ALL_ARTICULOS_INVENTARIO);
        ResultSet articulosResult = statement.executeQuery();
        Articulo articulo = new Articulo();
        ArticuloCatalogo articuloCatalogo = new ArticuloCatalogo();
        boolean registerExisted = false;
       
        while(articulosResult.next()){
            articulo.setId(articulosResult.getInt("idArtículo"));
            articulo.setCantidad(articulosResult.getInt("cantidad"));
            articulo.setFechaCaducidad(articulosResult.getDate("fechaCaducidad"));
            articuloCatalogo.setId(articulosResult.getString("idArtículoCatálogo"));
            articuloCatalogo.setNombre(articulosResult.getString("nombre"));
            articuloCatalogo.setPrecio(articulosResult.getFloat("precio"));
            articuloCatalogo.setTipo(articulosResult.getString("tipo"));
            articulo.setArticuloCatalogo(articuloCatalogo);
            listaArticulos.add(articulo);
            registerExisted = true;
        }
        if(!registerExisted){
            articulo.setId(ERROR);
            listaArticulos.add(articulo);
        }
        
        DataBaseManager.close();
        
        return listaArticulos;
    }
    @Override
    public List<ArticuloCatalogo> getAllArticuloCatalogo() throws SQLException {
  List<ArticuloCatalogo> listaArticulosCatalogo = new ArrayList<>();
        String query = GET_ALL_ARTICULOS_CATALOGO;
        Statement statement = DataBaseManager.getInstance().createStatement();
        ResultSet articulosResult = statement.executeQuery(query); 
       while(articulosResult.next()){
           ArticuloCatalogo articuloCatalogo = new ArticuloCatalogo();
                articuloCatalogo.setId(articulosResult.getString("idArtículoCatálogo"));
                articuloCatalogo.setNombre(articulosResult.getString("nombre"));
                articuloCatalogo.setPrecio(articulosResult.getFloat("precio")); 
                listaArticulosCatalogo.add(articuloCatalogo);
            }
        DataBaseManager.close();
        return listaArticulosCatalogo;    
    }

    @Override
    public Articulo getArticuloInventario(int idArticulo) throws SQLException {
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(GET_ARTICULO_BY_ID);

        statement.setInt(1, idArticulo);

        ResultSet articuloResult = statement.executeQuery();
        
        Articulo articulo = new Articulo();
        
        
        if(articuloResult.next()){
            ArticuloCatalogo articuloCatalogo = new ArticuloCatalogo();
            articulo.setId(articuloResult.getInt("idArtículo"));
            articulo.setCantidad(articuloResult.getInt("cantidad"));
            articulo.setFechaCaducidad(articuloResult.getDate("fechaCaducidad"));
            articuloCatalogo.setId(articuloResult.getString("idArtículoCatálogo"));
            articuloCatalogo.setNombre(articuloResult.getString("nombre"));
            articuloCatalogo.setPrecio(articuloResult.getFloat("precio"));
            articuloCatalogo.setTipo(articuloResult.getString("tipo"));
            articulo.setArticuloCatalogo(articuloCatalogo);
        } else{
            articulo.setId(ERROR);
        }

        DataBaseManager.close();

        return articulo;
    }

    @Override
    public int updateArticuloInventario(Articulo articulo) throws SQLException {
        int response = ERROR;
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(UPDATE_ARTICULO_INVENTARIO);
        statement.setInt(1, articulo.getCantidad());
        statement.setDate(2, (Date) articulo.getFechaCaducidad());
        statement.setInt(3, articulo.getId());
        
        response = statement.executeUpdate();
        
        DataBaseManager.close();
        return response;
    }

    @Override
    public int deleteArticuloInventario(int idArticulo) throws SQLException {
        int response = ERROR;
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(DELETE_ARTICULO_INVENTARIO);
        statement.setInt(1, idArticulo);
        
        response = statement.executeUpdate();
        
        DataBaseManager.close();
        return response;
    }

    @Override
    public List<Articulo> getArticulosBusqueda(String nombre) throws SQLException {
        List<Articulo> listaArticulos = new ArrayList<>();
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(GET_ARTICULOS_BY_NOMBRE);
        String nombreBusqueda = nombre + "%";
        statement.setString(1, nombreBusqueda);

        ResultSet articuloResult = statement.executeQuery();
        
        
        while(articuloResult.next()){
            Articulo articulo = new Articulo();
            ArticuloCatalogo articuloCatalogo = new ArticuloCatalogo();
            articulo.setId(articuloResult.getInt("idArtículo"));
            articulo.setCantidad(articuloResult.getInt("cantidad"));
            articulo.setFechaCaducidad(articuloResult.getDate("fechaCaducidad"));
            articuloCatalogo.setId(articuloResult.getString("idArtículoCatálogo"));
            articuloCatalogo.setNombre(articuloResult.getString("nombre"));
            articuloCatalogo.setPrecio(articuloResult.getFloat("precio"));
            articulo.setArticuloCatalogo(articuloCatalogo);
            listaArticulos.add(articulo);
        }
        DataBaseManager.close();

        return listaArticulos;
    }
    
    @Override
    public String getCodigoByName(ArticuloCatalogo articulo) throws SQLException{
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(GET_CODIGO_BY_NAME);
        statement.setString(1, articulo.getNombre());
        ResultSet result = statement.executeQuery();
        String codigo = "000";
        
        if(result.next()){
            codigo = result.getString("idArtículoCatálogo");
        }
        
        return codigo;
    }
    
    @Override
    public List<ArticuloCatalogo> getArticulosCatalogoByName(String nombre) throws SQLException{
        List<ArticuloCatalogo> listaArticulos = new ArrayList<>();
        PreparedStatement statement = DataBaseManager.getInstance().prepareStatement(GET_ARTICULOS_CATALOGO_BY_NOMBRE);
        String nombreBusqueda = nombre + "%";
        statement.setString(1, nombreBusqueda);

        ResultSet articuloResult = statement.executeQuery();
        
        while(articuloResult.next()){
            ArticuloCatalogo articuloCatalogo = new ArticuloCatalogo();
            articuloCatalogo.setId(articuloResult.getString("idArtículoCatálogo"));
            articuloCatalogo.setNombre(articuloResult.getString("nombre"));
            articuloCatalogo.setPrecio(articuloResult.getFloat("precio"));
            articuloCatalogo.setTipo(articuloResult.getString("tipo"));
            listaArticulos.add(articuloCatalogo);
        }
        
        DataBaseManager.close();
        
        return listaArticulos;
    }
}

