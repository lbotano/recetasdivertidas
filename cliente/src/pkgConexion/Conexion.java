package pkgConexion;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pkgRecetasDivertidas.Alerta;
import pkgRecetasDivertidas.RecetasDivertidas;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public final class Conexion {
    public static Socket socket;
    public static ObjectOutputStream output;
    public static ObjectInputStream input;
    public static boolean svResponse;

    public static ArrayList<String> sendMessage(ArrayList<String> message) throws IOException {
        ArrayList<String> answer = new ArrayList<>();

        try {
            socket = new Socket("127.0.0.1", 7070);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            output.writeObject(message);
            answer = (ArrayList<String>) input.readObject();

            socket.close();
        } catch (Exception e) {
            Alerta alert = new Alerta(Alert.AlertType.ERROR, "Se ha detectado un problema con el servidor",
                    "Upsi! Estamos teniendo problemas en nuestros servidores!!!");
            alert.showAndWait();
        }

        return answer;
    }

    public static boolean isSvResponse() {
        try {
            socket = new Socket("127.0.0.1", 7070);

            svResponse = true;
            socket.close();
        } catch(Exception e){
            svResponse = false;
        }
        return svResponse;
    }

    public static void probarConexion(){
        try {
            socket = new Socket("127.0.0.1", 7070);

            svResponse = true;
            socket.close();
        } catch(Exception e){
            svResponse = false;
            Alerta alert = new Alerta(Alert.AlertType.ERROR,"Se ha detectado un problema con el servidor",
                    "No se ha detectado conexion con el servidor");

            alert.showAndWait();
        }
    }
}
