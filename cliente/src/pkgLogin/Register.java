package pkgLogin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import pkgConexion.Conexion;

import java.io.IOException;
import java.util.ArrayList;

public class Register extends Stage {

    public Register(){
        super();

        VBox vbox = getLayout();
        vbox.setPadding(new Insets(12, 12, 12, 12));
        vbox.setSpacing(10);

        Scene scnRegister = new Scene(vbox, 300, 300);
        setScene(scnRegister);
        getIcons().add(new Image("https://cdn.discordapp.com/attachments/453644623168929803/764301261523779614/logo_chiquito.png"));
        setTitle("Registrate!");
        this.initModality(Modality.APPLICATION_MODAL);
        this.setHeight(588);
        this.setWidth(290);
        setResizable(false);
    }
    //Añadir un checkentries que se fije que las entradas esten bien puestas
    private VBox getLayout(){
        VBox vbox = new VBox(8);

        //---------------------------------------TextField Section BEGIN------------------------------------------------
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
        //---------------------------------------TextField Section END--------------------------------------------------

        //---------------------------------------Tooltip Section BEGIN--------------------------------------------------
        Tooltip[] tooltip = new Tooltip[6];

        for (int i = 0; i < tooltip.length; i++) {
            tooltip[i] = new Tooltip();
        }

        tooltip[0].setText(
                """
                Su nombre de usuario debe ser
                de 3 a 32 caracteres de largo
                """
        );
        tooltip[1].setText(
                """
                Su respuesta de seguridad debe ser
                de 4 a 64 caracteres de largo
                """
        );
        tooltip[2].setText(
                """
                Escriba su nombre como figura
                en el documento
                """
        );
        tooltip[3].setText(
                """
                Escriba su apellido como figura
                en el documento
                """
        );
        tooltip[4].setText(
                """
                Su contraseña debe ser de 
                8 a 50 caracteres de largo
                """
        );
        tooltip[5].setText(
                """
                Asegurese de escribir una
                direccion de mail real
                """
        );

        for(int i = 0 ; i < inputRegistro.length ; i++){
            inputRegistro[i].setTooltip(tooltip[i]);
        }

        Image img = new Image(getClass().getResourceAsStream("error.png"));
        ImageView[] epicasso = new ImageView[tooltip.length];

        //Aca instanciamos el array de imageview, que aunque muestren la misma imagen, si no lo haces asi se re bugea
        for (int i = 0; i < epicasso.length; i++) {
            epicasso[i] = new ImageView(img);
            epicasso[i].setFitHeight(30);
            epicasso[i].setFitWidth(30);
        }

        for (int i = 0 ; i < tooltip.length ; i++){
           tooltip[i].setGraphic(epicasso[i]);
           tooltip[i].setShowDelay(Duration.ZERO);
        }
        //---------------------------------------Tooltip Section END----------------------------------------------------

        //---------------------------------------Label Section BEGIN----------------------------------------------------
        Label[] lblRegistro = new Label[8];

        for (int i = 0; i < lblRegistro.length ; i++){
            lblRegistro[i] = new Label();
            lblRegistro[i].setPrefSize(140,10);
        }
        //Se les dan nombre a los labels despues de instanciarlos
        lblRegistro[0].setText("Nombre de Usuario");
        lblRegistro[1].setText("Pregunta de seguridad");
        lblRegistro[2].setText("Respuesta de seguridad");
        lblRegistro[3].setText("Nombre de pila");
        lblRegistro[4].setText("Apellido");
        lblRegistro[5].setText("Contraseña");
        lblRegistro[6].setText("Mail");
        lblRegistro[7].setText("Genero");

        //---------------------------------------Label Section END------------------------------------------------------

        //---------------------------------------ComboBox Section BEGIN-------------------------------------------------
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Masculino",
                        "Femenino",
                        "Otro"
                );
        ComboBox genero = new ComboBox(options);
        genero.setPromptText("Género");
        genero.setPrefSize(250,10);

        ArrayList<String> optionPS = new ArrayList<String>();
        ComboBox preguntaSeguridad = new ComboBox(options);
        preguntaSeguridad.setPromptText("Elija una pregunta de seguridad");
        preguntaSeguridad.setPrefSize(250,10);

        //---------------------------------------ComboBox Section END---------------------------------------------------

        //---------------------------------------Añadir al VBox Section BEGIN-------------------------------------------
        int i = 0;
        int j = 0;

        do{
            vbox.getChildren().add(lblRegistro[i]);
            if(i == 1){
                vbox.getChildren().add(preguntaSeguridad);
                i++;
                vbox.getChildren().add(lblRegistro[i]);
            }
            if(j < inputRegistro.length) {
                vbox.getChildren().add(inputRegistro[j]);
                j++;
            }
            i++;
        }while(i < lblRegistro.length);

        vbox.getChildren().add(genero);

        Button btnRegister = new Button("Registrarme");
        btnRegister.setPrefSize(250,10);
        btnRegister.setOnAction(e -> {
                inputRegistro[0].setStyle("-fx-control-inner-background: #FFCCCC");
                //register();
        });

        vbox.getChildren().add(btnRegister);
        //---------------------------------------Añadir al VBox Section END-----------------------------------------

        //Boton para saber cuanto mide una stage XD
        /*Button btn = new Button("pRESS ME");
        btn.setOnAction(e -> System.out.println("width:" + this.getWidth() + " height:" + this.getHeight()));
        vbox.getChildren().add(btn);*/

        return vbox;
    }

    private void register() throws IOException {
        if(consRegister()){

        }
    }

    private boolean consRegister() throws IOException {
        ArrayList<String> message = new ArrayList<String>();
        message.add("Register");

        ArrayList<String> ans = Conexion.sendMessage(message);


        return false;
    }

    /*private ArrayList<String> getPreguntasSeguridad(){

    }*/
}
