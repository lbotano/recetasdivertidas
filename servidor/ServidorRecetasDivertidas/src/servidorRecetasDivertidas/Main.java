package servidorRecetasDivertidas;

import java.net.ServerSocket;
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
				pool.execute(new ThreadClient(cpds ,listener.accept()));
				
			} 
		}
	}
}
