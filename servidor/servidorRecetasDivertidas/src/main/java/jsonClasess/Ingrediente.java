package jsonClasess;

public class Ingrediente {
	private int id;
	private int cantidad;
	private String unidad;
	
	public Ingrediente(int i, int c, String u) {
		id = i;
		cantidad = c;
		unidad = u;
	}
	@Override
	public String toString() {
		return "id: "+id+", cantidad: " + cantidad + ", unidad: " + unidad;
	}
}
