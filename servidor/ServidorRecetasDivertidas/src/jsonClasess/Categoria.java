package jsonClasess;

public class Categoria {
	private int cID;
	
	public Categoria(int c) {
		cID = c;
	}
	
	@Override
	public String toString() {
		return "cID: "+cID;
	}
}
