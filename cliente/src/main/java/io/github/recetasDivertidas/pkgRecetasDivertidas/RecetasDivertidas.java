package io.github.recetasDivertidas.pkgRecetasDivertidas;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgAplicacion.Aplicacion;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Hashtable;

public class RecetasDivertidas {
    public static boolean logueadoComoAdmin;
    public static String username;
    public final static String HOVERED="E7E7E7";
    public final static String EXITED="FFFFFF";
    public BorderPane borderPane;

    @FXML BorderPane bpanePrincipal;

    @FXML private Button btnInicio;
    @FXML private Button btnAdmin;

    private void setPestanaActual(Button b) throws IOException {
        bpanePrincipal.setCenter(FXMLLoader.load(getClass().getResource((String) b.getUserData())));
    }

    @FXML
    private void initialize() {
        if(!logueadoComoAdmin){
            ((Pane) btnAdmin.getParent()).getChildren().remove(btnAdmin);
        }
        try {
            setPestanaActual(btnInicio);
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }


    @FXML
    private void cambiarPestana(ActionEvent event) {
        Button btn = (Button) event.getSource();
        System.out.println("Pressed " + btn.getId());
        try {
            setPestanaActual(btn);
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }
}
