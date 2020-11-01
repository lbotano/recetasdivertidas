package io.github.recetasDivertidas.pkgRecetasDivertidas;

public class Ingrediente {
    private int id;
    private String nombre;
    private CategoriaIngrediente categoria;

    public Ingrediente(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
        //this.categoria = categoria (añadirle el parametro)
    }

    @Override
    public String toString() {
        return nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public CategoriaIngrediente getCategoria() {
        return categoria;
    }
}
