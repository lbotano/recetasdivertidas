package io.github.recetasDivertidas.pkgComponentes;

import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ResultadoBusqueda {
    @FXML private Label lblTitulo;
    @FXML private Label lblDescripcion;
    @FXML private Label lblAutor;
    @FXML private Label lblCalificacion;
    @FXML private Label lblCantCalificaciones;
    @FXML private Calificador calificador;

    Receta receta;

    public void ponerReceta(Receta receta){
        this.receta = receta;

        actualizarDatos();
    }

    private void actualizarDatos() {
        lblTitulo.setText(receta.getTitulo());
        lblDescripcion.setText(receta.getDescripcion());
        lblAutor.setText(receta.getAutor());
        lblCalificacion.setText(String.valueOf(receta.getCalificacion()));
        lblCantCalificaciones.setText("(" + receta.getCantCalificaciones() + ")");

        calificador.setCalificacionApariencia(receta.getCalificacionPropia());
    }

    @FXML
    private void initialize() {
        calificador.setOnAction(this::onCalificar);
    }

    // Se llama cuando el usuario calificó la receta
    private void onCalificar(ActionEvent e) {
        receta.calificar(calificador.getCalificacionPuesta());
        receta.actualizarSimple();
        actualizarDatos();
    }
}