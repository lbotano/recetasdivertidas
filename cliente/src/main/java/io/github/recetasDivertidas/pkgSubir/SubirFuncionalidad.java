package io.github.recetasDivertidas.pkgSubir;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaIngrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaReceta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public final class SubirFuncionalidad {

    public static ArrayList<CategoriaReceta> getCategoriasReceta() throws IOException {
        //Se crea el arraylist y se le añade el mensaje que se manda
        ArrayList<String> message = new ArrayList<>();
        message.add("LISTARCATREC");
        //Espera a que le manden una respuesta
        ArrayList<String> ans = Conexion.sendMessage(message);
        //Se crea el arraylist que va a contener las categorias
        ArrayList<CategoriaReceta> categoriaRecetas = new ArrayList<>();

        switch(ans.get(0)){
            case "LISTACATREC" ->{
                int i= 1;
                while(i < ans.size()){
                    categoriaRecetas.add(new CategoriaReceta(Integer.parseInt(ans.get(i)),ans.get(i+1)));
                    i += 2;
                }
            }
            case "LISTARCATRECFAIL" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error con al obtener preguntas de seguridad", ans.get(1));
                System.out.println(ans.get(0));
                alerta.showAndWait();
            }
            case "MESSAGEERROR" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "Hubo un problema al comunicarse con el servidor");
                alerta.showAndWait();
            }
            case "ELEMENTBLANK" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "El mensaje contenia espacios en blanco");
                alerta.showAndWait();
            }
            case "FORMATERROR" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "Hubo un problema en el formato del mensaje");
                alerta.showAndWait();
            }
        }

        return categoriaRecetas;
    }

    public static void addIngrediente(VBox vbox) throws IOException {
        HBox hbox = new HBox();

        ComboBox<CategoriaIngrediente> cmbCategoriaIngrediente = new ComboBox<>();
        cmbCategoriaIngrediente.setPromptText("Que tipo de ingrediente es?");
        if (Conexion.isSvResponse()) {
            cmbCategoriaIngrediente.getItems().addAll(SubirFuncionalidad.getCategoriasIngrediente());
        }

        ComboBox<Ingrediente> cmbIngrediente = new ComboBox<>();
        cmbIngrediente.setPromptText("Que ingrediente es?");
        if (Conexion.isSvResponse()) {
            cmbIngrediente.getItems().addAll(SubirFuncionalidad.getIngredientes());
        }

        Button btnMas = new Button("+");
        Button btnMenos = new Button("-");
        btnMas.setOnAction(e -> {
            try {
                addIngrediente(vbox);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        hbox.getChildren().addAll(cmbCategoriaIngrediente,cmbIngrediente,btnMas,btnMenos);
        vbox.getChildren().add(hbox);

        btnMenos.setOnAction(e -> removeIngrediente(vbox,hbox));

        System.out.println("Añadido un elemento");
        System.out.println("Ahora hay " + vbox.getChildren().size() + " elementos");
    }

    public static void removeIngrediente(VBox vbox, HBox hBox){
        if (vbox.getChildren().size() > 1) {
            vbox.getChildren().remove(hBox);
            System.out.println("Quitado un elemento");
            System.out.println("Ahora hay " + vbox.getChildren().size() + " elementos");
        }
    }

    public static ArrayList<Ingrediente> getIngredientes() throws IOException {
        //Se crea el arraylist y se le añade el mensaje que se manda
        ArrayList<String> message = new ArrayList<>();
        message.add("INGREDIENTES");
        //Espera a que le manden una respuesta
        ArrayList<String> ans = Conexion.sendMessage(message);
        //Se crea el arraylist que va a contener las categorias
        ArrayList<Ingrediente> ingredientes = new ArrayList<>();

        switch(ans.get(0)){
            case "INGREDIENTESOK" ->{
                int i= 1;
                while(i < ans.size()){
                    ingredientes.add(new Ingrediente(Integer.parseInt(ans.get(i)),ans.get(i+1)));
                    i += 2;
                }
            }
            case "INGREDIENTESFAIL" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error con al obtener preguntas de seguridad", ans.get(1));
                System.out.println(ans.get(0));
                alerta.showAndWait();
            }
            case "MESSAGEERROR" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "Hubo un problema al comunicarse con el servidor");
                alerta.showAndWait();
            }
            case "ELEMENTBLANK" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "El mensaje contenia espacios en blanco");
                alerta.showAndWait();
            }
            case "FORMATERROR" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "Hubo un problema en el formato del mensaje");
                alerta.showAndWait();
            }
        }

        return ingredientes;
    }

    public static ArrayList<CategoriaIngrediente> getCategoriasIngrediente() throws IOException {
        //Se crea el arraylist y se le añade el mensaje que se manda
        ArrayList<String> message = new ArrayList<>();
        message.add("LISTARCATING");
        //Espera a que le manden una respuesta
        ArrayList<String> ans = Conexion.sendMessage(message);
        //Se crea el arraylist que va a contener las categorias
        ArrayList<CategoriaIngrediente> categoriaIngredientes = new ArrayList<>();

        switch(ans.get(0)){
            case "LISTACATING" -> {
                int i= 1;
                while(i < ans.size()){
                    categoriaIngredientes.add(new CategoriaIngrediente(Integer.parseInt(ans.get(i)),ans.get(i+1)));
                    i += 2;
                }
            }
            case "LISTARCATINGFAIL" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error con al obtener preguntas de seguridad", ans.get(1));
                System.out.println(ans.get(0));
                alerta.showAndWait();
            }
            case "MESSAGEERROR" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "Hubo un problema al comunicarse con el servidor");
                alerta.showAndWait();
            }
            case "ELEMENTBLANK" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "El mensaje contenia espacios en blanco");
                alerta.showAndWait();
            }
            case "FORMATERROR" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "Hubo un problema en el formato del mensaje");
                alerta.showAndWait();
            }

        }

        return categoriaIngredientes;
    }

    public static void subirReceta(){

    }
}
