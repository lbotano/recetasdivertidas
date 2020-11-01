package io.github.recetasDivertidas.pkgBusquedaTexto;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.ArrayList;

public final class BusquedaTextoFuncionalidad{

   public static void openReceta(int id){
      Receta receta = new Receta();
      receta.show();
   }

   /*Esto podes ignorarlo, tipo, es codigo hecho para probar como se ve cuando agregas una receta, lo que se deberia
     hacer es obtener el arraylist<string> que te manda el servidor y añadirlos a un layout separados por id
     cada layout es una receta, si apretas el layout se abre la pestaña con la info, y eso es otro mensaje que se manda
   * */

   public static void obtenerRecetas(VBox resultado, int pag, String consulta) throws IOException {
      ArrayList<GridPane> recetas = new ArrayList<>();
      ArrayList<String> message = new ArrayList<>();
      /*
      CONSRECETATEXT
      <numpag> (numero de pagina de la consulta, para no mandar todas las id de las recetas, se separa por paginas)
      <texto> (texto que ingresa el usuario para realizar la búsqueda)

      La respuesta todavia no esta implementada
      RESPCONSULTA
      <rID> (id de receta)
      <rAutor> (autor de la receta)
      <rNombre> (nombre de la receta)
      …
      <rID>
      <rAutor>
      <rNombre>
       RESPOCONSULTAFAIL: Error al hacer búsqueda de recetas.
      RESPOCONSULTAFAIL
      <mensajeError>
       */
      message.add("CONSRECETATEXT");
      message.add(String.valueOf(pag));
      message.add(consulta);

      if (Conexion.isSvResponse()){
         ArrayList<String> ans = Conexion.sendMessage(message);
         for (int i = 0; i < ans.size(); i++) {
            System.out.println(ans.get(i));
         }
      }else{
         Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error al conectarse con el servidor",
                 "Hubo un problema al intentar buscar");
         alerta.showAndWait();
      }



      resultado.getChildren().addAll(recetas);
   }


   public static void obtenerRecetas(int i, String consulta) {
   }
}
