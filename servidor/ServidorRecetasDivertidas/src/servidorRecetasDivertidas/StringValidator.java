package servidorRecetasDivertidas;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class StringValidator {
	ArrayList<String> alValidar;
	/*
	 * String con caracteres de a-z o A-Z o 0-9 o espacios en blanco (\\s)
	 * que pueden ocurrir un minimo (primer parametro) y maximo (segundo parametro)
	 * de veces.
	 */
	//Pattern.matches(String.format(patternNumero, rangMin, rangMax), s);
	//Pattern.matches(String.format(patternDato, rangMin, rangMax), s);
	private String patternDato = "[a-zA-Z0-9\\s]{%d,%d}";

	//caracter numerico de rango [0-9] y puede ocurrir un minimo o maximo de veces
	private String patternNumero = "\\d{%d,%d}";
	//lo saque del codigo del cliente (camiloxd)
	private String patternMail ="^[a-zA-Z0-9_+&*-]+(?:\\."+
								"[a-zA-Z0-9_+&*-]+)*@" +
								"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
								"A-Z]{2,7}$";

	public StringValidator(ArrayList<String> a) {
		alValidar = a;
	}
	
	public boolean esResgistroValido() {
		if(alValidar.size() != 8) {
			return false;
		}

		boolean nickname = Pattern.matches(String.format(patternDato, 3, 32), alValidar.get(1));
		boolean preguntaSeg = Pattern.matches(String.format(patternNumero, 1, 2), alValidar.get(2));
		boolean respuestaSeg = Pattern.matches(String.format(patternNumero, 1, 64), alValidar.get(3));
		boolean uNombre = Pattern.matches(String.format(patternNumero, 1, 50), alValidar.get(4));
		boolean uApellido = Pattern.matches(String.format(patternNumero, 1, 50), alValidar.get(5));
		boolean uContra = Pattern.matches(String.format(patternNumero, 8, 50), alValidar.get(6));
		boolean uGenero = Pattern.matches(String.format(patternDato, 1, 1), alValidar.get(7));
		boolean uMail = Pattern.matches(patternMail, alValidar.get(8));
		
		if (nickname && preguntaSeg && respuestaSeg && uApellido && uNombre && uContra && uGenero && uMail) {
			return true;
		}
		
		return false;
	}
	
	public boolean esCalificarValido() {
		//primero se fija si hay algun elemento nulo
		if(alValidar.size() != 4) {
			return false;
		}		
		boolean idReceta = Pattern.matches(String.format(patternNumero, 1, 11), alValidar.get(1));
		boolean nickname = Pattern.matches(String.format(patternDato, 1, 32), alValidar.get(2));
		boolean calificacion = Pattern.matches(String.format(patternDato, 1, 2), alValidar.get(3));
		//idReceta, nickname y calificacion
		if(idReceta && nickname && calificacion) {
			return true;
		}
		return false;

	}
	private boolean vMail(String s) {
		return Pattern.matches(patternMail, s);
	}
	
	public boolean elementArrayListBlank(ArrayList<String> al) {
		int i = 1;
		boolean blank = false;
		//recorre el arraylist buscando si algun elemento es nulo o tiene espacios en blanco
		while(i < al.size() && !blank) {
			blank = al.get(i).isBlank();
			i++;
		}		
		return blank;
	}
}
