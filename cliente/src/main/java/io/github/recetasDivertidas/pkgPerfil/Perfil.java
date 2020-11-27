package io.github.recetasDivertidas.pkgPerfil;

import com.sun.javafx.iio.ios.IosDescriptor;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.ResultadoBusqueda;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.PreguntaSeguridad;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;

public class Perfil {
    @FXML
    Label lblPregunta;
    @FXML
    VBox vboxMisRecetas;
    @FXML
    PasswordField txtNewPwd;
    @FXML
    TextField txtRespuesta;
    @FXML
    Button btnCambiarPwd;
    @FXML
    Label lblUsuario;

    PreguntaSeguridad preguntaSeguridad;

    @FXML
    public void initialize() {
        lblUsuario.setText(RecetasDivertidas.username);
        showRecetasUsuario();
        putPreguntaSeguridad();
    }

    private void putPreguntaSeguridad() {
        ArrayList<String> msg = new ArrayList<>();
        msg.add("USUPREGSEG");
        msg.add(RecetasDivertidas.username);
        try {
            ArrayList<String> ans = Conexion.sendMessage(msg);
            switch (ans.get(0)) {
                case "RESPUSUPREGSEG" -> {
                    preguntaSeguridad = new PreguntaSeguridad(Integer.parseInt(ans.get(1)),ans.get(2));
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
    private void cambiarPwd() {
        if (txtNewPwd.getText().length() >= 8 && txtRespuesta.getText().length() > 0){
            ArrayList<String> msg = new ArrayList<>();
            msg.add("CAMBIARCONTRA");
            msg.add(RecetasDivertidas.username);
            msg.add(txtRespuesta.getText());
            msg.add(txtNewPwd.getText());

            try{
                ArrayList<String> ans = Conexion.sendMessage(msg);
                switch (ans.get(0)){
                    case "CAMBIARCONTRAOK" ->{
                        Alerta a = new Alerta(Alert.AlertType.CONFIRMATION,
                                "Contraseña cambiada con exito",
                                "Su contraseña se ha cambiado con exito");
                        a.showAndWait();
                    }
                    case "CAMBIARCONTRAFAIL" ->{
                        Alerta a = new Alerta(Alert.AlertType.ERROR,"Ha ocurrido un error", ans.get(1));
                        a.showAndWait();
                    }
                }
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                Alerta a = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "Hubo un error inesperado");
                a.showAndWait();
            }

        }

    }
}
