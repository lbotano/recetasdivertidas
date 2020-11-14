package io.github.recetasDivertidas.pkgRecetasDivertidas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.ArrayList;

public class Ingrediente {
    private int id;
    private String nombre;
    private final ArrayList<CategoriaIngrediente> categorias = new ArrayList<>();

    public Ingrediente() {}

    public Ingrediente(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public void anadirCategoria(CategoriaIngrediente categoria) {
        this.categorias.add(categoria);
    }

    public static ArrayList<Ingrediente> getIngredientes() throws IOException{
        ArrayList<Ingrediente> ingredientes = new ArrayList<>();
        ArrayList<String> mensajeEnviar = new ArrayList<>();

        mensajeEnviar.add("INGREDIENTES");
        ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);
        System.out.println(mensajeRecibir);

        switch (mensajeRecibir.get(0)) {
            case "INGREDIENTESOK" -> {
                Ingrediente ingrediente = null;
                for (int i = 1; i < mensajeRecibir.size(); i += 2) {
                    if (mensajeRecibir.get(i).equals("NEXTING")) {
                        if (ingrediente != null) {
                            ingredientes.add(ingrediente);
                        }
                        ingrediente = new Ingrediente(
                                Integer.parseInt(mensajeRecibir.get(i + 1)),
                                mensajeRecibir.get(i + 2));
                        i++;
                    } else {
                        // Añade una categoría al ingrediente
                        if (ingrediente != null) {
                            ingrediente.anadirCategoria(new CategoriaIngrediente(
                                    Integer.parseInt(mensajeRecibir.get(i)),
                                    mensajeRecibir.get(i + 1)));
                        }
                    }
                }
                ingredientes.add(ingrediente);
            }
            case "INGREDIENTESFAIL" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error", mensajeRecibir.get(1));
                alerta.showAndWait();
            }
            default -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "Ocurrió un error inesperado");
                alerta.showAndWait();
            }
        }

        return ingredientes;
    }
}
