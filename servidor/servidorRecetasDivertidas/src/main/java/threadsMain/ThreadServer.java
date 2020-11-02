package threadsMain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import servidorRecetasDivertidas.ThreadAdmin;
import servidorRecetasDivertidas.ThreadClient;

public class ThreadServer implements Runnable{
	private String name;
	private boolean type;
	private int port;
	private ExecutorService pool;
	private int sizeThreadPool;
	private ComboPooledDataSource cpds;
	private ServerSocket ss;

	//typeServer true = admin  false = client
	public ThreadServer(boolean typeServer, ComboPooledDataSource combo, int port, int sizePool) {
		this.type = typeServer;
		if(type){
			this.name = "Admin";
		}else{
			this.name = "Client";
		}
		this.cpds = combo;
		this.port = port;
		this.sizeThreadPool = sizePool;
	}
	
	@Override
	public void run() {
		try{
			ss = new ServerSocket(port);
			System.out.println(name+ ": Server running ");

			pool = Executors.newFixedThreadPool(sizeThreadPool);
			while(true) {						
				try {
					if(type){
						pool.execute(new ThreadAdmin(cpds , ss.accept()));
					}else{
						pool.execute(new ThreadClient(cpds , ss.accept()));
					}
				}catch(SocketException se) {
					System.out.println(name + ": Server stopped");
					break;
				}
			}
		} catch (IOException e) {
			System.out.println(name + ": Failed to initiate server");
			System.out.println(e.getMessage());
		}
	}
	public void shutDownServer() {
		if(ss.isClosed()) {
			System.out.println(name+ ": Server socket already close!");
		}else {
			try {
				ss.close();
			} catch (IOException e) {
				System.out.println("Error while trying to close server socket: " + name);
				System.out.println(e.getMessage());
			}
		}
	}
	public void shutDownThreadPool(int secWait) {
		pool.shutdown();
		try {
			// Wait a while for existing tasks to terminate
			System.out.println(name +": Waiting for threads to finish their tasks... ");
			if (pool.awaitTermination(secWait, TimeUnit.SECONDS)) {
				System.out.println(name + ": Thread pool successfully terminated!");				
			}else {
				System.out.println(name +": Time passed, forcing shutdown...");
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				System.out.println(name + ": Waiting for threads to respond to shutdownnow...");
				if (pool.awaitTermination(secWait, TimeUnit.SECONDS)) {
					System.out.println(name + ": Thread pool successfully terminated!");
				}else {
					System.out.println(name+ ": Pool did not terminate :(");
				}
			}
		} catch (InterruptedException ie) {
			System.out.println(name + ": Shutdown interrupted, server will call shutdownnow anyways");
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
		}
	}
	
}

