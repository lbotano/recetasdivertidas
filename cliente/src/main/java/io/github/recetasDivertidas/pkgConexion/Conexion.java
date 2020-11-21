package io.github.recetasDivertidas.pkgConexion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public final class Conexion {
    public static Socket socket;
    public static ObjectOutputStream out;
    public static ObjectInputStream in;
    public static boolean svResponse;
    private final static String HOST = "127.0.0.1";
    private static int port = 7070;

    public static void iniciarConexion() {
        try {
            socket = new Socket(HOST, port);
            socket.setSoTimeout(5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> sendMessage(List<String> message) throws IOException {
        ArrayList<String> answer;
        //out.flush();

        for(String s: message) {
            System.out.println("Send: " + s);
        }

        out.writeObject(message);
        try {
            answer = (ArrayList<String>) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException();
        }

        for(String s: answer) {
            System.out.println("Received: " + s);
        }

        return answer;
    }

    public static void setAdminPort(){
        port = 6969;
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
