package io.github.recetasDivertidas.pkgAbrirMultimedia;

import io.github.recetasDivertidas.pkgRecetasDivertidas.Multimedia;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        ScrollPane scrollPane = new ScrollPane();
        ImageView viewImagen = new ImageView(this.multimedia.getImg());
        scrollPane.setContent(viewImagen);
        Scene scene = new Scene(scrollPane);

        setScene(scene);
    }
}
