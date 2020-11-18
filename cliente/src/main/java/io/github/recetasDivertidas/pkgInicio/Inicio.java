package io.github.recetasDivertidas.pkgInicio;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.ResultadoBusqueda;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.io.IOException;
import java.util.ArrayList;

public class Inicio{
    @FXML Button btnNextPag;
    @FXML Button btnPrevPag;
    @FXML VBox vboxResultados;
    private int current_pag = 0;

    ArrayList<Receta> topRecetas;

    @FXML
    private void initialize() {
        actualizarRecetas();
    }

    @FXML
    private void recargar() {
        actualizarRecetas();
    }

    // Recarga la página (mantiene el número de página)
    private void actualizarRecetas() {
        vboxResultados.getChildren().clear();

        try {
            topRecetas = Receta.getTopRecetas(current_pag);

            for (Receta r : topRecetas) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/componentes/resultado_busqueda.fxml"));
                HBox paneReceta = fxmlLoader.load();
                ResultadoBusqueda controllerResultadoBusqueda = fxmlLoader.getController();
                controllerResultadoBusqueda.ponerReceta(r);
                vboxResultados.getChildren().add(paneReceta);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alerta a = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            a.showAndWait();
        }
    }

    @FXML
    private void nextPag() {
        // TODO: Implementar
    }

    @FXML
    private void prevPag() {
        // TODO: Implementar
    }

}
