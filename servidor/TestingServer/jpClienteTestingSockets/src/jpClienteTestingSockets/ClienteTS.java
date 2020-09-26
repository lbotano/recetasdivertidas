package jpClienteTestingSockets;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTS {
	public static void main(String[] args) throws Exception {
		try (var socket = new Socket("127.0.0.1", 59898)) {
            Scanner scanner = new Scanner(System.in);
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String nextLine;
            System.out.println("Enter 'sp' or 'exit' ");           
            while (scanner.hasNextLine()) {
                nextLine =scanner.nextLine();
            	if(nextLine.contentEquals("exit")) {
            		System.out.println("Leaving...");
            		break;
            	}else {
            		out.println(nextLine);
            		while(in.hasNextLine()){
            			String msg = in.nextLine();
                		System.out.println(msg);
            			if(msg.contentEquals("done")) {
                    		System.out.println("Termino mensajes de server");
            				break;
            			}
                	}
            	}
            }
            System.out.println("Exiting...");
        }
	}
}
