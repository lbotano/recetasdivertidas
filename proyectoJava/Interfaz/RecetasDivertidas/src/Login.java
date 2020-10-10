import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class Login extends BorderPane {

    public Button btnLogin;
    public Button btnRegister;
    public TextField tbUsername;
    public TextField tbPassword;
    private Label lblUsername;
    private Label lblPassword;

    public Login(){
        super();

        setCenter(addGridPane());
        setBottom(addHBox());
        //this.setStyle("-fx-background-color: #BE2500;");
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

    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        //Button setup
        btnLogin = new Button();
        btnLogin.setText("Entrar");
        btnLogin.setPrefSize(120,10);

        btnRegister = new Button();
        btnRegister.setText("Registrarse");
        btnRegister.setPrefSize(120,10);

        hbox.getChildren().addAll(btnLogin,btnRegister);

        return hbox;
    }
}
