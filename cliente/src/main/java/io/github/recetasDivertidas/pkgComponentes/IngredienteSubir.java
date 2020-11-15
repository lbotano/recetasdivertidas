package io.github.recetasDivertidas.pkgComponentes;

import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class IngredienteSubir {
    @FXML HBox root;
    @FXML Label lblIngrediente;
    @FXML TextField txtCantidad;
    @FXML TextField txtUnidad;

    private Ingrediente ingrediente;

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
        root.setUserData(this.ingrediente);

        this.lblIngrediente.setText(ingrediente.toString());
    }

    @FXML
    private void actualizarDatos() {
        // Hace que solo se puedan poner números en la cantidad
        if (!txtCantidad.getText().matches("\\d*")) {
            txtCantidad.setText(txtCantidad.getText().replaceAll("[^\\d]", ""));
        }

        this.ingrediente.setCantidad(txtCantidad.getText().isBlank() ? 0 : Integer.parseInt(txtCantidad.getText()));
        this.ingrediente.setUnidad(txtUnidad.getText());
    }

    @FXML
    private void borrar() {
        // Se elimina a si mismo
        Pane n = (Pane) root.getParent();
        n.getChildren().remove(root);
    }
}
