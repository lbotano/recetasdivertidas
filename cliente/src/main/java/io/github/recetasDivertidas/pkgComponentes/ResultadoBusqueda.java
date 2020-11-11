package io.github.recetasDivertidas.pkgComponentes;

import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
        lblCantCalificaciones.setText("(" + String.valueOf(receta.getCantCalificaciones()) + ")");
    }

    @FXML
    private void initialize() {
        calificador.setOnAction(event -> onCalificar((ActionEvent)event));
    }

    // Se llama cuando el usuario calificó la receta
    private void onCalificar(ActionEvent e) {
        System.out.println(calificador.getCalificacionPuesta());
        if (!receta.calificar(calificador.getCalificacionPuesta())) calificador.reiniciarEstrellas();
        receta.actualizarSimple();
        actualizarDatos();
    }
}