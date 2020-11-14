package io.github.recetasDivertidas.pkgSubir;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaReceta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.MensajeServerInvalidoException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Subir {
    @FXML private VBox vboxLeft;
    @FXML private VBox vboxRight;
    @FXML private ComboBox<Ingrediente> cmbIngredientes;
    @FXML private ComboBox<CategoriaReceta> cmbCategorias;

    @FXML
    private void initialize() {
        // Hack para que los combobox no cambien de tamaño al clickearlos
        vboxLeft.setPrefSize(vboxLeft.getWidth(), vboxLeft.getHeight());
        vboxRight.setPrefSize(vboxRight.getWidth(), vboxRight.getHeight());

        // Poner los ingredientes en el combobox
        try {
            cmbIngredientes.getItems().addAll(Ingrediente.getIngredientes());
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }

        // Poner las categorías en el combobox
        try {
            cmbCategorias.getItems().addAll(CategoriaReceta.getCategorias());
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }
    }
}
