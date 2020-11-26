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
		String pathConfig = "./config.properties";
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
		cpds.setMinPoolSize(2);
		cpds.setMaxPoolSize(4);
		cpds.setPreferredTestQuery("SELECT 1");
		cpds.setAcquireRetryAttempts(2);
		cpds.setAcquireRetryDelay(500);

		// Iniciar el server
        Connection c = null;
		try {
            System.out.println("Testing database connection...");

            c = cpds.getConnection();
            c.close();
            System.out.println("Database OK");

			ThreadServer admin = new ThreadServer(true, cpds, archivoConfig.getPuertoAdmin(), 3);
			Thread ServerAdmin = new Thread(admin);
			ServerAdmin.setPriority(Thread.MAX_PRIORITY -1);
			
			ThreadServer client = new ThreadServer(false, cpds, archivoConfig.getPuertoCliente(), 3);
			Thread ServerClient = new Thread(client);
			
			ServerAdmin.start();
			ServerClient.start();			
			
			Thread ServerConsole = new Thread(new ThreadConsole(cpds, admin, client));		
			ServerConsole.setPriority(Thread.MAX_PRIORITY);
			
			ServerConsole.start();

		}catch(Exception e) {
			System.out.println("Could not start server: " + e.getMessage());
			e.printStackTrace();
		}finally {
		    try{
                if(c != null){
                    c.close();
                }
            }catch(SQLException e){
                System.out.println("Error while trying to close testeing connection");
            }
        }

	}
}