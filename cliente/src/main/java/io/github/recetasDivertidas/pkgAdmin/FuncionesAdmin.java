package io.github.recetasDivertidas.pkgAdmin;

import io.github.recetasDivertidas.pkgConexion.Conexion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FuncionesAdmin {

    private ArrayList mensaje; //objeto donde voy a guardar los mensajes para el server

    //las siguientes funciones piden los datos necesarios para enviarle al servidor la peticion y devuelven su respuesta
    //todas las funciones hacen mas o menos lo mismo, crean el mensaje usando el nombre de la funcion del servidor y sus parametros
    //preguntan si hay conexion con el servidor
    //si hay conexion mandan el mensaje y devuelven la respuesta
    //si no consigue conexion devuelve null

    public ArrayList<String> borrarCategoriaIngrediente (int idCategoriaIngrediente) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARCATING", String.valueOf(idCategoriaIngrediente)));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> borrarCategoriaReceta (int idCategoriaReceta) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARCATREC", String.valueOf(idCategoriaReceta)));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> borrarIngrediente (int idIngrediente) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARING", String.valueOf(idIngrediente)));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> borrarReceta (int idReceta) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARREC", String.valueOf(idReceta)));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> banearUsuario (String nickname) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("BANEARUSUARIO", nickname));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> subirCategoriaIngrediente (String nombreCategoria) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("SUBIRCATING", nombreCategoria));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> subirCategoriaReceta (String nombreReceta) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("BORRARREC", nombreReceta));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }

    public ArrayList<String> subirIngrediente (String nombreIngrediente, int idCategoria) throws IOException {
        mensaje = new ArrayList<>(Arrays.asList("SUBIRING", nombreIngrediente, String.valueOf(idCategoria)));
        if (Conexion.isSvResponse()) {
            return Conexion.sendMessage(mensaje);
        }
        return null;
    }
}
