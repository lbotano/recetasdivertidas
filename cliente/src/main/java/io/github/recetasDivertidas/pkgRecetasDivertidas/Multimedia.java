package io.github.recetasDivertidas.pkgRecetasDivertidas;

import javafx.scene.image.Image;

public class Multimedia {
    private int id;
    private String url;
    private Image img;

    // Multimedia para enviar
    public Multimedia(String url) {
        this.url = url;
        this.img = new Image(url);
    }

    // Multimedia para recibir
    public Multimedia(int id, String url) {
        this.id = id;
        this.url = url;
        this.img = new Image(url);
    }

    public String getUrl() {
        return url;
    }

    public Image getImg() {
        return img;
    }
}
