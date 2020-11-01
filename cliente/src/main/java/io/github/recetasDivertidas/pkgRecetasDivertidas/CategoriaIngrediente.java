package io.github.recetasDivertidas.pkgRecetasDivertidas;

public class CategoriaIngrediente {
    private final int id;
    private final String nombre;

    public CategoriaIngrediente(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

}
