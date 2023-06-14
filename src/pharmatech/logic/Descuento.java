package pharmatech.logic;

/**
 *
 * @author Star3oy
 */
public class Descuento {
    private int idDescuento;
    private String idArticuloCatalogo;
    private int diaPromocion;
    private int porcentaje;

    public int getIdDescuento() {
        return idDescuento;
    }

    public void setIdDescuento(int idDescuento) {
        this.idDescuento = idDescuento;
    }

    public String getIdArticuloCatalogo() {
        return idArticuloCatalogo;
    }

    public void setIdArticuloCatalogo(String idArticuloCatalogo) {
        this.idArticuloCatalogo = idArticuloCatalogo;
    }

    public int getDiaPromocion() {
        return diaPromocion;
    }

    public void setDiaPromocion(int diaPromocion) {
        this.diaPromocion = diaPromocion;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(int porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    
}
