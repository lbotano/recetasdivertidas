package io.github.recetasDivertidas.pkgRecetasDivertidas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.ArrayList;

public class CategoriaReceta {
    private final int id;
    private final String nombre;

    public CategoriaReceta(int id, String nombre){
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

    // Devuelve todas las categorías de receta
    public static ArrayList<CategoriaReceta> getCategorias() throws IOException, ClassNotFoundException {
        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("LISTARCATREC");

        ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);

        ArrayList<CategoriaReceta> categorias = new ArrayList<>();
        switch (mensajeRecibir.get(0)) {
            case "LISTCATREC" -> {
                for (int i = 1; i < mensajeRecibir.size(); i += 2) {
                    categorias.add(new CategoriaReceta(
                            Integer.parseInt(mensajeRecibir.get(i)),
                            mensajeRecibir.get(i + 1))
                    );
                }
            }
            case "LISTARCATRECFAIL" -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        mensajeRecibir.get(1));
                alerta.showAndWait();
            }
            default -> {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "Hubo un error inesperado.");
            }
        }

        return categorias;
    }
}
