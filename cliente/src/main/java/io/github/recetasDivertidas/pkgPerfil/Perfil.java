package io.github.recetasDivertidas.pkgPerfil;

import com.sun.javafx.iio.ios.IosDescriptor;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.ResultadoBusqueda;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.PreguntaSeguridad;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Perfil {
    @FXML
    VBox vboxMisRecetas;
    @FXML
    Label lblUsuario;
    private Label lblPwd;
    private Label lblPregunta;
    private PasswordField txtNewPwd;
    private TextField txtRespuesta;
    private Button btnCambiarPwd;

    PreguntaSeguridad preguntaSeguridad;

    @FXML
    public void initialize() {
        lblPwd = new Label("Nueva contraseña:");

        txtNewPwd = new PasswordField();
        txtNewPwd.setPromptText("Escriba su nueva contraseña");

        lblUsuario.setText(RecetasDivertidas.username);
        lblPregunta = new Label();
        putPreguntaSeguridad();

        txtRespuesta = new TextField();
        txtRespuesta.setPromptText("Escriba su respuesta");

        btnCambiarPwd = new Button("Cambiar Contraseña");
        btnCambiarPwd.setPrefWidth(300);

        showRecetasUsuario();
    }

    @FXML
    private void pestanaCambiarPwd() {
        Stage stage = new Stage();
        btnCambiarPwd.setOnAction(e -> cambiarPwd(stage));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(lblPregunta, txtRespuesta, lblPwd, txtNewPwd, btnCambiarPwd);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setFillWidth(true);
        vbox.setSpacing(10);
        vbox.prefHeight(100);
        vbox.prefWidth(300);

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.setTitle("Cambiar Contraseña");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/logo_recetas_divertidas.png")));
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    private void putPreguntaSeguridad() {
        ArrayList<String> msg = new ArrayList<>();
        msg.add("USUPREGSEG");
        msg.add(RecetasDivertidas.username);
        try {
            ArrayList<String> ans = Conexion.sendMessage(msg);
            switch (ans.get(0)) {
                case "RESPUSUPREGSEG" -> {
                    preguntaSeguridad = new PreguntaSeguridad(Integer.parseInt(ans.get(1)), ans.get(2));
                    lblPregunta.setText(preguntaSeguridad.getPregunta());
                }
                case "RESPUSUPREGSEGFAIL" -> {
                    Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error con el servidor", ans.get(1));
                    alerta.showAndWait();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Alerta a = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            a.showAndWait();
        }
    }


    private void showRecetasUsuario() {
        vboxMisRecetas.getChildren().clear();

        try {
            ArrayList<Receta> recetasUsuario = Receta.getRecetasUsuario(RecetasDivertidas.username);

            if (recetasUsuario.size() > 0) {
                for (Receta r : recetasUsuario) {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/componentes/resultado_busqueda.fxml"));
                    HBox paneReceta = fxmlLoader.load();
                    ResultadoBusqueda controllerResultadoBusqueda = fxmlLoader.getController();
                    controllerResultadoBusqueda.ponerReceta(r);
                    vboxMisRecetas.getChildren().add(paneReceta);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alerta a = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            a.showAndWait();
        }
    }

    @FXML
    private void cambiarPwd(Stage stage) {
        if (checkItems()) {
            ArrayList<String> msg = new ArrayList<>();
            msg.add("CAMBIARCONTRA");
            msg.add(RecetasDivertidas.username);
            msg.add(txtRespuesta.getText());
            msg.add(txtNewPwd.getText());

            try {
                ArrayList<String> ans = Conexion.sendMessage(msg);
                switch (ans.get(0)) {
                    case "CAMBIARCONTRAOK" -> {
                        Alerta a = new Alerta(Alert.AlertType.CONFIRMATION,
                                "Contraseña cambiada con exito",
                                "Su contraseña se ha cambiado con exito");
                        a.showAndWait();
                        stage.close();
                        txtRespuesta.setText("");
                        txtRespuesta.setStyle("");
                        txtNewPwd.setText("");
                        txtNewPwd.setStyle("");
                    }
                    case "CAMBIARCONTRAFAIL" -> {
                        Alerta a = new Alerta(Alert.AlertType.ERROR, "Ha ocurrido un error", ans.get(1));
                        a.showAndWait();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                Alerta a = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "Hubo un error inesperado");
                a.showAndWait();
            }
        }else{
            Alerta a = new Alerta(Alert.AlertType.ERROR,
                    "Formulario mal llenado",
                    "Asegurese de haber llenado bien el formulario");
            a.showAndWait();
        }
    }

    private boolean checkItems(){
        final String STYLE_BUENO = "-fx-control-inner-background: #CCFFCC";
        final String STYLE_MALO = "-fx-control-inner-background: #FFCCCC";
        boolean datos_ok = true;

        if (txtRespuesta.getText().length() < 3){
            txtRespuesta.setStyle(STYLE_MALO);
            datos_ok = false;
        }else{
            txtRespuesta.setStyle(STYLE_BUENO);
        }

        if (txtNewPwd.getText().length() < 8){
            datos_ok = false;
            txtNewPwd.setStyle(STYLE_MALO);
        }else{
            txtNewPwd.setStyle(STYLE_BUENO);
        }

        return datos_ok;
    }
}
