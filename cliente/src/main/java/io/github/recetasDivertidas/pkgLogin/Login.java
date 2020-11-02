package io.github.recetasDivertidas.pkgLogin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgAplicacion.Aplicacion;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Login {

    @FXML TextField txtUsuario;
    @FXML TextField txtContrasena;
    private boolean admin = false;

    @FXML
    public void login() {
        try {
            if (consLogin(txtUsuario.getText(), txtContrasena.getText())) {
                Alerta alerta = new Alerta(Alert.AlertType.CONFIRMATION, "Bienvenidx de nuevo!",
                        "Identidad confirmada con éxito");
                alerta.showAndWait();

                // "Mostrá el inicio de la app (si es o no admin) y ocultá el login"
                RecetasDivertidas recetasDivertidas = new RecetasDivertidas(admin, txtContrasena.getText());
                recetasDivertidas.show();
                Aplicacion.hide();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    public void registrarse() {
        try {
            if (Conexion.isSvResponse()) {
                Stage registro = new Stage();
                Pane rootRegistro = (Pane) FXMLLoader.load(getClass().getResource("/fxml/registro.fxml"));
                Scene escenaRegistro = new Scene(rootRegistro);
                registro.setScene(escenaRegistro);
                registro.show();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private boolean consLogin(String Usr, String Pwd) throws IOException {
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
                admin = ans.get(1).equals("true");

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
