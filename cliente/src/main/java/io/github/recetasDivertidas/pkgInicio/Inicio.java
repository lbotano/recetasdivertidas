package io.github.recetasDivertidas.pkgInicio;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.ResultadoBusqueda;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Inicio{
    @FXML Button btnNextPag;
    @FXML Button btnPrevPag;
    @FXML VBox vboxResultados;
    private int paginaActual = 0;

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
            topRecetas = Receta.getTopRecetas(paginaActual);

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

    public void nextPag() {
        if(vboxResultados.getChildren().size() == 10){
            paginaActual++;
            actualizarRecetas();
        }
    }

    public void prevPag() {
        if (paginaActual > 0) {
            paginaActual--;
            actualizarRecetas();
        }
    }

}
