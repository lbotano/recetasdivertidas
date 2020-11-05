package io.github.recetasDivertidas.pkgBusquedaTexto;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public final class BusquedaTextoFuncionalidad{

   public static void openReceta(int id){
      Receta receta = new Receta();
      receta.show();
   }

   public static void obtenerRecetas(VBox resultado, int pag, String consulta) throws IOException {
      ArrayList<GridPane> recetas = new ArrayList<>();
      ArrayList<String> message = new ArrayList<>();
      message.add("CONSRECETATEXT");
      message.add(String.valueOf(pag));
      message.add(consulta);

      if (Conexion.isSvResponse()){
         ArrayList<String> ans = Conexion.sendMessage(message);
         for (int i = 0; i < ans.size(); i++) {
            System.out.println(ans.get(i));
         }
      }else{
         Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error al conectarse con el servidor.",
                 "Hubo un problema al intentar buscar.");
         alerta.showAndWait();
      }



      resultado.getChildren().addAll(recetas);
   }


   public static void obtenerRecetas(int i, String consulta) {
   }
}
