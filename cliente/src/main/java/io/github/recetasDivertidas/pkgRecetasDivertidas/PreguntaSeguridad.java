package io.github.recetasDivertidas.pkgRecetasDivertidas;

public class PreguntaSeguridad {
    private final int id;
    private final String pregunta;

    public PreguntaSeguridad(int id, String pregunta){
        this.id = id;
        this.pregunta = pregunta;
    }

    public int getId() {
        return id;
    }

    public String getPregunta() {
        return pregunta;
    }

    @Override
    public String toString() {
        return pregunta;
    }
}
