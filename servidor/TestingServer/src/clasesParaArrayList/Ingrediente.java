package clasesParaArrayList;

public class Ingrediente {
    private int iID;
    private int cantidad;
    private String unidad;

    public Ingrediente(int i, int c, String u){
        iID = i;
        cantidad = c;
        unidad = u;
    }

    public int getiID() {
        return iID;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
}
