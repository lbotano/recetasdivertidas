package io.github.recetasDivertidas.pkgAbrirMultimedia;

import io.github.recetasDivertidas.pkgRecetasDivertidas.Multimedia;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class MultimediaStage extends Stage {
    private Multimedia multimedia;

    public MultimediaStage(Multimedia multimedia) {
        super();

        this.multimedia = multimedia;

        this.setWidth(800);
        this.setHeight(600);
        this.setTitle(this.multimedia.esVideoYoutube() ? "Video" : "Imágen");
        this.getIcons().add(new Image(getClass().getResourceAsStream("/logo_recetas_divertidas.png")));

        Parent parent;
        if (this.multimedia.esVideoYoutube()) {
            parent = new WebView();
            System.out.println(this.multimedia.getUrlEmbeed());
            ((WebView)parent).getEngine().load(this.multimedia.getUrlEmbeed());
        } else {
            parent = new BorderPane();
            ImageView viewImagen = new ImageView(this.multimedia.getImg());
            ((BorderPane) parent).getChildren().add(viewImagen);
        }
        Scene scene = new Scene(parent);

        setScene(scene);
    }
}
