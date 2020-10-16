package pkgConexion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public final class Conexion {
    public static Socket socket;
    public static ObjectOutputStream output;
    public static ObjectInputStream input;

    public static ArrayList<String> sendMessage(ArrayList<String> message) throws IOException {
        ArrayList<String> answer = new ArrayList<String>();

        try {
            socket = new Socket("127.0.0.1", 7070);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            output.writeObject(message);

            answer = (ArrayList<String>) input.readObject();
        } catch(Exception e){
            System.out.println(e);
        }finally {
            socket.close();
        }

        return answer;
    }
}
