package io.github.recetasDivertidas.pkgConexion;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public final class Conexion {
    public static Socket socket;
    public static ObjectOutputStream out;
    public static ObjectInputStream in;
    public static boolean svResponse;
    public static String HOST;
    private final static int PORT = 7070;

    public static void iniciarConexion() {
        try {
            System.out.println("Conectando");
            socket = new Socket(HOST, PORT);
            socket.setSoTimeout(5000);
            System.out.println("Conectado");
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> sendMessage(List<String> message) throws IOException, ClassNotFoundException {
        ArrayList<String> answer = new ArrayList<>();

        for (String s: message)
            System.out.println("Send: " + s);

        out.writeObject(message);

        try {
            answer.addAll((ArrayList<String>)in.readObject());
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error con del servidor",
                    "El servidor no responde");
            alerta.showAndWait();
        }

        for (String s: answer)
            System.out.println("Received: " + s);

        return answer;
    }

    public static boolean isSvResponse() {
        ArrayList<String> msgOut = new ArrayList<>();
        msgOut.add("SERVIDORVIVE");

        try {
            ArrayList<String> msgIn = Conexion.sendMessage(msgOut);
            if (msgIn.get(0).equals("SERVIDORESTAVIVO")) return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
    public static boolean setServerIP(String ip){
        String regex = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        if(ip.matches(regex)){
            HOST = ip;
            return true;
        }else{
            return false;
        }

    }
}
