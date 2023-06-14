package pharmatech.logic;


public class ArticuloCatalogo {
    private String id;
    private float precio;
    private String nombre;
    private String tipo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public boolean equals(Object object){
        if((object == null) || (object.getClass() != this.getClass())) {
            return false;
        } 
        final ArticuloCatalogo otherArticuloCatalogo = (ArticuloCatalogo) object;
       
        return (this.id == null? otherArticuloCatalogo.id == null : this.id.equals(otherArticuloCatalogo.id))
           && (this.precio == otherArticuloCatalogo.precio)
           && (this.nombre == null? otherArticuloCatalogo.nombre == null : this.nombre.equals(otherArticuloCatalogo.nombre));
    }
    @Override
    public String toString() {
        return this.getNombre();
    }
}