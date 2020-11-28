package io.github.recetasDivertidas.pkgComponentes;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgAplicacion.Aplicacion;
import io.github.recetasDivertidas.pkgRecetas.RecetaStage;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ResultadoBusqueda {
    @FXML private Label lblTitulo;
    @FXML private Label lblDescripcion;
    @FXML private Label lblAutor;
    @FXML private Label lblCalificacion;
    @FXML private Label lblCantCalificaciones;
    @FXML private Calificador calificador;
    @FXML private HBox paneResultado;

    Receta receta;

    public void ponerReceta(Receta receta){
        this.receta = receta;

        actualizarDatos();
    }

    @FXML
    private void hovered(){
        this.paneResultado.setStyle("-fx-background-color: #E1E1E1");
    }

    @FXML
    private void exited(){
        this.paneResultado.setStyle("");
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

    @FXML
    private void onAbrirReceta() throws IOException {
        try {
            Stage recetaStage = new RecetaStage(Receta.getReceta(receta.getId()));
            recetaStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }
    }
}