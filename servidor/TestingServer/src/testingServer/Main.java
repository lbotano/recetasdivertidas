package testingServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        try (var socket = new Socket("127.0.0.1", 7070)) {

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            MensajesDeCliente msgHandler = new MensajesDeCliente(input, output);

            msgHandler.consTopRecetas(0);
            try{
                msgHandler.enviarMensaje();
            }catch (IOException e){
                System.out.println("No se pudo enviar el mensaje: ");
                e.printStackTrace();
            }finally {
                msgHandler.limpiarMensaje();
            }

            @SuppressWarnings("unchecked")
            ArrayList<String> message = (ArrayList<String>) input.readObject();

            for (int i = 0; i < message.size(); i++) {
                System.out.println(message.get(i));
            }

            System.out.println("Exiting...");
        }
    }
}
