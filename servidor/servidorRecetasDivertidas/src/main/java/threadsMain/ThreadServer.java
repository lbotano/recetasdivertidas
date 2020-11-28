package threadsMain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import servidorRecetasDivertidas.ThreadClient;

public class ThreadServer implements Runnable {
	private String name;
	private int port;
	private ExecutorService pool;
	private int sizeThreadPool;
	private ComboPooledDataSource cpds;
	private ServerSocket socketListener;

	public ThreadServer(ComboPooledDataSource combo, int port, int sizePool) {
		this.cpds = combo;
		this.port = port;
		this.sizeThreadPool = sizePool;
	}
	
	@Override
	public void run() {
		try {
			socketListener = new ServerSocket(port);
			pool = Executors.newFixedThreadPool(sizeThreadPool);

			System.out.println("[INFO] Server running ");

			boolean isRunning = true;
			while (isRunning) {
				try {
					pool.execute(new ThreadClient(cpds, socketListener.accept()));
				} catch (SocketException se) {
					System.out.println("[INFO] Server stopped");
					isRunning = false;
				}
			}

		} catch (IOException e) {
			System.err.println("[ERROR] Failed to start server");
			e.printStackTrace();
		}
	}
	public void shutDownServer() {
		if (socketListener.isClosed()) {
			System.out.println("[WARNING] Server socket already closed!");
		} else {
			try {
				socketListener.close();
			} catch (IOException e) {
				System.err.println("[ERROR] Error while trying to close server socket: " + name);
				e.printStackTrace();
			}
		}
	}
	public void shutDownThreadPool(int secWait) {
		pool.shutdown();
		try {
			// Wait a while for existing tasks to terminate
			System.out.println("[INFO] Waiting for threads to finish their tasks... ");
			if (pool.awaitTermination(secWait, TimeUnit.SECONDS)) {
				System.out.println("[INFO] Thread pool successfully terminated!");
			} else {
				System.out.println("[WARNING] Time passed, forcing shutdown...");
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				System.out.println("[INFO] Waiting for threads to respond to shutdownnow...");
				System.out.println((pool.awaitTermination(secWait, TimeUnit.SECONDS) ?
						"[INFO] Thread pool successfully terminated!" : "[WARNING] Pool didn't terminate :("));
			}
		} catch (InterruptedException ie) {
			System.out.println("[WARNING] Shutdown interrupted, server will call shutdownnow anyway");
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
		}
	}
	
}

