package servidorRecetasDivertidas;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import com.google.gson.*;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ThreadClient implements Runnable{
	//referencia al objeto que maneja las conexiones a la base de datos
	protected static ComboPooledDataSource cpds;
	//objeto que maneja el enlace con el cliente
	protected Socket socket;
	//objetos que guardan los mensajes que se transmiten
	protected ArrayList<String> answer;
	protected ArrayList<String> message;
	//objeto para llamar a store procedures
	protected CallableStatement stmt;
	//objeto para guardar la conexion a la base de datos
	protected Connection conn;
	//objetos para pasar mensajes
	protected ObjectOutputStream output;
	protected ObjectInputStream input;
	protected StringValidator sv;

	
	//llamadas a SPs
	//private static final String CONSRECETASCAT = "";
	//private static final String CONSRECETAING = "";
	//private static final String CONSRECETATEXT = "";
	private static final String CALIFICAR = "{call spCalificarReceta(?,?,?)}";
	//private static final String DATOSRECETA = "{call spGetDatosReceta()}";
	//private static final String LISTARCATREC = "";
	//private static final String LISTARCATING = "";
	private static final String LOGIN = "{call spInicioSesion(?,?,?,?)}";
	//private static final String PREGUNTASSEG = "";
	private static final String RECETASDEUSUARIO = "{call spGetRecetasUsuario(?)}";
	private static final String REGISTRO = "{call spRegistroUsuario(?,?,?,?,?,?,?,?,?)}";
	private static final String SUBIRRECETA = "{call spSubirReceta(?,?,?,?,?,?,?}";
	private static final String DefaultSQLErrorMsg = "Error en la base de datos";
	
	public ThreadClient(ComboPooledDataSource c, Socket s) {
		ThreadClient.cpds = c;
		this.socket = s;
	}
	/*
	 * Cuando se ejecuta un sp puede tirar un error, este metodo recibe el SQLException
	 * y el mensaje de error que se la va a comunicar al cliente si el error de sql
	 * es 45000 (estos son los mensajes de error definidos por lauti pro gamer), si no lo es
	 * entonces le manda el 'DefaultSQLErrorMsg'
	 */
	
	protected void exceptionHandler(SQLException e, String failMsg) {
		answer.add(failMsg);
		if(e.getSQLState().contentEquals("45000")) {
    		answer.add(e.getMessage());
		}else {
			answer.add(DefaultSQLErrorMsg);
		}
	}
	
	private void calificar() {
		try {
			//define que utiliza el sp de calificar
			stmt = conn.prepareCall(CALIFICAR);
			//establece los parametros segun lo que haya enviado el cliente
			stmt.setString(1, message.get(1));
			stmt.setString(2, message.get(2));
			stmt.setString(3, message.get(3));
			//ejecuta el sp
			stmt.execute();
			//si sql no tira ningun error, significa que se pudo calificar correctamente
			answer.add("CALIFICAOK");
		} catch (SQLException e) {
			exceptionHandler(e, "CALIFICARFAIL");
		}
	}
	
	private void consRecetasCat() {
		
	}
	
	private void consRecetasIng() {
		
	}
	
	private void consRecetasText() {
		
	}
	
	private void datosReceta() {
		
	}
	
	private void listarCatIng() {
		
	}
	
	private void listarCatRec() {
		
	}
	
	private void login() {
		try {
			//establece q codigo sql va a ejecutar
			stmt = conn.prepareCall(LOGIN);
			/*setea los parametros a partir del mensaje del cliente
			 * 1: nickname 2: contraseña 3: resultado 4: esAdmin
			 * 3 y 4 son parametros de salida
			 */			
			stmt.setString(1, message.get(1));
			stmt.setString(2, message.get(2));
			
			stmt.registerOutParameter(3, Types.BOOLEAN);
			stmt.registerOutParameter(4, Types.BOOLEAN);
			
			//ejecuta la consulta			
			stmt.execute();
			/*Si pudo logearse entonces manda un LOGINOK
			 * y si es admin o no (true: si)
			 * Se fija por el 3er parametro que es el resultado
			 * si es false cagaada xd
			 */
			if(stmt.getBoolean(3)) {
				answer.add("LOGINOK");
				answer.add(String.valueOf(stmt.getBoolean(4)));
			}else {
				answer.add("LOGINFAIL");
				answer.add("cagada");
			}
		} catch (SQLException e) {	
			exceptionHandler(e, "LOGINFAIL");	
		}		
	}	
	
	private void preguntasSeg() {
		
	}
	
	private void recetaUsuario() {
		try {
			stmt = conn.prepareCall(RECETASDEUSUARIO);
			//pone de parametro del sp el nickname recibido del cliente
			stmt.setString(1, message.get(1));
			stmt.execute();

			ResultSet resultadoSP = stmt.getResultSet();
			//next = false: resultset vacio
			//next = true: resultset con datos, mueve el cursor a la fila 1.
			if(resultadoSP.next()) {

				answer.add("RECETASDEUSUARIOOK");
				do {
					//agarra los elementos de la fila y los pone en el aeeay de respuesta
					answer.add(resultadoSP.getString(1));
					answer.add(resultadoSP.getString(2));
					answer.add(resultadoSP.getString(3));		
				//Mueve el cursor a la siguiente fila, si hay mas resultados devuelve true
				} while (resultadoSP.next());
			}else {
				answer.add("RECETASDEUSUARIONULL");				
			}
		
		}catch (SQLException e) {
			exceptionHandler(e, "RECETASDEUSUARIOFAIL");	
		}
	}
	
	private void registro() {
		try {
			stmt = conn.prepareCall(REGISTRO); 
			//poner en el statement todos los parametros de registro
    		for (int i = 1; i < message.size(); i++) {
    			if (i == 2 || i == 7) {
    				stmt.setInt(i, Integer.parseInt(message.get(i)));
    			}else {
            		stmt.setString(i, message.get(i));            				
    			}
    	
			}
    		stmt.registerOutParameter(9, Types.BOOLEAN);
    		System.out.println("Sending REGISTER message to db for socket: " + socket);
    		stmt.execute();
    		answer.add("REGISTEROK");
		} catch (SQLException e) {
			exceptionHandler(e, "REGISTERFAIL");
		}
		 
	}
	
	private void subirReceta() {
		try {
			stmt = conn.prepareCall(SUBIRRECETA);
			ArrayList<Ingrediente> ing = new ArrayList<Ingrediente>();
			ArrayList<Categoria> catRec = new ArrayList<Categoria>();
			ArrayList<Multimedia> mult = new ArrayList<Multimedia>();
			int i;
			for (i = 1; i < 5; i++) {
				//1: nickname, 2: nombre receta, 3: descripcion, 4: instrucciones
				stmt.setString(i, message.get(i));
			}
			
			//agregar ingredientes
			while(!(message.get(i).contentEquals("CATEGORIASRECETA"))){
				ing.add(new Ingrediente(Integer.parseInt(message.get(i)) ,Integer.parseInt(message.get(i+1)),message.get(i+2)));
				i+=3;
			}
			//agregar categorias de recetas
			while(!(message.get(i).contentEquals("INICIOMULTIMEDIA"))){
				catRec.add(new Categoria(Integer.parseInt(message.get(i))));
				i++;
			}	
			//agregar multimedia
			while(i < message.size()){
				mult.add(new Multimedia(message.get(i)));
				i++;
			}
			//cuarto elemento el json de ingredientes
		    stmt.setString(5,new Gson().toJson(ing));
			//cuarto elemento el json de multimedia
		    stmt.setString(6,new Gson().toJson(mult));
			//cuarto elemento el json de categorias de recetas
		    stmt.setString(7,new Gson().toJson(catRec));
		    
			stmt.execute();
			answer.add("SUBIRRECETAOK");
		}catch(SQLException e) {
			exceptionHandler(e, "SUBIRRECETAFAIL");
		}
	}
	
	protected void opcionesCliente(String peticion) {
		//segun la peticion, ejecuta cierto metodo
        switch(peticion) {
        case "CALIFICAR"://
        	if(sv.esCalificarValido()) {
        		calificar();
        	}else {
        		answer.add("FORMATERROR");
        	}
        	break;
        case "CONSRECETASCAT": 
        	consRecetasCat();
        	break;
        case "CONSRECETASING":
        	consRecetasIng();
        	break;
        case "CONSRECETASTEXT":
        	consRecetasText();
        	break;
        case "DATOSRECETA":
        	datosReceta();
        	break;
        case "LISTARCATING":
        	listarCatIng();
        	break;
        case "LISTARCATREC":
        	listarCatRec();
        	break;
        case "LOGIN":
        	login();
        	break;
        case "PREGUNTASSEG":
        	preguntasSeg();
        	break;
        case "RECETASDEUSUARIO":
        	recetaUsuario();
        	break;
        case "REGISTRO":   //       	
        	if(sv.esResgistroValido()) {     		
        		registro();     
        	}else {
        		answer.add("FORMATERROR");
        	}
    		break;
        case "SUBIRRECETA"://  	
        	if(sv.esSubirRecetaValido()) { 
        		subirReceta();    
        	}else {
        		answer.add("FORMATERROR");
        	}
        	break;
    	default:
    		//se manda esta respuesta si la peticion es invalida
    		answer.add("MESSAGEERROR");
        }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {	
			System.out.println("Connected with client" + this.socket);
			//inicializacion de los atributos
			this.conn = cpds.getConnection();		    
	        this.output = new ObjectOutputStream(this.socket.getOutputStream());
	        this.input = new ObjectInputStream(this.socket.getInputStream());
	        this.answer = new ArrayList<String>();	        
	        //recibe el mensaje del cliente
			this.message = (ArrayList<String>) this.input.readObject();
			sv = new StringValidator(message);
			if(sv.elementArrayListBlank(message)) {
				answer.add("ELEMENTBLANK");
			}else {
				//switch de opcioens del cliente
		        opcionesCliente(message.get(0));
			}
	        //una vez ejecutados los metodos correspondientes, manda la respuesta
    		output.writeObject(answer);
    		//vacia el objeto para la proxima respuesta
			answer.clear();
			
		}catch (Exception e){
        	System.out.println();
        	System.out.println("Client error: " + e.getMessage() + " in socket: " + socket);
		}finally {
	        try {
	        	this.output.close();
	        	this.input.close();
	        	this.conn.close();
	            this.socket.close();
	        } catch (Exception e) {
	        	System.out.println();
	        	System.out.println("Client error: " + e.getMessage() + " in socket: " + socket);
	        }
	        System.out.println("Closed: " + socket );
	        System.out.println();
		}
	}
}
