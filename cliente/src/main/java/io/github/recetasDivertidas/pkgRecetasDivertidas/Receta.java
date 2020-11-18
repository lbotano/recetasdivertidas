package io.github.recetasDivertidas.pkgRecetasDivertidas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.ResultadoBusqueda;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Receta {
    private int id;
    private String autor;
    private String titulo;
    private String descripcion;
    private String instrucciones;
    private float calificacion;
    private int cantCalificaciones;
    private final ArrayList<Ingrediente> ingredientes = new ArrayList<>();
    private final ArrayList<CategoriaReceta> categorias = new ArrayList<>();

    // Receta para resultado de búsqueda
    public Receta(int id, String autor, String titulo, String descripcion, float calificacion, int cantCalificaciones) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.calificacion = calificacion;
        this.cantCalificaciones = cantCalificaciones;
    }

    // Receta para subir
    // TODO: Implementar multimedia
    public Receta(String autor,
                  String titulo,
                  String descripcion,
                  String instrucciones,
                  List<Ingrediente> ingredientes,
                  List<CategoriaReceta> categorias) {
        this.autor = autor;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.instrucciones = instrucciones;
        this.ingredientes.addAll(ingredientes);
        this.categorias.addAll(categorias);
    }

    public int getId() {
        return id;
    }

    public String getAutor() {
        return autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public float getCalificacion() {
        return calificacion;
    }

    // Devuelve la calificación del usuario logueado
    public int getCalificacionPropia() {
        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("CONSCALIFUSUARIO");
        mensajeEnviar.add(RecetasDivertidas.username);
        mensajeEnviar.add(String.valueOf(this.id));

        if (Conexion.isSvResponse()) {
            try {
                ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);
                if (mensajeRecibir.get(0).equals("CALIFICACIONUSUARIO")) {
                    return Integer.parseInt(mensajeRecibir.get(1));
                }
            } catch(IOException e) {
                e.printStackTrace();
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "Ocurrión un error inesperado.");
                alerta.showAndWait();
            }
        } else {
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error de conexión",
                    "Hubo un problema al conectarse al servidor");
            alerta.showAndWait();
        }

        return 0;
    }

    public int getCantCalificaciones() {
        return cantCalificaciones;
    }

    public static ArrayList<Receta> getRecetasConsultaBusquedas(List<String> mensajeServidor)
    throws MensajeServerInvalidoException {
        ArrayList<Receta> resultado = new ArrayList<>();

        // Verificar que la consulta sea correcta
        if ((mensajeServidor.size() - 1) % 6 != 0 || !mensajeServidor.get(0).equals("RESPCONSULTA"))
            throw new MensajeServerInvalidoException();

        for (int i = 1; i < mensajeServidor.size(); i += 6) {
            Receta receta = new Receta(
                Integer.parseInt(mensajeServidor.get(i)),
                mensajeServidor.get(i + 1),
                mensajeServidor.get(i + 2),
                mensajeServidor.get(i + 3),
                Float.parseFloat(mensajeServidor.get(i + 4)),
                Integer.parseInt(mensajeServidor.get(i + 5))
            );
            resultado.add(receta);
        }
        return resultado;
    }

    public static ArrayList<Receta> getTopRecetas(int pagina) throws IOException, MensajeServerInvalidoException {
        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("CONSTOPRECETAS");
        mensajeEnviar.add(String.valueOf(pagina));

        ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);
        return Receta.getRecetasConsultaBusquedas(mensajeRecibir);
    }

    public static ArrayList<Receta> getRecetasUsuario(String usuario) throws MensajeServerInvalidoException, IOException {
        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("RECETASDEUSUARIO");
        mensajeEnviar.add(usuario);


        if (Conexion.isSvResponse()) {
            try {
                ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);
                //Hay que cambiar este if por un switch, pero tengo mucho sueño bye
                if (mensajeRecibir.get(0).equals("RECETASDEUSUARIOOK")) {
                    ArrayList<Receta> resultado = new ArrayList<>();
                    for (int i = 1; i < mensajeRecibir.size(); i += 6) {
                        Receta receta = new Receta(
                                Integer.parseInt(mensajeRecibir.get(i)),
                                mensajeRecibir.get(i + 1),
                                mensajeRecibir.get(i + 2),
                                mensajeRecibir.get(i + 3),
                                Float.parseFloat(mensajeRecibir.get(i + 4)),
                                Integer.parseInt(mensajeRecibir.get(i + 5))
                        );
                        resultado.add(receta);
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "Ocurrión un error inesperado.");
                alerta.showAndWait();
            }
        } else {
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error de conexión",
                    "Hubo un problema al conectarse al servidor");
            alerta.showAndWait();
        }

        return null;
    }


    public boolean calificar(int calificacion) {
        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("CALIFICAR");
        mensajeEnviar.add(RecetasDivertidas.username);
        mensajeEnviar.add(String.valueOf(this.id));
        mensajeEnviar.add(String.valueOf(calificacion));

        try {
            Conexion.sendMessage(mensajeEnviar);
            if (Conexion.isSvResponse()) {
                ArrayList<String> respuesta = Conexion.sendMessage(mensajeEnviar);
                if (respuesta.get(0).equals("CALIFICAROK")) return true;
            }
        } catch (IOException e) {
            Alerta alerta =
                    new Alerta(Alert.AlertType.ERROR, "Error inesperado", "Hubo un error inesperado");
            alerta.showAndWait();
            e.printStackTrace();
        }
        return false;
    }

    // Actualiza el nombre, el autor, la descripción y las calificaciones
    public void actualizarSimple() {
        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("DATOSRECETA");
        mensajeEnviar.add(String.valueOf(this.id));

        try {
            Conexion.sendMessage(mensajeEnviar);
            if (Conexion.isSvResponse()) {
                ArrayList<String> respuesta = Conexion.sendMessage(mensajeEnviar);
                if (respuesta.get(0).equals("DATOSRECETAOK")) {
                    this.autor = respuesta.get(2);
                    this.titulo = respuesta.get(3);
                    this.descripcion = respuesta.get(4);
                    this.calificacion = Float.parseFloat(respuesta.get(6));
                    this.cantCalificaciones = Integer.parseInt(respuesta.get(7));
                }
            }
        } catch (IOException e) {
            Alerta alerta =
                    new Alerta(Alert.AlertType.ERROR, "Error inesperado", "Hubo un error inesperado");
            alerta.showAndWait();
            e.printStackTrace();
        }
    }

    public void subir() {
        try {
            ArrayList<String> mensajeEnviar = new ArrayList<>();
            mensajeEnviar.add("SUBIRRECETA");
            mensajeEnviar.add(this.autor);
            mensajeEnviar.add(this.titulo);
            mensajeEnviar.add(this.descripcion);
            mensajeEnviar.add(this.instrucciones);

            for (Ingrediente i : this.ingredientes) {
                mensajeEnviar.add(String.valueOf(i.getId()));
                mensajeEnviar.add(String.valueOf(i.getCantidad()));
                mensajeEnviar.add(i.getUnidad() == null || i.getUnidad().equals("") ? "SinUnidad" : i.getUnidad());
            }

            mensajeEnviar.add("CATEGORIASRECETA");
            for (CategoriaReceta c : this.categorias) {
                mensajeEnviar.add(String.valueOf(c.getId()));
            }

            mensajeEnviar.add("INICIOMULTIMEDIA");
            // TODO: Agregar multimedia

            ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);
            Alerta alerta;
            switch (mensajeRecibir.get(0)) {
                case "SUBIRRECETAOK" ->
                    alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                            "Receta subida",
                            "Su receta se ha subido exitosamente.");
                case "SUBIRRECETAFAIL" ->
                    alerta = new Alerta(Alert.AlertType.ERROR,
                            "Error al subir receta",
                            "Error: " + mensajeRecibir.get(1));
                default ->
                    alerta = new Alerta(Alert.AlertType.ERROR,
                            "Error inesperado",
                            "Hubo un error inesperado");
            }
            alerta.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }
    }
}
