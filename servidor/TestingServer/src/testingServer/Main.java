package testingServer;

import clasesParaArrayList.Ingrediente;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        try (var socket = new Socket("127.0.0.1", 6969)) {

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            MensajesDeAdmin msgHandler = new MensajesDeAdmin(input, output);

            msgHandler.subirIngrediente("choclovich", new int[]{});

            try{
                msgHandler.enviarMensaje();
                System.out.println("Mensaje enviado");
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
