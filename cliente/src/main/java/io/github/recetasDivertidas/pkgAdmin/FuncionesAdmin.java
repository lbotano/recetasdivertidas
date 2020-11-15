package io.github.recetasDivertidas.pkgAdmin;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaIngrediente;
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
        mensaje = new ArrayList<>(Arrays.asList("BORRARREC", nombreReceta));
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

    public void borrarCategoriaIngrediente(ActionEvent actionEvent) {
        try {
            ArrayList<String> respServer = consBorrarCategoriaIngrediente((int) cmbBorrarCatIngrediente.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() throws IOException {
        try{
            cmbBorrarCatIngrediente.getItems().addAll(CategoriaIngrediente.getCategorias());
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }

    }
}
