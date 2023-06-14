package pharmatech.logic;

/**
 *
 * @author Star3oy
 */
public class Dia {
    private int idDia;
    private String dia;

    public int getIdDia() {
        return idDia;
    }

    public void setIdDia(int idDia) {
        this.idDia = idDia;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
    
    @Override
    public String toString() {
        return this.getDia();
    }
    
}
