package io.github.recetasDivertidas.pkgSubir;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class Subir {
    @FXML private VBox vboxLeft;
    @FXML private VBox vboxRight;

    @FXML
    private void initialize() {
        // Hack para que los combobox no cambien de tamaño al clickearlos
        vboxLeft.setPrefSize(vboxLeft.getWidth(), vboxLeft.getHeight());
        vboxRight.setPrefSize(vboxRight.getWidth(), vboxRight.getHeight());
    }
}
