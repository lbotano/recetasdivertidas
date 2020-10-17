package servidorRecetasDivertidas;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Main {
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();

	public static void main(String[] args) throws Exception {
		cpds.setDriverClass( "com.mysql.jdbc.Driver" );           
		cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/RecetasDivertidasDB?serverTimezone=UTC" );
		cpds.setUser("root");        
		cpds.setPassword("");  
		
		Thread ServerAdmin = new Thread(new Runnable(){
			@Override
			public void run() {
				try (ServerSocket ss = new ServerSocket(6969)) {
					System.out.println("Admin server running ");
					var pool = Executors.newFixedThreadPool(10);	
					while(true) {
						pool.execute(new ThreadAdmin(cpds , ss.accept()));
					}
				} catch (IOException e) {
				}
				
			}
			
		});
		
		ServerAdmin.setPriority(Thread.MAX_PRIORITY);
		
		Thread ServerClient = new Thread(new Runnable(){
			@Override
			public void run() {
				try (ServerSocket ss = new ServerSocket(7070)) {
					System.out.println("Client server running ");
					var pool = Executors.newFixedThreadPool(50);	
					while(true) {
						pool.execute(new ThreadAdmin(cpds , ss.accept()));
					}
				} catch (IOException e) {
				}
				
			}
			
		});
		
		ServerAdmin.start();
		ServerClient.start();
		
		/*
		try (ServerSocket listener = new ServerSocket(6969)) {
			System.out.println("Server running...");
			var pool = Executors.newFixedThreadPool(50);			
			while (true) {
				pool.execute(new ThreadAdmin(cpds ,listener.accept()));
				
			} 
		}*/
	}
	

}
