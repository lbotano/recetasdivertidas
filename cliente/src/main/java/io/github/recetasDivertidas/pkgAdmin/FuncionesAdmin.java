package io.github.recetasDivertidas.pkgAdmin;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaIngrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaReceta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import javafx.collections.ObservableList;
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
    @FXML ComboBox<CategoriaIngrediente> cmbBorrarCatIngrediente;
    @FXML Button btnBorrarCatIngrediente;

    @FXML ComboBox<Ingrediente> cmbBorrarIngrediente;
    @FXML Button btnBorrarIngrediente;

    @FXML ComboBox<CategoriaReceta> cmbBorrarCatReceta;
    @FXML Button btnBorrarCatReceta;

    @FXML TextField txtBanearUsuario;
    @FXML Button btnBanearUsuario;

    @FXML TextField txtSubirCatIng;
    @FXML Button btnSubirCatIng;

    @FXML CheckComboBox<CategoriaIngrediente> chkcmbCategoriasIngrediente;
    @FXML TextField txtSubirIng;
    @FXML Button btnSubirIng;

    @FXML TextField txtSubirCatRec;
    @FXML Button btnSubirCatRec;

    //las siguientes funciones piden los datos necesarios para enviarle al servidor la peticion y devuelven su respuesta
    //todas las funciones hacen mas o menos lo mismo, crean el mensaje usando el nombre de la funcion del servidor y sus parametros
    //preguntan si hay conexion con el servidor
    //si hay conexion mandan el mensaje y devuelven la respuesta
    //si no consigue conexion devuelve null

    private ArrayList<String> consBorrarCategoriaIngrediente (int idCategoriaIngrediente) throws IOException, ClassNotFoundException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARCATING", String.valueOf(idCategoriaIngrediente)));
        return Conexion.sendMessage(mensaje);
    }

    public ArrayList<String> consBorrarCategoriaReceta (int idCategoriaReceta) throws IOException, ClassNotFoundException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARCATREC", String.valueOf(idCategoriaReceta)));
        return Conexion.sendMessage(mensaje);
    }

    public ArrayList<String> consBorrarIngrediente (int idIngrediente) throws IOException, ClassNotFoundException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARING", String.valueOf(idIngrediente)));
        return Conexion.sendMessage(mensaje);
    }

    public ArrayList<String> consBorrarReceta (int idReceta) throws IOException, ClassNotFoundException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARREC", String.valueOf(idReceta)));
        return Conexion.sendMessage(mensaje);
    }

    public ArrayList<String> consBanearUsuario (String nickname) throws IOException, ClassNotFoundException {
        mensaje = new ArrayList<>(Arrays.asList("BANEARUSUARIO", nickname));
        return Conexion.sendMessage(mensaje);
    }

    public ArrayList<String> consSubirCategoriaIngrediente (String nombreCategoria)
            throws IOException, ClassNotFoundException {
        mensaje = new ArrayList<>(Arrays.asList("SUBIRCATING", nombreCategoria));
        return Conexion.sendMessage(mensaje);
    }

    public ArrayList<String> consSubirCategoriaReceta (String nombreReceta) throws IOException, ClassNotFoundException {
        mensaje = new ArrayList<>(Arrays.asList("SUBIRCATREC", nombreReceta));
        return Conexion.sendMessage(mensaje);
    }

    public ArrayList<String> consSubirIngrediente (String nombreIngrediente,
                                                   ObservableList<CategoriaIngrediente> categorias)
            throws IOException, ClassNotFoundException {
        mensaje = new ArrayList<>();
        mensaje.add("SUBIRING");
        mensaje.add(nombreIngrediente);
        for (CategoriaIngrediente i: categorias) {
            mensaje.add(String.valueOf(i.getId()));
        }

        return Conexion.sendMessage(mensaje);
    }

    private void respuestasDeServerComunes(String mensaje) {
        if ("MESSAGEERROR".equals(mensaje))
            System.err.println("[ERROR] El servidor no ha reconocido la petición.");
        else
            System.err.println("[ERROR] El servidor ha devuelto un resultado inválido.");

        alerta = new Alerta(Alert.AlertType.ERROR,
                "Error inesperado",
                "Hubo un error inesperado");
        alerta.showAndWait();
    }

    public void borrarCategoriaIngrediente() {
        ArrayList<String> respServer;
        try {
            CategoriaIngrediente itemSeleccionado =  cmbBorrarCatIngrediente.getValue();
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
                            //Actualizar categorias
                            cmbBorrarCatReceta.getItems().clear();
                            cmbBorrarCatReceta.getItems().addAll(CategoriaReceta.getCategorias());
                        }
                        default -> respuestasDeServerComunes(respServer.get(0));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }
    }

    public void borrarCatReceta() {
        Alerta alerta;
        ArrayList<String> respServer;
        try {
            CategoriaReceta itemSeleccionado =  cmbBorrarCatReceta.getValue();
            if (itemSeleccionado != null) {
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
                            //Actualizar categorias
                            cmbBorrarCatReceta.getItems().clear();
                            cmbBorrarCatReceta.getItems().addAll(CategoriaReceta.getCategorias());
                        }
                        default -> respuestasDeServerComunes(respServer.get(0));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }
    }

    @FXML
    public void borrarIngrediente() {
        ArrayList<String> respServer;
        Alerta alerta;
        try {
            Ingrediente itemSeleccionado = cmbBorrarIngrediente.getValue();

            if (itemSeleccionado != null){
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
                            //Actualizar ingredientes
                            cmbBorrarIngrediente.getItems().clear();
                            cmbBorrarIngrediente.getItems().addAll(Ingrediente.getIngredientes());
                        }
                        default -> respuestasDeServerComunes(respServer.get(0));
                    }
                } else {
                    alerta = new Alerta(Alert.AlertType.ERROR,
                            "No se ha podido conectar con el servidor",
                            "El server no responde");
                    alerta.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }
    }

    public void subirCatRec() {
        ArrayList<String> respServer;

        try {
            respServer = consSubirCategoriaReceta(txtSubirCatRec.getText());
            if (respServer != null) {
                switch (respServer.get(0)){
                    case "SUBIRCATRECFAIL" ->{
                        alerta = new Alerta(Alert.AlertType.ERROR,
                                "Error",
                                "No se ha podido subir la categoria de receta");
                        alerta.showAndWait();
                    }
                    case "SUBIRCATRECOK" ->{
                        alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                                "Todo salio bien!",
                                "Se ha subido la categoria de receta correctamente");
                        alerta.showAndWait();
                        cmbBorrarCatReceta.getItems().clear();
                        cmbBorrarCatReceta.getItems().addAll(CategoriaReceta.getCategorias());
                    }

                    default -> respuestasDeServerComunes(respServer.get(0));
                }
            } else {
                alerta = new Alerta(Alert.AlertType.ERROR,
                        "No se ha podido conectar con el servidor",
                        "El server no responde");
                alerta.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }
    }

    public void subirIng() {
        ArrayList<String> respServer;

        try {
            ObservableList<CategoriaIngrediente> catIngSeleccionados = chkcmbCategoriasIngrediente.getCheckModel().getCheckedItems();
            respServer = consSubirIngrediente(txtSubirIng.getText(), catIngSeleccionados);

            if (respServer != null) {
                switch (respServer.get(0)){
                    case "SUBIRINGFAIL" ->{
                        alerta = new Alerta(Alert.AlertType.ERROR,
                                "Error",
                                "No se ha podido subir el ingrediente");
                        alerta.showAndWait();
                    }
                    case "SUBIRINGOK" ->{
                        alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                                "Todo salio bien!",
                                "Se ha subido el ingrediente correctamente");
                        alerta.showAndWait();
                        cmbBorrarIngrediente.getItems().clear();
                        cmbBorrarIngrediente.getItems().addAll(Ingrediente.getIngredientes());
                    }

                    default -> respuestasDeServerComunes(respServer.get(0));
                }
            } else {
                alerta = new Alerta(Alert.AlertType.ERROR,
                        "No se ha podido conectar con el servidor",
                        "El server no responde");
                alerta.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }
    }

    public void subirCatIng() {
        ArrayList<String> respServer;

        try {
            respServer = consSubirCategoriaIngrediente(txtSubirCatIng.getText());
            if (respServer != null) {
                switch (respServer.get(0)){
                    case "SUBIRCATINGFAIL" ->{
                        alerta = new Alerta(Alert.AlertType.ERROR,
                                "Error",
                                "No se ha podido subir la categoria de ingrediente");
                        alerta.showAndWait();
                    }
                    case "SUBIRCATINGOK" ->{
                        alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                                "Todo salio bien!",
                                "Se ha subido la categoria de ingrediente correctamente");
                        alerta.showAndWait();
                        cmbBorrarCatIngrediente.getItems().clear();
                        chkcmbCategoriasIngrediente.getItems().clear();
                        cmbBorrarCatIngrediente.getItems().addAll(CategoriaIngrediente.getCategoriasIngrediente());
                        chkcmbCategoriasIngrediente.getItems().addAll(CategoriaIngrediente.getCategoriasIngrediente());
                    }

                    default -> respuestasDeServerComunes(respServer.get(0));
                }
            } else {
                alerta = new Alerta(Alert.AlertType.ERROR,
                        "No se ha podido conectar con el servidor",
                        "El server no responde");
                alerta.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }
    }

    public void banearUsuario() {
        ArrayList<String> respServer;
        Alerta alerta;

        try {
            respServer = consBanearUsuario(txtBanearUsuario.getText());
            if (respServer != null) {
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
            } else {
                alerta = new Alerta(Alert.AlertType.ERROR,
                        "No se ha podido conectar con el servidor",
                        "El server no responde");
                alerta.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error al enviar el mensaje");
            alerta.showAndWait();
        }


    }


    @FXML
    private void initialize() {
        try{
            //llenar comboboxes
            cmbBorrarCatIngrediente.getItems().addAll(CategoriaIngrediente.getCategoriasIngrediente());
            cmbBorrarIngrediente.getItems().addAll(Ingrediente.getIngredientes());
            cmbBorrarCatReceta.getItems().addAll(CategoriaReceta.getCategorias());
            chkcmbCategoriasIngrediente.getItems().addAll(CategoriaIngrediente.getCategoriasIngrediente());
        } catch (Exception e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }

    }
}
