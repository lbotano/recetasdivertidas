package pkgRecetasDivertidas;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Alerta extends Alert {
    Image img;
    ImageView imgView;

    public Alerta(AlertType alertType,String header, String content) {
        super(alertType);


        switch (alertType){
            case CONFIRMATION -> {
                img = new Image(getClass().getResourceAsStream("ok.png"));
            }
            case ERROR -> {
                img = new Image(getClass().getResourceAsStream("error.png"));
            }
        }

        //Aca se usa el mensaje de error proporcionado con el servidor
        imgView = new ImageView(img);
        imgView.setSmooth(false);
        this.setTitle("Recetas divertidas");
        this.setHeaderText(header);
        this.setGraphic(imgView);
        this.setContentText(content);
        this.initOwner(RecetasDivertidas.window.getScene().getWindow());
    }
}
