package io.github.recetasDivertidas.pkgBusquedas;

import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ResultadoBusqueda {
    @FXML private Label lblTitulo;
    @FXML private Label lblDescripcion;
    @FXML private Label lblAutor;
    @FXML private Label lblCalificacion;
    @FXML private Label lblCantCalificaciones;

    Receta receta;

    public void ponerReceta(Receta receta){
        this.receta = receta;

        lblTitulo.setText(receta.getTitulo());
        lblDescripcion.setText(receta.getDescripcion());
        lblAutor.setText(receta.getAutor());
        lblCalificacion.setText(String.valueOf(receta.getCalificacion()));
        lblCantCalificaciones.setText(String.valueOf(receta.getCantCalificaciones()));
    }
}
