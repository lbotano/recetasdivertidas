package servidorRecetasDivertidas;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Main {
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();

	public static void main(String[] args) throws Exception {
		
		try (ServerSocket listener = new ServerSocket(6969)) {
			System.out.println("Server running...");
			var pool = Executors.newFixedThreadPool(50);
			cpds.setDriverClass( "com.mysql.jdbc.Driver" );           
			cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/RecetasDivertidasDB?serverTimezone=UTC" );
			cpds.setUser("root");        
			cpds.setPassword("");  
			
			while (true) {
				pool.execute(new Server(listener.accept()));
				
			} 
		}
	}
	private static class Server implements Runnable{
		private Socket socket;
		private Connection conn;
		private ArrayList<String> answer;
		private CallableStatement stmt;

		//Strings para los callableStatement, es decir para llamar a las sp.
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
		
		public Server (Socket socket) {
			this.socket = socket;
		}
		@Override
		public void run() {
			try {
				System.out.println("Connected with client" + socket);
				this.conn = cpds.getConnection();
			    System.out.println("Database connected for " + socket);
			    System.out.println();
	            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
	            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
	            answer = new ArrayList<String>();
	            
	            @SuppressWarnings("unchecked")
				ArrayList<String> message = (ArrayList<String>) input.readObject();
	            
    			answer.clear();
	            
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
            		stmt = conn.prepareCall(LOGIN);
            		stmt.setString(1, message.get(1));
            		stmt.setString(2, message.get(2));
            		stmt.registerOutParameter(3, Types.TINYINT);
            		
            		System.out.println("Sending LOGIN message to db for socket: " + socket);
            		stmt.execute();
            		if(stmt.getInt(3) == 0) {
	            		answer.add("LOGINFAIL");
	            		answer.add("usuario no existe");
            		}else if(stmt.getInt(3) == 1) {
            			answer.add("LOGINOK");
            		}else {
            			answer.add("LOGINFAIL");
            			answer.add("contraseña incorrecta");
            		}
            		output.writeObject(answer);
	            	break;
	            case "PREGUNTASSEG":
	            	break;
	            case "REGISTRO":
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
            		
            		try {
                		stmt.execute();
                		answer.add("REGISTEROK");
					} catch (SQLException e) {
	            		answer.add("REGISTERFAIL");
	            		answer.add(e.getMessage());
					}
            		
            		output.writeObject(answer);
            		break;
	            case "SUBIRRECETA":
	            	break;
            	default:
            		answer.clear();
            		answer.add("MESSAGEERROR");
            		output.writeObject(answer);
	            }
	            
			}catch (Exception e){
				System.out.println(e);
				System.out.println(e.getMessage());
			}finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                System.out.println("Closed: " + socket );
			}
		}
	}
}
