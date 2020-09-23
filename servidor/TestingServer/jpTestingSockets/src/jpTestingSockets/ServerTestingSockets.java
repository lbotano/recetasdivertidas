package jpTestingSockets;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.Executors;


public class ServerTestingSockets {
	
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();
	
	public static void main(String[] args) throws Exception {

		cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver            
		cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/RecetasDivertidasDB?serverTimezone=UTC" );
		cpds.setUser("root");        
		cpds.setPassword("");  
		// the settings below are optional -- c3p0 can work with defaults
		cpds.setMinPoolSize(5);                                     
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);
	
		try (ServerSocket listener = new ServerSocket(59898)) {
			System.out.println("Server running...");
			var pool = Executors.newFixedThreadPool(20);
			while (true) {
				pool.execute(new Server(listener.accept()));
				
			} 
		}
	}
	 
	private static class Server implements Runnable{
		private Socket socket;
		private Connection con;
		private ResultSet rs;
		private PreparedStatement pstm;
		
		public Server (Socket socket) {
			this.socket = socket;
		}
		@Override
		public void run() {
			try {
				System.out.println("Connected with client");
				this.con = cpds.getConnection();
			    System.out.println("Database connected!");

				Scanner in = new Scanner(socket.getInputStream());
	            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	            String msg;
	            
	            while(in.hasNextLine()) {
	            	msg = in.nextLine();
	            	if(msg.contentEquals("exit")) {
            			out.println("Leaving..." + socket);
            			System.out.println("Leaving..." + socket);
	            		
	            	}
	            	switch(msg){
	            		case "sp":
	            			out.println("Calling store procedure...");
	            			System.out.println("Calling store procedure...");
	            			
	            			this.pstm = con.prepareStatement("CALL spTestingSockets();");
	            			this.rs = pstm.executeQuery();

	            			out.println("Values: ");
	            			System.out.println("Values: ");
	            			while(rs.next()) {
	            				System.out.println("Sending data to socket: " + socket);
	            				out.println(rs.getInt(1));
	            				Thread.sleep(150);
	            			}
	            			System.out.println("Completed!");
	            			pstm.close();
	            			rs.close();
	            			break;
	            		default:
	            			out.println("Type 'exit' or 'sp' ");
	            	}	            	
	            }
			} catch (Exception e) {
				System.out.println("Error: " + socket+ " " + e.getMessage());
			} finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                System.out.println("Closed: " + socket );
            }

		}	 
	}
}
