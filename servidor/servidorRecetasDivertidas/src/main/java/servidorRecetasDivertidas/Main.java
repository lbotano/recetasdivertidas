package servidorRecetasDivertidas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Main {
	
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();	
	private static boolean stop = false;
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
		ArchivoConfiguracion archivoConfig = new ArchivoConfiguracion(pathConfig);

		if (archivoConfig.getTuvoQueCrearArchivo()) {
			System.out.println("Se creó el archivo de configuración en: " + pathConfig);
			System.out.println("Configúrelo y vuelva a iniciar el servidor.");
			return;
		}

		// Valida los datos del archivo
		if (!archivoConfig.validarDatos()) {
			System.out.println("Cambie el archivo de configuración y vuelva a intentarlo.");
			return;
		}

		// Aplica los datos del archivo como parámetros para iniciar el servidor
		jdbcUrl = String.format(jdbcUrl, archivoConfig.getIp());
		cpds.setJdbcUrl(jdbcUrl);
		cpds.setUser(archivoConfig.getUsuarioMysql());
		cpds.setPassword(archivoConfig.getPassMysql());


		// Iniciar el server
		cpds.setDriverClass( "com.mysql.jdbc.Driver" );
		Thread ServerAdmin = new Thread(new Runnable(){
			@Override
			public void run() {
				try (ServerSocket ss = new ServerSocket(archivoConfig.getPuertoAdmin())) {
					System.out.println("Admin server running ");

					var pool = Executors.newFixedThreadPool(10);
					while(!isStopping()) {
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
				try (ServerSocket ss = new ServerSocket(archivoConfig.getPuertoCliente())) {
					System.out.println("Client server running ");
					var pool = Executors.newFixedThreadPool(50);
					while(!isStopping()) {
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