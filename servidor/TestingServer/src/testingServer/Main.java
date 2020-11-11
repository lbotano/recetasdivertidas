package testingServer;

import clasesParaArrayList.Ingrediente;

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


            ArrayList<Ingrediente> ingredientes = new ArrayList<Ingrediente>();
            ingredientes.add(new Ingrediente(1,200, "gramos"));
            ingredientes.add(new Ingrediente(2,500, "gramos"));
            ArrayList<String> catRecetas = new ArrayList<String>();
            catRecetas.add("1asdasd");
            ArrayList<String> multimedia = new ArrayList<String>();
            multimedia.add("https://www.google.com/");

            msgHandler.subirReceta("lbotano", "boquita","grande","pasion",ingredientes,catRecetas,multimedia);

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
