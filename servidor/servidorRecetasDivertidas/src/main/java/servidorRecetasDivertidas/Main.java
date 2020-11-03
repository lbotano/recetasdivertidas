package servidorRecetasDivertidas;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import threadsMain.ThreadConsole;
import threadsMain.ThreadServer;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
	
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();	
	private static String jdbcUrl = "jdbc:mysql://%s:3306/RecetasDivertidasDB?serverTimezone=UTC";
	

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
		cpds.setDriverClass( "com.mysql.cj.jdbc.Driver" );
		cpds.setTestConnectionOnCheckout(true);
		// Iniciar el server

		try {
			ThreadServer admin = new ThreadServer(true, cpds, archivoConfig.getPuertoAdmin(), 5);
			Thread ServerAdmin = new Thread(admin);
			ServerAdmin.setPriority(Thread.MAX_PRIORITY -1);
			
			ThreadServer client = new ThreadServer(false, cpds, archivoConfig.getPuertoCliente(), 10);
			Thread ServerClient = new Thread(client);
			
			ServerAdmin.start();
			ServerClient.start();			
			
			Thread ServerConsole = new Thread(new ThreadConsole(cpds, admin, client));		
			ServerConsole.setPriority(Thread.MAX_PRIORITY);
			
			ServerConsole.start();

		}catch(Exception e) {
			System.out.println("Could not start server");
			System.out.println(e.getMessage());
		}
	}
}