package pkgConexion;

import javafx.scene.control.Alert;
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

    public static ArrayList<String> sendMessage(ArrayList<String> message) throws IOException {
        ArrayList<String> answer = new ArrayList<>();

        try {
            socket = new Socket("127.0.0.1", 7070);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            output.writeObject(message);
            answer = (ArrayList<String>) input.readObject();

            socket.close();
        } catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hubo un problema");
            alert.setHeaderText("Se ha detectado un problema con el servidor");
            //Aca se usa el mensaje de error proporcionado con el servidor
            alert.setContentText("Upsi! Estamos teniendo problemas en nuestros servidores!!!");
            alert.initOwner(RecetasDivertidas.window.getScene().getWindow());
            alert.showAndWait();
        }

        return answer;
    }
}
