package pkgLogin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Register extends Stage {

    private Scene scnRegister;
    private HBox hbox;

    public Register(){
        super();

        hbox = new HBox();
        hbox.setPadding(new Insets(18, 12, 18, 12));
        hbox.setSpacing(10);
        VBox lblvbox = getLabelLayout();
        VBox txtvbox = getTextLayout();
        hbox.getChildren().addAll(lblvbox,txtvbox);

        scnRegister = new Scene(hbox,430,350);
        setScene(scnRegister);
        getIcons().add(new Image("https://cdn.discordapp.com/attachments/453644623168929803/764301261523779614/logo_chiquito.png"));
        setTitle("Registrate!");
        setResizable(true);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setHeight(350);
        this.setWidth(430);
    }

    private VBox getTextLayout(){
        VBox vbox = new VBox(8);

        TextField[] inputRegistro = new TextField[6];

        for (int i = 0; i < inputRegistro.length ; i++){
            inputRegistro[i] = new TextField();
            inputRegistro[i].setPrefSize(250,10);
        }
        inputRegistro[0].setPromptText("Escriba su nombre de usuario aqui");
        inputRegistro[1].setPromptText("Escriba su respuesta de seguridad aqui");
        inputRegistro[2].setPromptText("Escriba su nombre de pila aqui");
        inputRegistro[3].setPromptText("Escriba su apellido aqui");
        inputRegistro[4].setPromptText("Escriba su contraseña aqui");
        inputRegistro[5].setPromptText("Escriba su mail aqui");

        for(int i = 0; i < inputRegistro.length ; i++){
            vbox.getChildren().add(inputRegistro[i]);
        }

        //Pedi las preguntas de seguridad del server
        //Carga un nuevo combobox de genero
        //Segui haciendo el layout
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Masculino",
                        "Femenino",
                        "Otro"
                );
        ComboBox genero = new ComboBox(options);
        genero.setPromptText("Género");
        vbox.getChildren().add(genero);

        Button btn = new Button("pRESS ME");
        btn.setOnAction(e -> System.out.println("width:" + this.getWidth() + " height:" + this.getHeight()));
        vbox.getChildren().add(btn);
        /*ComboBox preguntaSeguridad = new ComboBox();


        for (int i = 0; i < 6; i++) {
            if(i == 1){
                vbox.getChildren().add(preguntaSeguridad);
            }
            vbox.getChildren().add(inputRegistro[i]);
        }*/

        return vbox;
    }

    private VBox getLabelLayout(){
        VBox vbox = new VBox(16);
        Label[] lblRegistro = new Label[8];

        for (int i = 0; i < lblRegistro.length ; i++){
            lblRegistro[i] = new Label();
            //lblRegistro[i].setPrefSize(250,10);
        }
        lblRegistro[0].setText("Nombre de Usuario:");
        lblRegistro[1].setText("Pregunta de seguridad:");
        lblRegistro[2].setText("Respuesta de seguridad:");
        lblRegistro[3].setText("Nombre de pila:");
        lblRegistro[4].setText("Apellido:");
        lblRegistro[5].setText("Contraseña:");
        lblRegistro[6].setText("Mail:");
        lblRegistro[7].setText("Genero:");


        for(int i = 0; i < lblRegistro.length ; i++){
            vbox.getChildren().add(lblRegistro[i]);
        }


        return vbox;
    }

    private void checkEntries(){

    }
}
