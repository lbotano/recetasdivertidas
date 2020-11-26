package io.github.recetasDivertidas.pkgComponentes;

import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class IngredienteBajar {
    @FXML HBox root;
    @FXML Label lblIngrediente;
    @FXML Label lblCantidad;
    @FXML Label lblUnidad;

    public void setIngrediente (Ingrediente ingrediente) {
        root.setUserData(ingrediente);

        lblIngrediente.setText(ingrediente.toString());
        lblCantidad.setText(String.valueOf(ingrediente.getCantidad()));
        lblUnidad.setText(String.valueOf(ingrediente.getUnidad()));
    }
}
