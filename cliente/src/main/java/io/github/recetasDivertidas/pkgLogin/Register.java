package io.github.recetasDivertidas.pkgLogin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Register{
    ArrayList<TextField> textfields = new ArrayList<>();

    @FXML TextField txtUsuario;
    @FXML TextField txtContrasena;
    @FXML TextField txtMail;
    @FXML TextField txtNombre;
    @FXML TextField txtApellido;
    @FXML ComboBox<String> cbGenero;
    @FXML ComboBox<PreguntaSeguridad> cbPregunta;
    @FXML TextField txtRespuesta;
    @FXML public Button btnRegistrarse;

    @FXML
    private void initialize() {
        // Array con los textbos para poder hacer bucles después
        textfields.add(txtUsuario);
        textfields.add(txtContrasena);
        textfields.add(txtMail);
        textfields.add(txtNombre);
        textfields.add(txtApellido);
        textfields.add(txtRespuesta);

        // Poner las respuestas de seguridad en el ComboBox correspondiente
        if (Conexion.isSvResponse()) {
            try {
                ArrayList<PreguntaSeguridad> resPreguntas = getPreguntasSeguridad();
                cbPregunta.getItems().addAll(resPreguntas);
            } catch (IOException e) {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Hubo un error",
                        "Fallo de conexión con el servidor.");
                alerta.showAndWait();
            }
        } else {
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Hubo un error",
                    "Fallo de conexión con el servidor.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void registrarse() {
        // Trimea todos los inputs
        for (TextField tf : textfields) {
            tf.setText(tf.getText().trim());
        }

        // Trata de registrarse
        if(consRegister()){
            Alerta alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                    "Bienvenidx a Recetas Divertidas","¡Datos registrados con exito!");
            alerta.showAndWait();
            Stage stage = (Stage) btnRegistrarse.getScene().getWindow();
            stage.close();
        }
    }

    private boolean consRegister() {
        Alerta alerta;

        ArrayList<String> message = new ArrayList<>();
        message.add("REGISTRO");

        if (corroborarDatos()){
            message.addAll(getDatos());
            try {
                ArrayList<String> ans;
                ans = Conexion.sendMessage(message);
                if(ans.size() != 0) {
                    switch (ans.get(0)) {
                        case "REGISTEROK" -> { return true; }
                        case "REGISTERFAIL" -> {
                            alerta = new Alerta(Alert.AlertType.ERROR, "Error al registrarse", ans.get(1));
                            alerta.showAndWait();
                        }
                        case "MESSAGEERROR" -> {
                            alerta = new Alerta(Alert.AlertType.ERROR, "Hubo un error en la comunicación con el server", ans.get(1));
                            alerta.showAndWait();
                        }
                        case "FORMATERROR" -> {
                            alerta = new Alerta(Alert.AlertType.ERROR, "Error en el formato", "Consulte a su técnico de cabecera");
                            System.out.println(ans.get(0));
                            alerta.showAndWait();
                        }
                        case "ELEMENTBLANK" ->{
                            alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                                    "El mensaje contenia espacios en blanco");
                            alerta.showAndWait();
                        }
                        default -> {
                            alerta = new Alerta(Alert.AlertType.ERROR, "Esto es vergonzoso", "Esto no debería haber sucedido");
                            alerta.showAndWait();
                        }
                    }
                }
            } catch (IOException e) {
                alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error fatal", "Hubo un error al conectarse con el servidor.");
                alerta.showAndWait();
            }
        } else {
            Alerta alert = new Alerta(Alert.AlertType.ERROR, "Datos erroneos",
                    "Corrobora que hayas completado bien el formulario");
            alert.showAndWait();
        }

        return false;
    }

    // Devuelve todos los datos de registro para el mensaje al servidor
    private ArrayList<String> getDatos(){
        ArrayList<String> itemList = new ArrayList<>();

        itemList.add(txtUsuario.getText());
        itemList.add(String.valueOf(cbPregunta.getValue().getId()));
        itemList.add(txtRespuesta.getText());
        itemList.add(txtNombre.getText());
        itemList.add(txtApellido.getText());
        itemList.add(txtContrasena.getText());
        itemList.add(getGenero());
        itemList.add(txtMail.getText());

        return itemList;
    }

    private String getGenero(){
        String res = "";

        switch (cbGenero.getValue()){
            case "Masculino" -> res = "0";
            case "Femenino" -> res = "1";
            case "Otro" -> res = "2";
        }

        return res;
    }

    private boolean corroborarDatos(){
        boolean datosOk = true;

        String STYLE_BUENO = "-fx-control-inner-background: #CCFFCC";
        String STYLE_MALO = "-fx-control-inner-background: #FFCCCC";
        if (txtUsuario.getText().length() > 32 || txtUsuario.getText().length() < 3) {
            txtUsuario.setStyle(STYLE_MALO);
            datosOk = false;
        } else {
            txtUsuario.setStyle(STYLE_BUENO);
        }

        if (txtContrasena.getText().length() > 50 || txtContrasena.getText().length() < 8) {
            txtContrasena.setStyle(STYLE_MALO);
            datosOk = false;
        } else {
            txtUsuario.setStyle(STYLE_BUENO);
        }

        if (!validarMail(txtMail.getText())) {
            txtMail.setStyle(STYLE_MALO);
            datosOk = false;
        } else {
            txtMail.setStyle(STYLE_BUENO);
        }

        if (txtNombre.getText().length() > 50 || txtNombre.getText().length() < 1) {
            txtNombre.setStyle(STYLE_MALO);
            datosOk = false;
        } else {
            txtNombre.setStyle(STYLE_BUENO);
        }

        if (txtApellido.getText().length() > 50 || txtApellido.getText().length() < 1) {
            txtApellido.setStyle(STYLE_MALO);
            datosOk = false;
        } else {
            txtApellido.setStyle(STYLE_BUENO);
        }

        if (txtRespuesta.getText().length() > 50 || txtRespuesta.getText().length() < 1) {
            txtRespuesta.setStyle(STYLE_MALO);
            datosOk = false;
        } else {
            txtRespuesta.setStyle(STYLE_BUENO);
        }

        // Verifica que el usuario haya seleccionado algo en los comboboxes
        if (cbGenero.getValue() == null) {
            cbGenero.setStyle(STYLE_MALO);
            datosOk = false;
        } else {
            cbGenero.setStyle(STYLE_BUENO);
        }

        if (cbPregunta.getValue() == null) {
            cbGenero.setStyle(STYLE_MALO);
            datosOk = false;
        } else {
            cbGenero.setStyle(STYLE_BUENO);
        }

        return datosOk;
    }

    public boolean validarMail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) return false;
        return pat.matcher(email).matches();
    }

    private ArrayList<PreguntaSeguridad> getPreguntasSeguridad() throws IOException {
        //Se crea el arraylist y se le añade el mensaje que se manda
        ArrayList<String> message = new ArrayList<>();
        message.add("PREGUNTASSEG");
        //Espera a que le manden una respuesta
        ArrayList<String> answer = Conexion.sendMessage(message);
        //Se crea el arraylist que contiene las preguntas de seguridad
        ArrayList<PreguntaSeguridad> preguntas = new ArrayList<>();
        
        if (answer.size() < 1) throw new IOException();

        switch(answer.get(0)){
            case "PREGUNTASSEG" ->{
                int i = 1;
                while(i < answer.size()){
                    preguntas.add(new PreguntaSeguridad(Integer.parseInt(answer.get(i)), answer.get(i+1)));
                    i += 2;
                }
            }
            case "MESSAGEERROR" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje.",
                        "Hubo un problema al comunicarse con el servidor.");
                alerta.showAndWait();
            }
            case "PREGUNTASSEGFAIL" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error al obtener preguntas de seguridad.", answer.get(1));
                System.out.println(answer.get(0));
                alerta.showAndWait();
            }
            case "ELEMENTBLANK" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje.",
                        "El mensaje contenía espacios en blanco.");
                alerta.showAndWait();
            }
            case "FORMATERROR" ->{
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje.",
                        "Hubo un problema en el formato del mensaje.");
                alerta.showAndWait();
            }
        }

        return preguntas;
    }
}