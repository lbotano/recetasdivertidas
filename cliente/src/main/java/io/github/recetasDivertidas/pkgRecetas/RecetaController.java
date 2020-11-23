package io.github.recetasDivertidas.pkgRecetas;

import io.github.recetasDivertidas.pkgComponentes.Calificador;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class RecetaController {
    @FXML private Label lblTitulo;
    @FXML private Label lblCalificacion;
    @FXML private Calificador calificador;
    @FXML private Label lblCantCalificaciones;
    @FXML private Label lblDescripcion;
    @FXML private Label lblInstrucciones;

    private Receta receta;

    public void setReceta(Receta receta) {
        this.receta = receta;

        lblTitulo.setText(this.receta.getTitulo());
        lblCalificacion.setText(String.valueOf(this.receta.getCalificacion()));
        calificador.setCalificacionApariencia(this.receta.getCalificacionPropia());
        lblCantCalificaciones.setText(String.valueOf(this.receta.getCantCalificaciones()));
        lblDescripcion.setText(this.receta.getDescripcion());
        lblInstrucciones.setText(this.receta.getInstrucciones());

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
