package io.github.recetasDivertidas.pkgRecetasDivertidas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import javafx.scene.control.Alert;

import javax.imageio.IIOException;
import java.io.IOException;
import java.sql.SQLException;
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

    public Receta(int id, String autor, String titulo, String descripcion, float calificacion, int cantCalificaciones) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.calificacion = calificacion;
        this.cantCalificaciones = cantCalificaciones;
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

    public int getCalificacion(String nickname) {
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
}
