package jsonClasess;

public class Ingrediente {
	private int iID;
	private int cantidad;
	private String unidadCantidad;
	
	public Ingrediente(int i, int c, String u) {
		iID = i;
		cantidad = c;
		unidadCantidad = u;
	}
	@Override
	public String toString() {
		return "id: "+iID+", cantidad: " + cantidad + ", unidad: " + unidadCantidad;
	}
}
