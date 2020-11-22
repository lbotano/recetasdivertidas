package servidorRecetasDivertidas;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class ArrayListStringValidator {
	private ArrayList<String> stringsValidar;
	//cualquier caracter menos saltos de linea y puede aparecer un minimo y maximo de veces
	private final String patternDato = ".{%d,%d}";
	//caracter numerico de rango [0-9] y puede ocurrir un minimo o maximo de veces
	private final String patternNumero = "\\d{%d,%d}";
	//lo saque del codigo del cliente (camiloxd)
	private final String patternMail ="^[a-zA-Z0-9_+&*-]+(?:\\."+
								"[a-zA-Z0-9_+&*-]+)*@" +
								"(?:[a-zA-Z0-9-]+\\.)+[a-z" +
								"A-Z]{2,7}$";
	private final String patternURL = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

			
	
	
	public ArrayListStringValidator(ArrayList<String> a) {
		stringsValidar = a;
	}
	//Cliente
	//-----------------------------------------------------------------
	
	public boolean esSubirRecetaValido() {
		//si no tiene estos datos, la estructura del mensaje es invalida.
		if (!stringsValidar.contains("CATEGORIASRECETA") || !stringsValidar.contains("INICIOMULTIMEDIA")) {
			return false;
		}
		//minimo tiene que tener [SUBIRRECETA, rNombre, rDescripcion, iID, cantidad, unidad]
		if (stringsValidar.size() < 7) {
			return false;
		}
		boolean rNombre = vDato(stringsValidar.get(1), 1, 128);
		/*3 (instrucciones) y 4 (descripcion) no hace falta validarlos ya que son textos
		 * que ingresa el usuario y pueden ser cualquier caracter incluyendo Line terminators
		 * (saltos de linea, carriage-return, etc)
		 */
		if(!rNombre) return false;

		int i = 4;
		//validar los ingredientes
		while (!stringsValidar.get(i).contentEquals("CATEGORIASRECETA")) {
			//si alguno (idIngrediente, cantidad, unidad) tiene un formato invalido
			if(!vNum(stringsValidar.get(i),1,11) || !vNum(stringsValidar.get(i+1),1,11) || !vDato(stringsValidar.get(i+2),1,16)) {
				return false;
			}
			i+=3;
		}
		//Suma uno para saltarse el mensaje CATEGORIASRECETA
		i++;
		//validar las categorias de la receta
		while(!stringsValidar.get(i).contentEquals("INICIOMULTIMEDIA")) {
			if(!vNum(stringsValidar.get(i),1,11)) {
				return false;
			}
			i++;
		}
		//suma 1 para saltarse el mensaje iniciomultimedia
		i++;
		//validar la multimedia
		while(i < stringsValidar.size()) {
			if(!vURL(stringsValidar.get(i))) {
				return false;
			}
			i++;
		}
		//si nada tiro false, entonces esta todo valido
		return true;
	}
	
	public boolean esResgistroValido() {
		if(stringsValidar.size() != 9) {
			return false;
		}
		
		boolean nickname = vDato(stringsValidar.get(1), 3, 32);
		boolean preguntaSeg = vNum(stringsValidar.get(2), 1, 2);
		boolean respuestaSeg = vDato(stringsValidar.get(3), 1, 64);
		boolean uNombre = vDato(stringsValidar.get(4), 1, 50);
		boolean uApellido = vDato(stringsValidar.get(5), 1, 50);
		boolean uContra = vDato(stringsValidar.get(6), 8, 50);
		boolean uGenero = vNum(stringsValidar.get(7), 1, 1);
		boolean uMail = vMail(stringsValidar.get(8));
		
		return (nickname && preguntaSeg && respuestaSeg && uApellido && uNombre && uContra && uGenero && uMail);
	}
	
	public boolean esCalificarValido() {
		//primero se fija si hay algun elemento nulo
		if(stringsValidar.size() != 4) {
			return false;
		}
		boolean nickname = vDato(stringsValidar.get(1), 1, 32);
		boolean idReceta = vNum(stringsValidar.get(2), 1, 11);
		boolean calificacion = vNum(stringsValidar.get(3), 1, 2);
		//idReceta, nickname y calificacion
		return (idReceta && nickname && calificacion);

	}
	
	public boolean esCambiarContraValido() {
		if(stringsValidar.size() != 4) {
			return false;
		}
		boolean nickname = vDato(stringsValidar.get(1), 3, 32);
		boolean respuesta = vDato(stringsValidar.get(2), 1, 64);
		boolean contraNueva = vDato(stringsValidar.get(3), 8, 50);
		
		return (nickname && contraNueva && respuesta);
	}
	
	//Admin
	//-----------------------------------
	
	public boolean esSubirCatValido() {
		if(stringsValidar.size() != 2) {
			return false;
		}
		return vDato(stringsValidar.get(1),1,64);
	}
	
	public boolean esSubirIngValido() {
		if(stringsValidar.size() < 2) {
			return false;
		}
		int i = 2;
		while(i < stringsValidar.size()) {
			if(!vNum(stringsValidar.get(i), 1, 11)) {
				return false;
			}
			i++;
		}
		return true;
	}
	//General
	//-----------------------------------

	public boolean vMail(String s) {
		return Pattern.matches(patternMail, s);
	}
	public boolean vDato(String s, int min, int max) {
		return Pattern.matches(String.format(patternDato, min,max), s);
	}
	public boolean vNum(String s, int min, int max) {
		return Pattern.matches(String.format(patternNumero, min, max), s);
	}
	public boolean vURL(String s){
		return Pattern.matches(patternURL, s);
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
