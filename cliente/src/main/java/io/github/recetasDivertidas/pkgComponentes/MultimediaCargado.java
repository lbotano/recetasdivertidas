package io.github.recetasDivertidas.pkgComponentes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class MultimediaCargado {
    public HBox root;
    public Hyperlink lblMultimedia;
    public Button btnRemover;
    public ImageView imgView;

    @FXML
    private void remover(){
        // Se elimina a si mismo
        Pane n = (Pane) root.getParent();
        n.getChildren().remove(root);
    }
}
