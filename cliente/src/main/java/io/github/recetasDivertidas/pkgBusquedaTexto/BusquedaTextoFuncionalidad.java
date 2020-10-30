package io.github.recetasDivertidas.pkgBusquedaTexto;

import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

public final class BusquedaTextoFuncionalidad{

   public static void openReceta(){
      System.out.println("Funciono");

   }

   public static void buscarReceta(VBox resultado){
      resultado.getChildren().addAll(addReceta());
   }

   /*Esto podes ignorarlo, tipo, es codigo hecho para probar como se ve cuando agregas una receta, lo que se deberia
     hacer es obtener el arraylist<string> que te manda el servidor y añadirlos a un layout separados por id
     cada layout es una receta, si apretas el layout se abre la pestaña con la info, y eso es otro mensaje que se manda
   * */
   private static ArrayList<GridPane> addReceta(){
      ArrayList<GridPane> recetas = new ArrayList<>();
      GridPane grid = new GridPane();
      //grid.setPadding(new Insets(10, 10, 10, 10));
      grid.setVgap(0);
      grid.setHgap(0);
      grid.setOnMouseClicked((EventHandler<Event>) event -> BusquedaTextoFuncionalidad.openReceta());
      grid.setOnMouseEntered((EventHandler<Event>) event -> grid.setStyle("-fx-background-color: "+ RecetasDivertidas.hovered));
      grid.setOnMouseExited((EventHandler<Event>) event -> grid.setStyle("-fx-background-color: "+ RecetasDivertidas.background));

      grid.setStyle("-fx-background-color: "+ RecetasDivertidas.background);


      //Username setup
      Label lblTitulo = new Label("Titulo");
      lblTitulo.setFont(new Font("Arial", 16));
      grid.add(lblTitulo, 0, 0);

      Label lblDescripcion = new Label("Descripcion");
      lblDescripcion.setFont(new Font("Arial", 11));
      grid.add(lblDescripcion, 0, 1);

      Label lblIngredientes = new Label("Ingredientes");
      lblIngredientes.setFont(new Font("Arial", 11));
      grid.add(lblIngredientes, 0, 2);

      return recetas;
   }


   public static void obtenerRecetas(int i, String consulta) {
   }
}
