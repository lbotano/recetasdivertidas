package io.github.recetasDivertidas.pkgRecetasDivertidas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.ArrayList;

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

    // Devuelve todas las categorías de ingrediente
    public static ArrayList<CategoriaIngrediente> getCategorias() throws IOException {
        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("LISTARCATING");

        ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);

        ArrayList<CategoriaIngrediente> categorias = new ArrayList<>();
        switch (mensajeRecibir.get(0)) {
            case "LISTCATING" -> {
                for (int i = 1; i < mensajeRecibir.size(); i += 2) {
                    categorias.add(new CategoriaIngrediente(
                            Integer.parseInt(mensajeRecibir.get(i)),
                            mensajeRecibir.get(i + 1))
                    );
                }
            }
            case "LISTARCATINGFAIL" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        mensajeRecibir.get(1));
                alerta.showAndWait();
            }
            default -> {
                throw new IOException("El mensaje recibido del servidor es erroneo");
            }
        }

        return categorias;
    }

}
