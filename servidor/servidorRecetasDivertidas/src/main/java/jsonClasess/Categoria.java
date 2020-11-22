package jsonClasess;

public class Categoria {
	private int cID;
	
	public Categoria(int c) {
		this.cID = c;
	}
	
	@Override
	public String toString() {
		return "cID: " + cID;
	}
}
