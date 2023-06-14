package pharmatech.GUI.controllers;

public class TablaPromociones {
    int identificador;
    String articulo;
    String dia;

    public TablaPromociones(int identificador, String articulo, String dia) {
        this.identificador = identificador;
        this.articulo = articulo;
        this.dia = dia;
    }
    
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
    
    
}
