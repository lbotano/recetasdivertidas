package servidorRecetasDivertidas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Main {
	
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();	
	private static boolean stop = false;
	private static int adminPort = 6969;
	private static int clientPort = 7070;
	private static String jdbcUrl = "jdbc:mysql://%s:3306/RecetasDivertidasDB?serverTimezone=UTC";
	
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
		String pathConfig = "src/main/java/servidorRecetasDivertidas/config.conf";
		File configFile = new File (pathConfig);
		//true: no existia el archivo y se creo correctamente
		//false: existe el archivo
		if(configFile.createNewFile()) {
			FileWriter writer = new FileWriter(pathConfig);
			writer.write("IP: localhost\r\n" + 
						"Admin-port:\r\n" + 
						"Client-port:\r\n" + 
						"Mysql-user:\r\n" + 
						"Mysql-pass:");
			writer.close();
			System.out.println("New configuration file created");
		}else {
			try {
				cpds.setDriverClass( "com.mysql.jdbc.Driver" );     
				if(getDataFromConfig(pathConfig)) {
					Thread ServerAdmin = new Thread(new Runnable(){
						@Override
						public void run() {
							try (ServerSocket ss = new ServerSocket(adminPort)) {
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
							try (ServerSocket ss = new ServerSocket(clientPort)) {
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
				}else {
					System.out.println("Fail to start server");
				}
					
			}catch(Exception e) {
				System.out.println("Error trying to get data from configuration file");
				System.out.println(e.getMessage());
			}
		}
		
	}
	//adminorclient true: admin / adminorclient false: client
	private static boolean validarPuerto(String s, boolean adminorclient) {
		String patterPort = "\\d{1,5}";
		if(Pattern.matches(patterPort, s)){
			int port = Integer.parseInt(s);
			if(port >= 0 && port <= 65535) {
				if(adminorclient) {
					adminPort = port;
				}else {
					clientPort = port;
				}
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
	private static boolean getDataFromConfig(String pathConfig) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(pathConfig));
		String zeroTo255 = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";
		String patternIP = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
		String patternUserPass = ".+";

		String line, data, value;
		while((line = br.readLine()) != null) {
			//strip borra los whitespace
			data = line.substring(0, line.indexOf(":")).strip();			
			value = line.substring(data.length()+1).strip();
			
			switch (data) {
				case "IP":	
					if(Pattern.matches(patternIP, value) || value.contentEquals("localhost")) {
						jdbcUrl = String.format(jdbcUrl, value);
						cpds.setJdbcUrl(jdbcUrl);
						System.out.println("IP configured successfully!");
					}else {
						System.out.println("Wrong format in IP in configuration file");
						return false;
					}
					break;
				case "Admin-port":
					if(validarPuerto(value, true)) {
						System.out.println("Admin-port configured successfully!");
					}else {
						System.out.println("Wrong format in Admin-port in configuration file");
						return false;						
					}
					break;				
				case "Client-port":		
					if(validarPuerto(value, false)) {
						System.out.println("Client-port configured successfully!");
					}else {
						System.out.println("Wrong format in Client-port in configuration file");
						return false;						
					}
					break;				
				case "Mysql-user":			
					if(Pattern.matches(patternUserPass, value)) {

						System.out.println("Mysql-user configured successfully!");
						cpds.setUser(value);
					}else {
						System.out.println("Wrong format in Mysql-user in configuration file");
						return false;		
						
					}
					break;		
				case "Mysql-pass":				
					if(Pattern.matches(patternUserPass, value)) {

						System.out.println("Mysql-pass configured successfully!");
						cpds.setPassword(value);
					}else {
						System.out.println("Wrong format in Mysql-pass in configuration file");
						return false;								
					}
					break;	
				default:
					System.out.println("Unexpected value in configuration");
					return false;					
			}
			
		}
		return true;
		
	}
	
}