package pkgLogin;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import pkgConexion.Conexion;
import pkgRecetasDivertidas.Alerta;
import pkgRecetasDivertidas.RecetasDivertidas;

import java.io.IOException;
import java.util.ArrayList;

public class LayoutLogin extends BorderPane {

    public Button btnLogin;
    public Button btnRegister;
    public TextField tbUsername;
    public TextField tbPassword;
    private Label lblUsername;
    private Label lblPassword;
    public Register register;
    private String message;
    private LayoutLogin login;


    public LayoutLogin(){
        this.setTop(addGridPane());
        this.setBottom(addHBox());
        //register = new Register();
    }

    private GridPane addGridPane(){

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(8);
        grid.setHgap(10);


        //Username setup
        lblUsername = new Label("Usuario:");
        grid.add(lblUsername,0,0);

        tbUsername = new TextField();
        tbUsername.setPrefSize(180,10);
        tbUsername.setPromptText("Escriba su usuario aqui");
        grid.add(tbUsername,1,0);

        //Password setup
        lblPassword = new Label("Contraseña:");
        grid.add(lblPassword,0,1);

        tbPassword = new TextField();
        tbPassword.setPrefSize(180,10);
        tbPassword.setPromptText("Escriba su contraseña aqui");
        grid.add(tbPassword,1,1);

        return grid;
    }

    private HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        //Button setup
        btnLogin = new Button();
        btnLogin.setText("Entrar");
        btnLogin.setPrefSize(120,10);
        btnLogin.setOnAction(e -> {
            try {
                checkEntries(tbUsername,tbPassword);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        btnRegister = new Button();
        btnRegister.setText("Registrarse");
        btnRegister.setPrefSize(120,10);
        btnRegister.setOnAction(e -> {
            try {
                register = new Register();
                register.show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        hbox.getChildren().addAll(btnLogin,btnRegister);

        return hbox;
    }

    private void checkEntries(TextField tbU, TextField tbP) throws IOException {
        //Si la consulta es true entonces pasa a la siguiente stage
        if(consLogin(tbU.getText(),tbP.getText())){
            Alerta alerta = new Alerta(Alert.AlertType.CONFIRMATION,"Bienvenidx de nuevo!","Identidad confimada con exito");
            alerta.showAndWait();
            //Aca iria la proxima stage, si tuviera una..
        }
    }

    private boolean consLogin(String Usr, String Pwd) throws IOException {
        ArrayList<String> login = new ArrayList<>();

        login.add("LOGIN");
        login.add(Usr);
        login.add(Pwd);

        ArrayList<String> ans = Conexion.sendMessage(login);

        if (ans.size() != 0){
            if(ans.get(0).equals("LOGINFAIL") || ans.get(0).equals("MESSAGEERROR")) {
                Alerta alert = new Alerta(Alert.AlertType.ERROR,"Hubo un problema con el servidor",ans.get(1));
                alert.showAndWait();
                return false;
            }
        }else{
            return false;
        }

        return true;
    }

}
