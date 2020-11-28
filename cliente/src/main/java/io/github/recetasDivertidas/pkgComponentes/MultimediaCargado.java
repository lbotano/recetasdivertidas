package io.github.recetasDivertidas.pkgComponentes;

import io.github.recetasDivertidas.pkgRecetasDivertidas.Multimedia;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class MultimediaCargado {
    @FXML public HBox root;
    @FXML public Hyperlink lblMultimedia;
    @FXML public Button btnRemover;
    @FXML public ImageView imgView;

    private Multimedia multimedia;

    //Setear multimedia
    public void setMultimedia(String url){
        multimedia = new Multimedia(url);
        this.lblMultimedia.setText(url);
        this.imgView.setImage(new Image(url));

        root.setUserData(multimedia);
    }

    public Multimedia getMultimedia(){
        return multimedia;
    }

    @FXML
    private void remover(){
        // Se elimina a si mismo
        Pane n = (Pane) root.getParent();
        n.getChildren().remove(root);
    }
}
