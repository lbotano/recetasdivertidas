package servidorRecetasDivertidas;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ThreadClient implements Runnable{
	private static ComboPooledDataSource cpds;
	private Socket socket;
	private ArrayList<String> answer;
	private ArrayList<String> message;
	private CallableStatement stmt;
	private Connection conn;
	
	private static final String CONSRECETASCAT = "";
	private static final String CONSRECETAING = "";
	private static final String CONSRECETATEXT = "";
	private static final String CALIFICAR = "";
	private static final String DATOSRECETA = "";
	private static final String LISTARCATREC = "";
	private static final String LISTARCATING = "";
	private static final String LOGIN = "{call spInicioSesion(?,?,?)}";
	private static final String PREGUNTASSEG = "";
	private static final String REGISTRO = "{call spRegistroUsuario(?,?,?,?,?,?,?,?,?)}";
	private static final String SUBIRRECETA = "";
	
	public ThreadClient(ComboPooledDataSource c, Socket s) {
		ThreadClient.cpds = c;
		this.socket = s;
	}
	
	synchronized private void login() {
		try {
			stmt = conn.prepareCall(LOGIN);
			stmt.setString(1, message.get(1));
			stmt.setString(2, message.get(2));
			stmt.registerOutParameter(3, Types.TINYINT);
			
			System.out.println("Sending LOGIN message to db for socket: " + socket);
			stmt.execute();
			answer.add("LOGINOK");
		} catch (SQLException e) {
			answer.add("LOGINFAIL");
			if(e.getSQLState().contentEquals("45000")) {
        		answer.add(e.getMessage());
    		}else {
    			answer.add("Error en la base de datos");
    		}
		}		
	}

	synchronized private void registro() {
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
	
			System.out.println("Connected with client" + socket);
			this.conn = cpds.getConnection();
		    System.out.println("Database connected for " + socket);
		    
	        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
	        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
	        //array list que guarda la respuesta que genera el servidor
	        answer = new ArrayList<String>();	        
	        //array list que tiene el mensaje del cliente
	        //recibe el mensaje del cleinte
			this.message = (ArrayList<String>) input.readObject();
	        
	        //segun la peticion, ejecuta cierto metodo
	        switch(message.get(0)) {
	        case "CONSRECETASCAT": 
	        	break;
	        case "CONSRECETAING":
	        	break;
	        case "CONSRECETATEXT":
	        	break;
	        case "CALIFICAR":
	        	break;
	        case "DATOSRECETA":
	        	break;
	        case "LISTARCATREC":
	        	break;
	        case "LISTARCATING":
	        	break;
	        case "LOGIN":
	        	login();
	        	break;
	        case "PREGUNTASSEG":
	        	break;
	        case "REGISTRO":          		
	    		registro();           		
	    		break;
	        case "SUBIRRECETA":
	        	break;
	    	default:
	    		answer.add("MESSAGEERROR");
	        }
	        //una vez ejecutados los metodos correspondientes, manda la respuesta
    		output.writeObject(answer);
    		//vacia el objeto para la proxima respuesta
			answer.clear();
			
		}catch (Exception e){
			System.out.println(e);
			System.out.println(e.getMessage());
		}finally {
	
	        try {
	            socket.close();
	        } catch (IOException e) {
	        }
	        System.out.println("Closed: " + socket );
	        System.out.println();
		}		
	}
}
