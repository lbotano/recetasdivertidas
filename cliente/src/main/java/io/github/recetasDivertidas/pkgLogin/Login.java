package io.github.recetasDivertidas.pkgLogin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgAplicacion.Aplicacion;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Login {

    @FXML TextField txtUsuario;
    @FXML TextField txtContrasena;
    private String username = "";

    @FXML
    private void login() {
        try {
            if (consLogin(txtUsuario.getText().trim(), txtContrasena.getText())) {
                Alerta alerta = new Alerta(Alert.AlertType.CONFIRMATION, "Bienvenidx de nuevo!",
                        "Identidad confirmada con éxito");
                alerta.showAndWait();

                RecetasDivertidas.username = username;

                Stage recetasdivertidas = new Stage();
                Pane rootRecetasDivertidas = FXMLLoader.load(getClass().getResource("/fxml/recetas_divertidas.fxml"));
                Scene scene = new Scene(rootRecetasDivertidas);

                recetasdivertidas.setTitle("Recetas Divertidas");
                recetasdivertidas.getIcons().add(new Image(getClass().getResourceAsStream("/logo_chiquito.png")));

                recetasdivertidas.setScene(scene);
                recetasdivertidas.show();

                Aplicacion.hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void registrarse() {
        if (Conexion.isSvResponse()) {
            Stage stageRegistro = new Stage();
            // Evitar que la ventana se abra más de una vez
            stageRegistro.initModality(Modality.APPLICATION_MODAL);

            try {
                Pane rootRegistro = FXMLLoader.load(getClass().getResource("/fxml/registro.fxml"));

                Scene escenaRegistro = new Scene(rootRegistro);

                stageRegistro.setScene(escenaRegistro);
                stageRegistro.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleKeyLogin(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) login();
    }

    private boolean consLogin(String Usr, String Pwd) throws IOException, ClassNotFoundException {
        ArrayList<String> login = new ArrayList<>();
        Alerta alert;

        login.add("LOGIN");
        login.add(Usr);
        login.add(Pwd);

        ArrayList<String> ans = Conexion.sendMessage(login);

        if (ans.size() == 0) {
            return false;
        }

        switch (ans.get(0)) {
            case "LOGINFAIL" -> {
                alert = new Alerta(Alert.AlertType.ERROR, "Error al logearse", ans.get(1));
                alert.showAndWait();
            }
            case "MESSAGEERROR" -> {
                alert = new Alerta(Alert.AlertType.ERROR, "Error al logearse",
                        "Hubo un problema al enviar la peticion");
                alert.showAndWait();
            }
            case "LOGINOK" -> {
                username = txtUsuario.getText();
                RecetasDivertidas.logueadoComoAdmin = Boolean.parseBoolean(ans.get(1));
                return true;
            }
            case "ELEMENTBLANK" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "El mensaje contenia espacios en blanco");
                alerta.showAndWait();
            }
            case "FORMATERROR" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "Hubo un problema en el formato del mensaje");
                alerta.showAndWait();
            }
        }
        return false;
    }
}
