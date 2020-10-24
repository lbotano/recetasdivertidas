package jsonClasess;

public class Multimedia {
	private String link;
	
	public Multimedia(String l) {
		link = l;  
	}
	@Override
	public String toString() {
		return "link: "+link;
	}
}
