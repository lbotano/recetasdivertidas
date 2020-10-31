package io.github.recetasDivertidas.pkgSubir;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgLogin.PreguntaSeguridad;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Categoria;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.ArrayList;

public final class SubirFuncionalidad {

    public static ArrayList<Categoria> getCategoriasReceta() throws IOException {
        //Se crea el arraylist y se le añade el mensaje que se manda
        ArrayList<String> message = new ArrayList<>();
        message.add("LISTARCATREC");
        //Espera a que le manden una respuesta
        ArrayList<String> ans = Conexion.sendMessage(message);
        //Se crea el arraylist que va a contener las categorias
        ArrayList<Categoria> categorias = new ArrayList<>();

        switch(ans.get(0)){
            case "LISTACATREC" ->{
                int i= 1;
                while(i < ans.size()){
                    categorias.add(new Categoria(Integer.parseInt(ans.get(i)),ans.get(i+1)));
                    i += 2;
                }
            }
            case "MESSAGEERROR" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "Hubo un problema al comunicarse con el servidor");
                alerta.showAndWait();
            }
            case "LISTARCATRECFAIL" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error con al obtener preguntas de seguridad", ans.get(1));
                System.out.println(ans.get(0));
                alerta.showAndWait();
            }
        }

        return categorias;
    }
}
