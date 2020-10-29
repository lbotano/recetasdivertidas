package io.github.recetasDivertidas.pkgLogin;

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
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Register extends Stage {
    private TextField[] inputRegistro;
    private PasswordField pwdRegistro;
    private ComboBox<String> genero;
    private ComboBox<PreguntaSeguridad> preguntaSeguridad;

    public Register() throws IOException {
        super();

        VBox vbox = getLayout();
        vbox.setPadding(new Insets(12, 12, 12, 12));
        vbox.setSpacing(10);

        Scene scnRegister = new Scene(vbox, 300, 300);
        setScene(scnRegister);
        getIcons().add(new Image(getClass().getResourceAsStream("/logo_chiquito.png")));
        setTitle("Registrate!");
        this.initModality(Modality.APPLICATION_MODAL);
        this.setHeight(588);
        this.setWidth(290);
        setResizable(false);
    }

    private VBox getLayout() throws IOException {
        VBox vbox = new VBox(8);
        //---------------------------------------PasswordField Section BEGIN--------------------------------------------
        pwdRegistro = new PasswordField();
        pwdRegistro.setPrefSize(250,10);
        pwdRegistro.setPromptText("Escriba su contraseña aqui");
        //---------------------------------------PasswordField Section END--------------------------------------------

        //---------------------------------------TextField Section BEGIN------------------------------------------------
        inputRegistro = new TextField[5];

        for (int i = 0; i < inputRegistro.length ; i++){
            inputRegistro[i] = new TextField();
            inputRegistro[i].setPrefSize(250,10);
        }
        inputRegistro[0].setPromptText("Escriba su nombre de usuario aqui");
        inputRegistro[1].setPromptText("Escriba su respuesta de seguridad aqui");
        inputRegistro[2].setPromptText("Escriba su nombre de pila aqui");
        inputRegistro[3].setPromptText("Escriba su apellido aqui");
        inputRegistro[4].setPromptText("Escriba su mail aqui");
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
                de maximo 50 caracteres de largo
                """
        );
        tooltip[2].setText(
                """
                Su nombre no puede exceder los 50
                caracteres
                """
        );
        tooltip[3].setText(
                """
                Su apelllido no puede exceder los 50
                caracteres
                """
        );
        tooltip[4].setText(
                """
                Asegurese de escribir una
                direccion de mail real
                """
        );
        tooltip[5].setText(
                """
                Su contraseña debe ser de 
                8 a 50 caracteres de largo
                """
        );
        for(int i = 0; i < tooltip.length ; i++){
            if(i < inputRegistro.length){
                inputRegistro[i].setTooltip(tooltip[i]);
            }else{
                pwdRegistro.setTooltip(tooltip[i]);
            }
        }

        Image img = new Image(getClass().getResourceAsStream("/atention.png"));
        ImageView[] imageViews = new ImageView[tooltip.length];

        //Aca instanciamos el array de imageview, que aunque muestren la misma imagen, si no lo haces asi se re bugea
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i] = new ImageView(img);
            imageViews[i].setFitHeight(30);
            imageViews[i].setFitWidth(30);
        }

        for (int i = 0; i < tooltip.length ; i++){
           tooltip[i].setGraphic(imageViews[i]);
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
        genero = new ComboBox<>(options);
        genero.setPromptText("Género");
        genero.setPrefSize(250,10);

        preguntaSeguridad = new ComboBox<>();
        if (Conexion.isSvResponse()) {
            ArrayList<PreguntaSeguridad> resPreguntas = getPreguntasSeguridad();
            preguntaSeguridad.getItems().addAll(resPreguntas);
        }else{
            Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Hubo un error","Fallo de conexion con el servidor");
        }
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
            if (i == 5){
                vbox.getChildren().add(pwdRegistro);
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
            try {
                register();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        vbox.getChildren().add(btnRegister);
        //---------------------------------------Añadir al VBox Section END---------------------------------------------

        return vbox;
    }

    private void register() throws IOException {
        if(consRegister()){
            Alerta alerta = new Alerta(Alert.AlertType.CONFIRMATION,"Bienvenidx a Recetas Divertidas","Datos registrados con exito!");
            alerta.showAndWait();
        }
    }

    private boolean consRegister() throws IOException {
        ArrayList<String> message = new ArrayList<>();
        message.add("REGISTRO");
        boolean consRes = false;
        Alerta alerta;

        if (corroborarDatos()){
            message.addAll(getItems());

            ArrayList<String> ans = Conexion.sendMessage(message);
            if(ans.size() != 0) {
                switch (ans.get(0)) {
                    case "REGISTEROK" -> consRes = true;
                    case "REGISTERFAIL" -> {
                        alerta = new Alerta(Alert.AlertType.ERROR, "Error al registrarse", ans.get(1));
                        alerta.showAndWait();
                    }
                    case "MESSAGEERROR" -> {
                        alerta = new Alerta(Alert.AlertType.ERROR, "Hubo un error en la comunicacion con el server", ans.get(1));
                        alerta.showAndWait();
                    }
                    case "FORMATERROR" -> {
                        alerta = new Alerta(Alert.AlertType.ERROR, "Error en el formato", "Consulte a su tecnico de cabecera");
                        System.out.println(ans.get(0));
                        alerta.showAndWait();
                    }
                    default -> {
                        alerta = new Alerta(Alert.AlertType.ERROR, "Esto es vergonzoso", "Esto no deberia haber sucedido");
                        alerta.showAndWait();
                    }
                }
            }
        }else{
            Alerta alert = new Alerta(Alert.AlertType.ERROR, "Datos erroneos",
                    "Corrobore que haya completado bien el formulario");
            alert.showAndWait();
        }

        return consRes;
    }

    private ArrayList<String> getItems(){
        ArrayList<String> itemList = new ArrayList<>();

        itemList.add(inputRegistro[0].getText());
        itemList.add(String.valueOf(preguntaSeguridad.getValue().getId()));
        itemList.add(inputRegistro[1].getText());
        itemList.add(inputRegistro[2].getText());
        itemList.add(inputRegistro[3].getText());
        itemList.add(pwdRegistro.getText());
        itemList.add(inputRegistro[4].getText());
        itemList.add(getGenero());

        return itemList;
    }

    private String getGenero(){
        String res = "";

        switch (genero.getValue()){
            case "Masculino" -> {
                res = "0";
            }
            case "Femenino" ->{
                res = "1";
            }
            case "Otro" ->{
                res = "2";
            }
        }

        return res;
    }

    private boolean corroborarDatos(){
        boolean datosok = true;

        for (int i = 0; i < inputRegistro.length; i++)
        {
            switch (i)
            {
                case 0:
                    if (!(inputRegistro[i].getText().trim().length() <= 32 && inputRegistro[i].getText().trim().length() >= 3 )){
                        inputRegistro[i].setStyle("-fx-control-inner-background: #FFCCCC");
                        datosok = false;
                    }else {
                        inputRegistro[i].setStyle("-fx-control-inner-background: #CCFFCC");
                    }

                    break;
                case 1:
                    if (!(inputRegistro[i].getText().length() <= 64 && inputRegistro[i].getText().length() >= 1 )){
                        inputRegistro[i].setStyle("-fx-control-inner-background: #FFCCCC");
                        datosok = false;
                    }else {
                        inputRegistro[i].setStyle("-fx-control-inner-background: #CCFFCC");
                    }

                    break;
                case 2:
                case 3:
                    if (!(inputRegistro[i].getText().length() <= 50 && inputRegistro[i].getText().length() >= 1 )){
                        inputRegistro[i].setStyle("-fx-control-inner-background: #FFCCCC");
                        datosok = false;
                    }else {
                        inputRegistro[i].setStyle("-fx-control-inner-background: #CCFFCC");
                    }

                    break;
                case 4:
                    if (!validarMail(inputRegistro[i].getText())){
                        inputRegistro[i].setStyle("-fx-control-inner-background: #FFCCCC");
                        datosok = false;
                    }else {
                        inputRegistro[i].setStyle("-fx-control-inner-background: #CCFFCC");
                    }

                    break;
            }
        }

        if(!(pwdRegistro.getText().length() >= 8)){
            pwdRegistro.setStyle("-fx-control-inner-background: #FFCCCC");
            datosok = false;
        }else {
            pwdRegistro.setStyle("-fx-control-inner-background: #CCFFCC");
        }

        if(genero.getValue() == null || preguntaSeguridad.getValue() == null ){
            datosok = false;
        }

        return datosok;
    }

    public boolean validarMail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private ArrayList<PreguntaSeguridad> getPreguntasSeguridad() throws IOException {
        //Se crea el arraylist y se le añade el mensaje que se manda
        ArrayList<String> message = new ArrayList<>();
        message.add("PREGUNTASSEG");
        //Espera a que le manden una respuesta
        ArrayList<String> ans = Conexion.sendMessage(message);
        //Se crea el arraylist que contiene las preguntas de seguridad
        ArrayList<PreguntaSeguridad> preguntas = new ArrayList<>();

        switch(ans.get(0)){
            case "PREGUNTASSEG" ->{
                int i= 1;
                while(i < ans.size()){
                    preguntas.add(new PreguntaSeguridad(Integer.parseInt(ans.get(i)), ans.get(i+1)));
                    i = i+2;
                }
            }
            case "MESSAGEERROR" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                        "Hubo un problema al comunicarse con el servidor");
                alerta.showAndWait();
            }
            case "PREGUNTASSEGFAIL" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error con al obtener preguntas de seguridad", ans.get(1));
                System.out.println(ans.get(0));
                alerta.showAndWait();
            }
        }

        return preguntas;
    }
}