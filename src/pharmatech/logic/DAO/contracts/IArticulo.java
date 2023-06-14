package pharmatech.logic.DAO.contracts;


import java.sql.SQLException;
import java.util.List;
import pharmatech.logic.Articulo;
import pharmatech.logic.ArticuloCatalogo;

public interface IArticulo {
    Articulo getArticulo(String idArticuloCatalogo) throws SQLException;
    ArticuloCatalogo getArticuloCatalogo(String idArticuloCatalogo) throws SQLException;
    Articulo getArticuloInventario(int idArticulo) throws SQLException;
    int addArticuloInventario(Articulo articulo) throws SQLException;
    int updateArticuloInventario(Articulo articulo) throws SQLException;
    int deleteArticuloInventario(int idArticulo) throws SQLException;
    List<Articulo> getAllArticulos() throws SQLException;
    List<ArticuloCatalogo> getAllArticuloCatalogo() throws SQLException;
    List<Articulo> getArticulosBusqueda(String nombre)throws SQLException;
    List<ArticuloCatalogo> getArticulosCatalogoByName(String nombre) throws SQLException;
    String getCodigoByName(ArticuloCatalogo articulo) throws SQLException;
}