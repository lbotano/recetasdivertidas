package testingServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MensajesDeAdmin extends Mensajes{
    MensajesDeAdmin(ObjectInputStream i, ObjectOutputStream o){
        super(i,o);
    }
    /*
    0. BORRARCATING
    1. BORRARCATREC
    2. BORRARING
    3. BORRARREC
    * */
    public void borrar(int opcion,int id){
        switch (opcion) {
            case 0:
                mensaje.add("BORRARCATING");
                break;
            case 1:
                mensaje.add("BORRARCATREC");
                break;
            case 2:
                mensaje.add("BORRARING");
                break;
            case 3:
                mensaje.add("BORRARREC");
                break;
        }
        mensaje.add(String.valueOf(id));
    }

    public void banearUsuario(String nickname){
        mensaje.add("BANEARUSUARIO");
        mensaje.add(nickname);
    }

    //true: categoria de ingrediente
    //false: categoria de receta
    public void subirCategoria(boolean opcion, String cNombre){
        if(opcion){
            mensaje.add("SUBIRCATING");
        }else{
            mensaje.add("SUBIRCATREC");
        }
        mensaje.add(cNombre);
    }

    public void subirIngrediente(String iNombre, int[] categorias){
        mensaje.add("SUBIRING");
        mensaje.add(iNombre);
        for(int i: categorias){
            mensaje.add(String.valueOf(i));
        }
    }
}
