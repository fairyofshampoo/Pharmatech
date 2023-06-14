package pharmatech.logic;

import java.util.Date;


public class Articulo {
    private int id;
    private int cantidad;
    private Date fechaCaducidad;
    private ArticuloCatalogo articuloCatalogo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public ArticuloCatalogo getArticuloCatalogo() {
        return articuloCatalogo;
    }

    public void setArticuloCatalogo(ArticuloCatalogo articuloCatalogo) {
        this.articuloCatalogo = articuloCatalogo;
    }
    
    @Override
    public boolean equals(Object object){
        if((object == null) || (object.getClass() != this.getClass())) {
            return false;
        } 
        final Articulo otherArticulo = (Articulo) object;
       
        return (this.id == otherArticulo.id)
           && (this.cantidad == otherArticulo.cantidad)
           && (this.fechaCaducidad == null? otherArticulo.fechaCaducidad == null : this.fechaCaducidad.equals(otherArticulo.fechaCaducidad));
    }
    @Override
    public String toString() {
        return articuloCatalogo != null ? articuloCatalogo.getNombre() : "";
    }
    public float calcularSubTotal(int cantidad){
        float subtotal = 0;
        subtotal = cantidad * this.articuloCatalogo.getPrecio();
        return subtotal;
    }

}