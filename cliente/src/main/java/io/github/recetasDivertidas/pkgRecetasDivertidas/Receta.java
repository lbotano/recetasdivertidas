package io.github.recetasDivertidas.pkgRecetasDivertidas;

import java.util.ArrayList;

public class Receta {
    private String titulo;
    private String descripcion;
    private String calificacion;
    private ArrayList<String> ingredientes;
    private ArrayList<String> categoria;

    public Receta(){

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
