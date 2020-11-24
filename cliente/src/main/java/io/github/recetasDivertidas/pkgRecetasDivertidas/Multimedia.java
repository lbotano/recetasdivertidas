package io.github.recetasDivertidas.pkgRecetasDivertidas;

import javafx.scene.image.Image;

public class Multimedia {
    private String url;
    private Image img;

    public Multimedia(String url){
        this.url = url;
        img = new Image(url);
    }

    public String getUrl() {
        return url;
    }

    public Image getImg() {
        return img;
    }
}
