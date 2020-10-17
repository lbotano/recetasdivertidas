package jpClienteTestingSockets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClienteTS {
	public static void main(String[] args) throws Exception {

		try (var socket = new Socket("127.0.0.1", 7070)) {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            
    		ArrayList<String> asd = new ArrayList<String>();
    		
    		asd.add("RECETASDEUSUARIO");
    		asd.add("lbotano");
    		
    		
    		output.writeObject(asd);    		
            @SuppressWarnings("unchecked")
            ArrayList<String> message = (ArrayList<String>) input.readObject();    
			for (int i = 0; i < message.size(); i++) {
				System.out.println(message.get(i));
			}

            System.out.println("Exiting...");
        }
	}
}
