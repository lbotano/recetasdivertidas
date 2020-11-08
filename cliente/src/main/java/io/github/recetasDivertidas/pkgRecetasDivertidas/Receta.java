package io.github.recetasDivertidas.pkgRecetasDivertidas;

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

    public int getCantCalificaciones() {
        return cantCalificaciones;
    }
}
