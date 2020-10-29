package io.github.recetasDivertidas.pkgLogin;

public class PreguntaSeguridad {
    private int id;
    private String pregunta;

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
