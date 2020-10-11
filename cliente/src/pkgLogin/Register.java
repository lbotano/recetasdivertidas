package pkgLogin;

import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Register extends Stage {

    private Scene scnRegister;
    private TextField[] inputRegistro;
    private Label[] lblRegistro;
    private HBox hbox;

    public Register(){
        super();

        hbox = new HBox();
        hbox.getChildren().addAll(getLabelLayout(),getTextLayout());

        scnRegister = new Scene(hbox,800,200);
        setScene(scnRegister);
        getIcons().add(new Image("https://cdn.discordapp.com/attachments/453644623168929803/764301261523779614/logo_chiquito.png"));
        setTitle("Registrate!");
        setResizable(false);
    }

    private VBox getTextLayout(){
        VBox vbox = new VBox();
        ComboBox preguntaSeguridad = new ComboBox();

        TextField[] inputRegistro = new TextField[6];
        inputRegistro[0].setPromptText("Escriba su nombre de usuario aqui");
        inputRegistro[1].setPromptText("Escriba su respuesta de seguridad aqui");
        inputRegistro[2].setPromptText("Escriba su nombre de pila aqui");
        inputRegistro[3].setPromptText("Escriba su apellido aqui");
        inputRegistro[4].setPromptText("Escriba su contraseña aqui");
        inputRegistro[5].setPromptText("Escriba su mail aqui");

        //Pedi las preguntas de seguridad del server
        //Carga un nuevo combobox de genero
        //Segui haciendo el layout
        for (int i = 0; i < 6; i++) {
            if(i == 1){
                vbox.getChildren().add(preguntaSeguridad);
            }
            vbox.getChildren().add(inputRegistro[i]);
        }

        return vbox;
    }

    private VBox getLabelLayout(){
        VBox vbox = new VBox();



        return vbox;
    }

    private void checkEntries(){

    }
}
