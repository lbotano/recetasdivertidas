package io.github.recetasDivertidas.pkgRecetasDivertidas;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Receta extends Stage{
    private String titulo;
    private String descripcion;
    private String calificacion;
    private ArrayList<String> ingredientes;
    private ArrayList<String> categoria;

    public Receta(){

        //Esto hace que no puedas apretar en otro lado que no sea la ventana
        this.initModality(Modality.APPLICATION_MODAL);
    }

    public String getCalificacion() {
        return calificacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public ArrayList<String> getCategoria() {
        return categoria;
    }

    public ArrayList<String> getIngredientes() {
        return ingredientes;
    }

}
