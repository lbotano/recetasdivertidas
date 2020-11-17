package io.github.recetasDivertidas.pkgAdmin;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaIngrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaReceta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FuncionesAdmin {

    private ArrayList<String> mensaje; //objeto donde voy a guardar los mensajes para el server
    private Alerta alerta;
    //Referencia a los objetos de la interfaz grafica
    @FXML ComboBox cmbBorrarCatIngrediente;
    @FXML Button btnBorrarCatIngrediente;

    @FXML ComboBox cmbBorrarIngrediente;
    @FXML Button btnBorrarIngrediente;

    @FXML ComboBox cmbBorrarCatReceta;
    @FXML Button btnBorrarCatReceta;

    @FXML TextField txtBanearUsuario;
    @FXML Button btnBanearUsuario;

    @FXML TextField txtSubirCatIng;
    @FXML Button btnSubirCatIng;

    @FXML CheckComboBox chkcmbCategoriasIngrediente;
    @FXML TextField txtSubirIng;
    @FXML Button btnSubirIng;

    @FXML TextField txtSubirCatRec;
    @FXML Button btnSubirCatRec;

    //las siguientes funciones piden los datos necesarios para enviarle al servidor la peticion y devuelven su respuesta
    //todas las funciones hacen mas o menos lo mismo, crean el mensaje usando el nombre de la funcion del servidor y sus parametros
    //preguntan si hay conexion con el servidor
    //si hay conexion mandan el mensaje y devuelven la respuesta
    //si no consigue conexion devuelve null

    private ArrayList<String> consBorrarCategoriaIngrediente (int idCategoriaIngrediente) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARCATING", String.valueOf(idCategoriaIngrediente)));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> consBorrarCategoriaReceta (int idCategoriaReceta) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARCATREC", String.valueOf(idCategoriaReceta)));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> consBorrarIngrediente (int idIngrediente) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARING", String.valueOf(idIngrediente)));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> consBorrarReceta (int idReceta) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARREC", String.valueOf(idReceta)));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> consBanearUsuario (String nickname) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("BANEARUSUARIO", nickname));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> consSubirCategoriaIngrediente (String nombreCategoria) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("SUBIRCATING", nombreCategoria));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> consSubirCategoriaReceta (String nombreReceta) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("SUBIRCATREC", nombreReceta));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> consSubirIngrediente (String nombreIngrediente, int idCategoria) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("SUBIRING", nombreIngrediente, String.valueOf(idCategoria)));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    private void respuestasDeServerComunes(String mensaje){
        switch(mensaje){
            case "MESSAGEERROR" -> {
                alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "El server no reconocio la petición");
                alerta.showAndWait();
            }
            default -> {
                alerta = new Alerta(Alert.AlertType.INFORMATION,
                        "Error en el server!",
                        "Se ha recibido una respuesta erronea por parte del server");
                alerta.showAndWait();
            }
        }
    }

    public void borrarCategoriaIngrediente(ActionEvent actionEvent) {
        ArrayList<String> respServer;
        try {
            CategoriaIngrediente itemSeleccionado =  (CategoriaIngrediente) cmbBorrarCatIngrediente.getValue();
            if(itemSeleccionado != null) {

                respServer = consBorrarCategoriaIngrediente(itemSeleccionado.getId());
                if (respServer != null) {
                    System.out.println(respServer.get(0));
                    switch (respServer.get(0)) {
                        case "BORRARCATINGFAIL" -> {
                            alerta = new Alerta(Alert.AlertType.ERROR,
                                    "Errpr inesperado",
                                    "Hubo un error al tratar de borrar la categoria");
                            alerta.showAndWait();
                        }
                        case "BORRARCATINGOK" -> {
                            alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                                    "Todo salio bien!",
                                    "Se borro correctamente la categoria");
                            alerta.showAndWait();
                        }
                        default -> respuestasDeServerComunes(respServer.get(0));
                    }
                    cmbBorrarCatReceta.getItems().clear();
                    cmbBorrarCatReceta.getItems().addAll(CategoriaReceta.getCategorias());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }
    }

    public void borrarCatReceta(ActionEvent actionEvent) {
        Alerta alerta;
        ArrayList<String> respServer;
        try {
            CategoriaReceta itemSeleccionado =  (CategoriaReceta) cmbBorrarCatReceta.getValue();
            if(itemSeleccionado != null) {

                respServer = consBorrarCategoriaReceta(itemSeleccionado.getId());
                if (respServer != null) {
                    System.out.println(respServer.get(0));
                    switch (respServer.get(0)) {
                        case "BORRARCATRECFAIL" -> {
                            alerta = new Alerta(Alert.AlertType.ERROR,
                                    "Errpr inesperado",
                                    "Hubo un error al tratar de borrar la categoria");
                            alerta.showAndWait();
                        }
                        case "BORRARCATRECOK" -> {
                            alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                                    "Todo salio bien!",
                                    "Se borro correctamente la categoria");
                            alerta.showAndWait();
                        }
                        default -> respuestasDeServerComunes(respServer.get(0));
                    }
                    cmbBorrarCatReceta.getItems().clear();
                    cmbBorrarCatReceta.getItems().addAll(CategoriaReceta.getCategorias());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }
    }

    public void borrarIngrediente(ActionEvent actionEvent) {
        ArrayList<String> respServer;
        Alerta alerta;
        try {
            Ingrediente itemSeleccionado =  (Ingrediente) cmbBorrarIngrediente.getValue();

            if(itemSeleccionado != null){
                respServer = consBorrarIngrediente(itemSeleccionado.getId());
                if(respServer != null){
                    switch (respServer.get(0)) {
                        case "BORRARINGFAIL" -> {
                            alerta = new Alerta(Alert.AlertType.ERROR,
                                    "Errpr inesperado",
                                    "Hubo un error al tratar de borrar el ingrediente");
                            alerta.showAndWait();
                        }
                        case "BORRARINGOK" -> {
                            alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                                    "Todo salio bien!",
                                    "Se borro correctamente el ingrediente");
                            alerta.showAndWait();
                        }
                        default -> respuestasDeServerComunes(respServer.get(0));
                    }
                    //Actualizar ingredientes
                    cmbBorrarIngrediente.getItems().clear();
                    cmbBorrarIngrediente.getItems().addAll(Ingrediente.getIngredientes());
                }else{
                    alerta = new Alerta(Alert.AlertType.ERROR,
                            "No se ha podido conectar con el servidor",
                            "El server no responde");
                    alerta.showAndWait();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }
    }

    public void borrarReceta(ActionEvent actionEvent){

    }

    public void subirCatRec(ActionEvent actionEvent) {
        ArrayList<String> respServer;
        Alerta alerta;

        try {
            respServer = consSubirCategoriaReceta(txtSubirCatRec.getText());
            if(respServer != null){
                switch (respServer.get(0)){
                    case "SUBIRCATRECFAIL" ->{
                        alerta = new Alerta(Alert.AlertType.ERROR,
                                "Error",
                                "No se ha podido subir la receta");
                        alerta.showAndWait();
                    }
                    case "SUBIRCATRECOK" ->{
                        alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                                "Todo salio bien!",
                                "Se ha subido la categoria de receta correctamente");
                        alerta.showAndWait();
                    }

                    default -> respuestasDeServerComunes(respServer.get(0));
                }
            }else{
                alerta = new Alerta(Alert.AlertType.ERROR,
                        "No se ha podido conectar con el servidor",
                        "El server no responde");
                alerta.showAndWait();
            }

        } catch (IOException e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }
    }

    public void subirIng(ActionEvent actionEvent) {
    }

    public void subirCatIng(ActionEvent actionEvent) {
    }

    public void banearUsuario(ActionEvent actionEvent) {
        ArrayList<String> respServer;
        Alerta alerta;

        try {
            respServer = consBanearUsuario(txtBanearUsuario.getText());
            if(respServer != null){
                switch (respServer.get(0)) {
                    case "BANEARUSUFAIL" -> {
                        alerta = new Alerta(Alert.AlertType.ERROR,
                                "Error",
                                "No se ha podido banear al usuario");
                        alerta.showAndWait();
                    }
                    case "BANEARUSUOK" -> {
                        alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                                "Todo salio bien!",
                                "Se ha baneado al usuario correctamente");
                        alerta.showAndWait();
                    }
                    default -> respuestasDeServerComunes(respServer.get(0));
                }
            }else{
                alerta = new Alerta(Alert.AlertType.ERROR,
                        "No se ha podido conectar con el servidor",
                        "El server no responde");
                alerta.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }


    }


    @FXML
    private void initialize() throws IOException {
        try{
            //llenar comboboxes
            cmbBorrarCatIngrediente.getItems().addAll(CategoriaIngrediente.getCategorias());
            cmbBorrarIngrediente.getItems().addAll(Ingrediente.getIngredientes());
            cmbBorrarCatReceta.getItems().addAll(CategoriaReceta.getCategorias());
            chkcmbCategoriasIngrediente.getItems().addAll(CategoriaIngrediente.getCategorias());
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }

    }
}
