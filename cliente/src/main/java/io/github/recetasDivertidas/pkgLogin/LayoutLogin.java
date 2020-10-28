package io.github.recetasDivertidas.pkgLogin;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgInicio.RecetasDivertidas;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgAplicacion.Aplicacion;

import java.io.IOException;
import java.util.ArrayList;

public class LayoutLogin extends BorderPane {

    private TextField tbUsername;
    private TextField tbPassword;
    private Register register;
    private boolean admin = false;

    public LayoutLogin() {
        this.setTop(addGridPane());
        this.setBottom(addHBox());
    }

    private GridPane addGridPane() {

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);


        //Username setup
        Label lblUsername = new Label("Usuario:");
        grid.add(lblUsername, 0, 0);

        tbUsername = new TextField();
        tbUsername.setPrefSize(180, 10);
        tbUsername.setPromptText("Escriba su usuario aqui");
        grid.add(tbUsername, 1, 0);

        //Password setup
        Label lblPassword = new Label("Contraseña:");
        grid.add(lblPassword, 0, 1);

        tbPassword = new TextField();
        tbPassword.setPrefSize(180, 10);
        tbPassword.setPromptText("Escriba su contraseña aqui");
        grid.add(tbPassword, 1, 1);

        return grid;
    }

    private HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        //Button setup
        Button btnLogin = new Button();
        btnLogin.setText("Entrar");
        btnLogin.setPrefSize(120, 10);
        btnLogin.setOnAction(e -> {
            try {
                if (consLogin(tbUsername.getText(), tbPassword.getText())) {
                    Alerta alerta = new Alerta(Alert.AlertType.CONFIRMATION, "Bienvenidx de nuevo!",
                            "Identidad confimada con exito");
                    alerta.showAndWait();

                    //Basicamente lo que esto dice es "Mostra el inicio de la app(si es o no admin) y oculta el login"
                    RecetasDivertidas recetasDivertidas = new RecetasDivertidas(admin);
                    recetasDivertidas.show();
                    Aplicacion.hide();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        Button btnRegister = new Button();
        btnRegister.setText("Registrarse");
        btnRegister.setPrefSize(120, 10);
        btnRegister.setOnAction(e -> {
            try {
                //Sacar el NOT de aca o poner un NOT para probar el registro
                if (!Conexion.isSvResponse()) {
                    register = new Register();
                    register.show();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        hbox.getChildren().addAll(btnLogin, btnRegister);

        return hbox;
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
            case "LOGINFAIL":
                alert = new Alerta(Alert.AlertType.ERROR, "Error al logearse", ans.get(1));
                alert.showAndWait();

                return false;
            case "MESSAGEERROR":
                alert = new Alerta(Alert.AlertType.ERROR, "Error al logearse",
                        "Hubo un problema al enviar la peticion");
                alert.showAndWait();

                return false;
            case "LOGINOK":
                admin = ans.get(1).equals("true");

                return true;
            default:
                return false;
        }
    }

}
