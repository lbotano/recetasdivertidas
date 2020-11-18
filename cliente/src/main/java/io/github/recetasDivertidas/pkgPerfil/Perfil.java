package io.github.recetasDivertidas.pkgPerfil;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.ResultadoBusqueda;
import io.github.recetasDivertidas.pkgRecetasDivertidas.MensajeServerInvalidoException;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class Perfil {
    @FXML Label lblPregunta;
    @FXML VBox vboxMisRecetas;
    @FXML TextField txtNewPwd;
    @FXML TextField txtRespuesta;
    @FXML Button btnCambiarPwd;
    @FXML Label lblUsuario;

    @FXML
    public void initialize() throws MensajeServerInvalidoException, IOException {
        lblUsuario.setText(RecetasDivertidas.username);
        showRecetasUsuario();
    }

    public void showRecetasUsuario(){
        vboxMisRecetas.getChildren().clear();

        try {
            ArrayList<Receta> recetasUsuario = Receta.getRecetasUsuario(RecetasDivertidas.username);

            if (recetasUsuario != null) {
                for (Receta r : recetasUsuario) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/componentes/resultado_busqueda.fxml"));
                    HBox paneReceta = fxmlLoader.load();
                    ResultadoBusqueda controllerResultadoBusqueda = fxmlLoader.getController();
                    controllerResultadoBusqueda.ponerReceta(r);
                    vboxMisRecetas.getChildren().add(paneReceta);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alerta a = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            a.showAndWait();
        }
    }

    public void cambiarPwd() {
    }
}
