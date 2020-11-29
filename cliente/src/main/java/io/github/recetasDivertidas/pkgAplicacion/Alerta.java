package io.github.recetasDivertidas.pkgAplicacion;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

public class Alerta extends Alert {
    Image img;
    ImageView imgView;
    Dimension screenSize;

    public Alerta(AlertType alertType,String header, String content) {
        super(alertType);

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        switch (alertType){
            case CONFIRMATION:
                img = new Image(getClass().getResourceAsStream("/ok.png"));
                break;
            case ERROR:
                img = new Image(getClass().getResourceAsStream("/error.png"));
                break;
            case NONE:
                img =  new Image(getClass().getResourceAsStream("/atention.png"));
                this.getDialogPane().getButtonTypes().add(ButtonType.OK);
        }

        //Aca se usa el mensaje de error proporcionado con el servidor
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        imgView = new ImageView(img);
        imgView.setSmooth(false);

        this.setTitle("Recetas divertidas");
        this.setHeaderText(header);
        this.setGraphic(imgView);
        this.setContentText(content);
        this.setX(screenSize.getWidth()/2.5);
        this.setY(screenSize.getHeight()/4);
        this.initOwner(Aplicacion.window.getScene().getWindow());
    }
}
