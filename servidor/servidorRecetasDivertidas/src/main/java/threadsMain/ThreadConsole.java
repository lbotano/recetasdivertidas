package threadsMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ThreadConsole implements Runnable {
	private final ThreadServer refServer;
	private final ComboPooledDataSource cpds;
	private Connection con;
	private boolean exit = false;

	public ThreadConsole(ComboPooledDataSource c, ThreadServer refServer) {
		this.refServer = refServer;
		this.cpds = c;
	}
	
	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
			String input = "";
			while (!exit) {
				input = br.readLine().strip();
				if(input.contentEquals("stop")) {
					refServer.shutDownServer();
					refServer.shutDownThreadPool(20);
					exit = true;
				}else if(input.contains("newAdmin")) {

					ArrayList<String> parameters = inputParameters(input);
					if(parameters.size() == 2) {
						//crear un nuevo admin
						try {
							con = cpds.getConnection();
							CallableStatement stmt = con.prepareCall("call spDarAdmin(?)");
							stmt.setString(1,parameters.get(1));
							stmt.execute();
							System.out.println("Console: Added new admin!");
						} catch (SQLException e) {
							String errMes = "Console: Could not excute db request: ";
							if(e.getSQLState().contentEquals("45000")){
								errMes += e.getMessage();
							}else{
								errMes += " an error ocurred in db";
							}
							System.out.println(errMes);

						}finally {
							try {
								con.close();
							} catch (SQLException throwables) {
								System.out.println("Console: error while trying to close connection for console");
							}
						}
					}else{
						System.out.println("Console: The number of parameters is not valid!");
					}
				}else {
					String commands = "stop; newAdmin <nickname>;";
					System.out.println("Console: " +input + " is not a valid command! try: " + commands);
				}						
			}
		} catch (IOException e) {
			System.out.println("Console: Failed to read console input! ");
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
