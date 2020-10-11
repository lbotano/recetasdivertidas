package pkgLogin;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import pkgRecetasDivertidas.RecetasDivertidas;

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
        btnLogin.setOnAction(e -> checkEntries(tbUsername,tbPassword));

        btnRegister = new Button();
        btnRegister.setText("Registrarse");
        btnRegister.setPrefSize(120,10);
        btnRegister.setOnAction(e -> checkRegister());

        hbox.getChildren().addAll(btnLogin,btnRegister);

        return hbox;
    }

    private void checkEntries(TextField tbU, TextField tbP){
        //Si la consulta es true entonces pasa a la siguiente stage
        if(consLogin(tbU.getText(),tbP.getText())){
            //actions
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hubo un problema");
            alert.setHeaderText("Los datos no coinciden con nuestra base de datos");
            //Usar string message
            alert.setContentText("El nombre de usuario o contraseña son incorrectos");
            alert.initOwner(RecetasDivertidas.window.getScene().getWindow());
            alert.showAndWait();
        }
    }

    private void checkRegister(){
        if(!register.isShowing()){
            register.show();
        }
    }

    private boolean consLogin(String Usr, String Pwd){

        return false; //aca va la consulta;
    }

}
