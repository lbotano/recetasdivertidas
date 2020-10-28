package servidorRecetasDivertidas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Main {
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();
	
	private static boolean stop = false;
	
	private static synchronized void stop() {
		stop = true;
	}
	private static synchronized void start() {
		stop = false;
	}
	
	private static synchronized boolean isStopping() {
		return stop;
	}

	public static void main(String[] args) throws Exception {
		cpds.setDriverClass( "com.mysql.jdbc.Driver" );           
		cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/RecetasDivertidasDB?serverTimezone=UTC" );
		cpds.setUser("recetasdivertidas");        
		cpds.setPassword("camilo200");  
		
		Thread ServerAdmin = new Thread(new Runnable(){
			@Override
			public void run() {
				try (ServerSocket ss = new ServerSocket(6969)) {
					System.out.println("Admin server running ");
					
					var pool = Executors.newFixedThreadPool(10);
					while(true) {
						if(isStopping()) {
							break;
						}
						pool.execute(new ThreadAdmin(cpds , ss.accept()));
					}
				} catch (IOException e) {
					System.out.println("Failed to initiate admin server");
				}	
			}
		});
		
		ServerAdmin.setPriority(Thread.MAX_PRIORITY -1);
		
		Thread ServerClient = new Thread(new Runnable(){
			@Override
			public void run() {
				try (ServerSocket ss = new ServerSocket(7070)) {
					System.out.println("Client server running ");
					var pool = Executors.newFixedThreadPool(50);
					while(true) {
						if(isStopping()) {
							break;
						}
						pool.execute(new ThreadClient(cpds , ss.accept()));
					}
				} catch (IOException e) {
					System.out.println("Failed to initiate client server");
				}
			}
		});
		
		Thread ServerConsole = new Thread(new Runnable() {			
			public void run() {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
					String input = "";
					while (true) {						
						input = br.readLine();
						System.out.println("input : " + input);
					}					
				} catch (IOException e) {
					System.out.println("Failed to initiate console");
				}
			}			
		});
		ServerConsole.setPriority(Thread.MAX_PRIORITY);
		
		ServerConsole.start();
		ServerAdmin.start();
		ServerClient.start();
		

	}
}
