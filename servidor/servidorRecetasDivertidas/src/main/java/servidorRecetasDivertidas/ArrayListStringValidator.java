package servidorRecetasDivertidas;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class ArrayListStringValidator {
	private ArrayList<String> alValidar;
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
		alValidar = a;
	}
	//Cliente
	//-----------------------------------------------------------------
	
	public boolean esSubirRecetaValido() {
		//si no tiene estos datos, la estructura del mensaje es invalida.
		if(!alValidar.contains("CATEGORIASRECETA") || !alValidar.contains("INICIOMULTIMEDIA")) {
			return false;
		}
		//minimo tiene que tener [SUBIRRECETA, nickname, rNombre, rDescripcion, iID, cantidad, unidad]
		if(alValidar.size() < 8) {
			return false;
		}
		boolean nickname = vDato(alValidar.get(1), 3, 32);
		boolean rNombre = vDato(alValidar.get(2), 1, 128);
		/*3 (instrucciones) y 4 (descripcion) no hace falta validarlos ya que son textos
		 * que ingresa el usuario y pueden ser cualquier caracter incluyendo Line terminators
		 * (saltos de linea, carriage-return, etc)
		 */
		if(!nickname || !rNombre) {
			return false;
		}
		int i = 5;
		//validar los ingredientes
		while(!alValidar.get(i).contentEquals("CATEGORIASRECETA")) {
			//si alguno (idIngrediente, cantidad, unidad) tiene un formato invalido
			if(!vNum(alValidar.get(i),1,11) || !vNum(alValidar.get(i+1),1,11) || !vDato(alValidar.get(i+2),1,16)) {
				return false;
			}
			i+=3;
		}
		//Suma uno para saltarse el mensaje CATEGORIASRECETA
		i++;
		//validar las categorias de la receta
		while(!alValidar.get(i).contentEquals("INICIOMULTIMEDIA")) {
			if(!vNum(alValidar.get(i),1,11)) {
				return false;
			}
			i++;
		}
		//suma 1 para saltarse el mensaje iniciomultimedia
		i++;
		//validar la multimedia
		while(i < alValidar.size()) {
			if(!vURL(alValidar.get(i))) {
				return false;
			}
			i++;
		}
		//si nada tiro false, entonces esta todo valido
		return true;
	}
	
	public boolean esResgistroValido() {
		if(alValidar.size() != 9) {
			return false;
		}
		
		boolean nickname = vDato(alValidar.get(1), 3, 32);
		boolean preguntaSeg = vNum(alValidar.get(2), 1, 2);
		boolean respuestaSeg = vDato(alValidar.get(3), 1, 64);
		boolean uNombre = vDato(alValidar.get(4), 1, 50);
		boolean uApellido = vDato(alValidar.get(5), 1, 50);
		boolean uContra = vDato(alValidar.get(6), 8, 50);
		boolean uGenero = vNum(alValidar.get(7), 1, 1);
		boolean uMail = vMail(alValidar.get(8));
		
		return (nickname && preguntaSeg && respuestaSeg && uApellido && uNombre && uContra && uGenero && uMail);
	}
	
	public boolean esCalificarValido() {
		//primero se fija si hay algun elemento nulo
		if(alValidar.size() != 4) {
			return false;
		}
		boolean nickname = vDato(alValidar.get(1), 1, 32);
		boolean idReceta = vNum(alValidar.get(2), 1, 11);
		boolean calificacion = vNum(alValidar.get(3), 1, 2);
		//idReceta, nickname y calificacion
		return (idReceta && nickname && calificacion);

	}
	
	public boolean esCambiarContraValido() {
		if(alValidar.size() != 4) {
			return false;
		}
		boolean nickname = vDato(alValidar.get(1), 3, 32);
		boolean respuesta = vDato(alValidar.get(2), 1, 64);
		boolean contraNueva = vDato(alValidar.get(3), 8, 50);
		
		return (nickname && contraNueva && respuesta);
	}
	
	//Admin
	//-----------------------------------
	
	public boolean esSubirCatValido() {
		if(alValidar.size() != 2) {
			return false;
		}
		return vDato(alValidar.get(1),1,64);
	}
	
	public boolean esSubirIngValido() {
		if(alValidar.size() < 2) {
			return false;
		}
		int i = 2;
		while(i < alValidar.size()) {
			if(!vNum(alValidar.get(i), 1, 11)) {
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
