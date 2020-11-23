package io.github.recetasDivertidas.pkgComponentes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class MultimediaCargar extends HBox {
    @FXML TextField txtMultimedia;
    @FXML Button btnAgregar;

    public MultimediaCargar() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/multimedia_cargar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    public void initialize(){

    }

    public void agregar(ActionEvent actionEvent) {
    }
}
