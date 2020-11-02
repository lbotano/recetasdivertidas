package threadsMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ThreadConsole implements Runnable {
	private ThreadServer refServerAdmin;
	private ThreadServer refServerClient;
	private ComboPooledDataSource cpds;
	private boolean exit = false;
	private final String commands = "stop; start; exit; newAdmin(<nickname>);";
	
	public ThreadConsole(ComboPooledDataSource c, ThreadServer refAdmin, ThreadServer refClient) {
		this.refServerAdmin = refAdmin;
		this.refServerClient = refClient;
		this.cpds = c;
	}
	
	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			String input = "";
			while (!exit) {
				input = br.readLine().strip();
				if(input.contentEquals("stop")) {
					refServerAdmin.shutDownServer();
					refServerAdmin.shutDownThreadPool(20);
					refServerClient.shutDownServer();
					refServerClient.shutDownThreadPool(20);
					exit = true;
				}else if(input.contains("newAdmin")) {
					System.out.println("Not implemented yet!");
					/*
					ArrayList<String> parameters = inputParameters(input);
					if(parameters.size() == 2) {
						//crear un nuevo admin
						Connection con;
						try {
							con = cpds.getConnection();
							
							System.out.println("");
						} catch (SQLException e) {
							System.out.println("Could not excute command: " + e.getMessage());
						}
					}*/
				}else {
					System.out.println(input + " is not a valid command! try: " + commands);							
				}						
			}
		} catch (IOException e) {
			System.out.println("Failed to read console input! ");
		}
	}
	
	
	private ArrayList<String> inputParameters(String s) {
		String temp[] = s.split(" ");
		ArrayList<String> result = new ArrayList<String>();
		  for (int i = 0; i < temp.length; i++) {
			  if(!temp[i].isBlank()) {
				  result.add(temp[i]);				  
			  }
		}
		return result;
		
	}
	
	
}
