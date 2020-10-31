package io.github.recetasDivertidas.pkgRecetasDivertidas;

public class Categoria {
    private int id;
    private String nombre;

    public Categoria(int id, String nombre){
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
