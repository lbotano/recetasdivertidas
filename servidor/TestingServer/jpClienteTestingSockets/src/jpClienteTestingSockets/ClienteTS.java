package jpClienteTestingSockets;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClienteTS {
	public static void main(String[] args) throws Exception {

		try (var socket = new Socket("127.0.0.1", 6969)) {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            
    		ArrayList<String> asd = new ArrayList<String>();
    		
    		asd.add("REGISTRO");
    		asd.add("asd");
    		asd.add("1");
    		asd.add("asd");
    		asd.add("asd");
    		asd.add("asd");
    		asd.add("asd");
    		asd.add("0");
    		asd.add("asd");
    		
    		output.writeObject(asd);    		
            @SuppressWarnings("unchecked")
            ArrayList<String> message = (ArrayList<String>) input.readObject();    
            System.out.println(message.get(0));
            
            System.out.println(message.get(1));
            System.out.println("Exiting...");
        }
	}
}
