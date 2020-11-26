package io.github.recetasDivertidas.pkgRecetas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.Calificador;
import io.github.recetasDivertidas.pkgComponentes.CategoriaBajar;
import io.github.recetasDivertidas.pkgComponentes.IngredienteBajar;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaIngrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaReceta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class RecetaController {
    @FXML private Label lblTitulo;
    @FXML private Label lblCalificacion;
    @FXML private Calificador calificador;
    @FXML private Label lblCantCalificaciones;
    @FXML private Label lblDescripcion;
    @FXML private Label lblInstrucciones;
    @FXML private VBox vboxIngredientes;
    @FXML private VBox vboxCategorias;

    private Receta receta;

    public void setReceta(Receta receta) {
        this.receta = receta;

        lblTitulo.setText(this.receta.getTitulo());
        lblCalificacion.setText(String.valueOf(this.receta.getCalificacion()));
        calificador.setCalificacionApariencia(this.receta.getCalificacionPropia());
        lblCantCalificaciones.setText(String.valueOf(this.receta.getCantCalificaciones()));
        lblDescripcion.setText(this.receta.getDescripcion());
        lblInstrucciones.setText(this.receta.getInstrucciones());

        // Añadir ingredientes
        try {
            for (Ingrediente i : this.receta.getIngredientes()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/ingrediente_bajar.fxml"));
                Pane ingPane = loader.load();
                IngredienteBajar ingController = loader.getController();
                ingController.setIngrediente(i);
                vboxIngredientes.getChildren().add(ingPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }

        // Añadir categorías de receta
        try {
            for (CategoriaReceta c : this.receta.getCategoriasReceta()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/categoria_bajar.fxml"));
                Pane catPane = loader.load();
                CategoriaBajar catController = loader.getController();
                catController.setCategoria(c);
                vboxCategorias.getChildren().add(catPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado.");
            alerta.showAndWait();
        }

        // Añadir categorías de ingrediente
        try {
            for (CategoriaIngrediente c : this.receta.getCategoriasIngrediente()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/categoria_bajar.fxml"));
                Pane catPane = loader.load();
                CategoriaBajar catController = loader.getController();
                catController.setCategoria(c);
                vboxCategorias.getChildren().add(catPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado.");
            alerta.showAndWait();
        }

        calificador.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                receta.calificar(calificador.getCalificacionPuesta());
                receta.actualizarSimple();
                calificador.setCalificacionApariencia(receta.getCalificacionPropia());
            }
        });
    }
}
