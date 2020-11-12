package testingServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Mensajes {
    protected ArrayList<String> mensaje;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;

    Mensajes(ObjectInputStream i, ObjectOutputStream o){
        mensaje = new ArrayList<>();
        input = i;
        output = o;
    }
    public void limpiarMensaje(){
        mensaje.clear();
    }

    public void enviarMensaje() throws IOException {
        output.writeObject(mensaje);
        limpiarMensaje();
    }

}
