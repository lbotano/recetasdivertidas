package servidorRecetasDivertidas;

public class CategoriaReceta {
	private int cID;
	
	public CategoriaReceta(int c) {
		cID = c;
	}
	
	@Override
	public String toString() {
		return "cID: "+cID;
	}
}
