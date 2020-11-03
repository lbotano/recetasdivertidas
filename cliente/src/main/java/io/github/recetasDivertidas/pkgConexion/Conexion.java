package io.github.recetasDivertidas.pkgConexion;

import javafx.scene.control.Alert;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public final class Conexion {
    public static Socket socket;
    public static ObjectOutputStream output;
    public static ObjectInputStream input;
    public static boolean svResponse;
    private final static String HOST = "127.0.0.1";
    private final static int PORT = 7070;

    public static ArrayList<String> sendMessage(List<String> message) throws IOException {
        ArrayList<String> answer;

        for(String s: message) {
            System.out.println("Send: " + s);
        }

        socket = new Socket(HOST, PORT);
        output = new ObjectOutputStream(socket.getOutputStream());
        socket.setSoTimeout(5000);
        input = new ObjectInputStream(socket.getInputStream());

        output.writeObject(message);
        try {
            answer = (ArrayList<String>) input.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException();
        }

        socket.close();

        return answer;
    }

    public static boolean isSvResponse() {
        ArrayList<String> msgOut = new ArrayList<>();
        msgOut.add("SERVIDORVIVE");

        try {
            ArrayList<String> msgIn = Conexion.sendMessage(msgOut);
            if (msgIn.get(0).equals("SERVIDORESTAVIVO")) return true;
        } catch (IOException e) {
            return false;
        }

        return false;
    }
}
