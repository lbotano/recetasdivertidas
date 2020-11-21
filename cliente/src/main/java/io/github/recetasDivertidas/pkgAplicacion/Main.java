package io.github.recetasDivertidas.pkgAplicacion;

import io.github.recetasDivertidas.pkgConexion.Conexion;
import javafx.application.Application;

public class Main{

    public static void main(String[] args){
        Conexion.iniciarConexion();
        Application.launch(Aplicacion.class, args);
    }

}
