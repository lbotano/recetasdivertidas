package io.github.recetasDivertidas.pkgRecetasDivertidas;

import javafx.scene.image.Image;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Multimedia {
    private int id;
    private final String url;
    private final Image img;
    private final boolean esVideoYoutube;
    private String urlEmbeed = "";

    public static final String YOUTUBE_IMG = "https://i.imgur.com/XvIKUKa.png";
    private static final String pattern =
            "https?://(?:www\\.)?youtu(?:\\.be/|be\\.com/(?:watch\\?v=|v/|embed/|user/(?:[\\w#]+/)+))([^&#?\\n]+)";

    // Multimedia para enviar
    public Multimedia(String url) {
        this.url = url;

        // Define si es un video de youtube
        Pattern youtubeUrlPattern = Pattern.compile(
                pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = youtubeUrlPattern.matcher(this.url);

        if (matcher.matches() && matcher.group().length() > 0) {
            this.esVideoYoutube = true;
            //urlEmbeed = "https://www.youtube.com/embed/" + matcher.group(1);
            urlEmbeed = "https://www.google.com";
        } else {
            this.esVideoYoutube = false;
        }

        // Si es un video de youtube, hace que la foto sea una miniatura.
        this.img = new Image(esVideoYoutube ? YOUTUBE_IMG : url);
    }

    // Multimedia para recibir
    public Multimedia(int id, String url) {
        this(url);
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public Image getImg() {
        return img;
    }

    public boolean esVideoYoutube() {
        return esVideoYoutube;
    }
}
