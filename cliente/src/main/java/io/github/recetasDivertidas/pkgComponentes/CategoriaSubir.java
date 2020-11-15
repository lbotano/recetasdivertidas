package io.github.recetasDivertidas.pkgComponentes;

import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaReceta;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CategoriaSubir {
    @FXML HBox root;
    @FXML Label lblCategoria;

    private CategoriaReceta categoria;

    public CategoriaReceta getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaReceta categoria) {
        this.categoria = categoria;
        root.setUserData(this.categoria);

        this.lblCategoria.setText(this.categoria.toString());
    }

    @FXML
    private void borrar() {
        // Se elimina a si mismo
        Pane n = (Pane) root.getParent();
        n.getChildren().remove(root);
    }
}
