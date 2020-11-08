package testingServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MensajesDeCliente {
    private ArrayList<String> mensaje;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    MensajesDeCliente(ObjectInputStream i, ObjectOutputStream o){
        mensaje = new ArrayList<>();
        input = i;
        output = o;
    }

    public void borrarRecUsu(int idRec, String nickname){
        mensaje.add("BORRARRECUSU");
        mensaje.add(String.valueOf(idRec));
        mensaje.add(nickname);
    }

    public void calificar(String nickname, int idRec,  int calificacion){
        mensaje.add("CALIFICAR");;
        mensaje.add(nickname);
        mensaje.add(String.valueOf(idRec));
        mensaje.add(String.valueOf(calificacion));
    }

    public void cambiarContra(String nickname, String respuesta, String contraNueva){
        mensaje.add("CAMBIARCONTRA");
        mensaje.add(nickname);
        mensaje.add(respuesta);
        mensaje.add(contraNueva);
    }

    public void consRecetasCatIng( int numpag, int[] a){
        mensaje.add("CONSRECETASCATING");
        mensaje.add(String.valueOf(numpag));
        for (int i : a) {
            mensaje.add(String.valueOf(i));
        }
    }

    public void consRecetasCatRec( int numpag, int[] a){
        mensaje.add("CONSRECETASCATREC");
        mensaje.add(String.valueOf(numpag));
        for (int i : a) {
            mensaje.add(String.valueOf(i));
        }
    }

    public void consRecetaIng(int numpag, int[] a){
        mensaje.add("CONSRECETAING");
        mensaje.add(String.valueOf(numpag));
        for (int i: a) {
            mensaje.add(String.valueOf(i));
        }
    }

    public void consRecetaText(int numpag, String txt){
        mensaje.add("CONSRECETATEXT");
        mensaje.add(String.valueOf(numpag));
        mensaje.add(txt);
    }

    public void consTopRecetas(int numpag){
        mensaje.add("CONSTOPRECETAS");
        mensaje.add(String.valueOf(numpag));
    }

    public void datosReceta(int rID){
        mensaje.add("DATOSRECETA");
        mensaje.add(String.valueOf(rID));
    }

    public void ingredientes(){
        mensaje.add("INGREDIENTES");
    }

    public void listarCatRec(){
        mensaje.add("LISTARCATREC");
    }

    public void listarCatIng(){
        mensaje.add("LISTARCATING");
    }

    public void login(String nickname, String pass){
        mensaje.add("LOGIN");
        mensaje.add(nickname);
        mensaje.add(pass);
    }

    public void preguntasSeg(){
        mensaje.add("PREGUNTASSEG");
    }

    public void recetasDeUsuario(String nickname){
        mensaje.add("RECETASDEUSUARIO");
        mensaje.add(nickname);
    }

    public void registro(String nickname, int preguntaSeg, String resp, String nom, String ape, String contra, int genero, String mail){
        mensaje.add("REGISTRO");
        mensaje.add(nickname);
        mensaje.add(String.valueOf(preguntaSeg));
        mensaje.add(resp);
        mensaje.add(nom);
        mensaje.add(ape);
        mensaje.add(contra);
        mensaje.add(String.valueOf(genero));
        mensaje.add(mail);
    }

    public void servidorVive(){
        mensaje.add("SERVIDORVIVE");
    }

    public void usuPregSeg(String nickname){
        mensaje.add("USUPREGSEG");
        mensaje.add(nickname);
    }

    public void limpiarMensaje(){
        mensaje.clear();
    }

    public void enviarMensaje() throws IOException {
        output.writeObject(mensaje);
    }




}
