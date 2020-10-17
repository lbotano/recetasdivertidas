package servidorRecetasDivertidas;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
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

	
	//llamadas a SPs
	//private static final String CONSRECETASCAT = "";
	//private static final String CONSRECETAING = "";
	//private static final String CONSRECETATEXT = "";
	//private static final String CALIFICAR = "";
	//private static final String DATOSRECETA = "{call spGetDatosReceta()}";
	//private static final String LISTARCATREC = "";
	//private static final String LISTARCATING = "";
	private static final String LOGIN = "{call spInicioSesion(?,?,?)}";
	//private static final String PREGUNTASSEG = "";
	private static final String REGISTRO = "{call spRegistroUsuario(?,?,?,?,?,?,?,?,?)}";
	private static final String SUBIRRECETA = "{call spSubirReceta(?,?,?,?,?,?}";
	
	public ThreadClient(ComboPooledDataSource c, Socket s) {
		ThreadClient.cpds = c;
		this.socket = s;
	}
	
	private void calificar() {
		
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
			//setea los parametros a partir del mensaje del cliente
			stmt.setString(1, message.get(1));
			stmt.setString(2, message.get(2));
			//setea los parametros de salida del sp
			stmt.registerOutParameter(3, Types.TINYINT);
			System.out.println("Sending LOGIN message to db for socket: " + socket);
			//ejecuta la consulta			
			stmt.execute();
			//si no hubo ningun error, entonces manda el siguiente mensaje
			answer.add("LOGINOK");
		} catch (SQLException e) {
			//si hubo un error y esta definido por los devs, se lo manda al cliente
			System.out.println("fail");
			answer.add("LOGINFAIL");
			if(e.getSQLState().contentEquals("45000")) {
        		answer.add(e.getMessage());
    		}else {
    			//sino, le manda el siguiente mensaje
    			answer.add("Error en la base de datos");
    		}
		}		
	}	
	
	private void preguntasSeg() {
		
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
			answer.add("REGISTERFAIL");
			if(e.getSQLState().contentEquals("45000")) {
        		answer.add(e.getMessage());
    		}else {
    			answer.add("Error en la base de datos");
    		}
		}
		 
	}
	
	private void subirReceta() {
		try {
			stmt = conn.prepareCall(SUBIRRECETA);
			ArrayList<Ingrediente> ing = new ArrayList<Ingrediente>();
			ArrayList<CategoriaReceta> catRec = new ArrayList<CategoriaReceta>();
			ArrayList<Multimedia> mult = new ArrayList<Multimedia>();
			int i;
			for (i = 1; i < 4; i++) {
				//nickname, nombre receta, descripcion
				stmt.setString(i, message.get(i));
			}
			//agregar ingredientes
			while(!(message.get(i).contentEquals("CATEGORIASRECETA"))){
				ing.add(new Ingrediente(Integer.parseInt(message.get(i)) ,Integer.parseInt(message.get(i+1)),message.get(i+2)));
				i+=3;
			}
			//agregar categorias de recetas
			while(!(message.get(i).contentEquals("INICIOMULTIMEDIA"))){
				catRec.add(new CategoriaReceta(Integer.parseInt(message.get(i))));
				i++;
			}	
			//agregar multimedia
			while(i < message.size()){
				mult.add(new Multimedia(message.get(i)));
				i++;
			}
			//cuarto elemento el json de ingredientes
		    stmt.setString(4,new Gson().toJson(ing));
			//cuarto elemento el json de categorias de recetas
		    stmt.setString(4,new Gson().toJson(catRec));
			//cuarto elemento el json de multimedia
		    stmt.setString(4,new Gson().toJson(mult));
		    
			stmt.execute();
			answer.add("SUBIRRECETAOK");
		}catch(SQLException e) {
			answer.add("SUBIRRECETAFAIL");
			if(e.getSQLState().contentEquals("45000")) {
        		answer.add(e.getMessage());
    		}else {
    			answer.add("Error en la base de datos");
    		}
		}
	}
	
	protected void opcionesCliente(String peticion) {
		//segun la peticion, ejecuta cierto metodo
		System.out.println(peticion);
        switch(peticion) {
        case "CALIFICAR":
        	calificar();
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
        case "LOGIN"://
        	login();
        	break;
        case "PREGUNTASSEG":
        	preguntasSeg();
        	break;
        case "REGISTRO":          		
    		registro();           		
    		break;
        case "SUBIRRECETA":
        	subirReceta();
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
			//switch de opcioens del cliente
	        opcionesCliente(message.get(0));
	        System.out.println(answer.get(0));
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
