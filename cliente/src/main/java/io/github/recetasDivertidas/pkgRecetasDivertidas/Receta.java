package io.github.recetasDivertidas.pkgRecetasDivertidas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import javafx.scene.control.Alert;

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
    private int calificacionDelUsuario = 0;
    private final ArrayList<Ingrediente> ingredientes = new ArrayList<>();
    private final ArrayList<CategoriaReceta> categoriasReceta = new ArrayList<>();
    private final ArrayList<CategoriaIngrediente> categoriasIngrediente = new ArrayList<>();
    private final ArrayList<Multimedia> multimedia = new ArrayList<>();

    // Receta completa
    public Receta(
            int id,
            String autor,
            String titulo,
            String descripcion,
            String instrucciones,
            float calificacion,
            int cantCalificaciones,
            int calificacionDelUsuario,
            List<Ingrediente> ingredientes,
            List<CategoriaReceta> categoriasReceta,
            List<CategoriaIngrediente> categoriasIngrediente,
            List<Multimedia> multimedia
    ) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.instrucciones = instrucciones;
        this.calificacion = calificacion;
        this.cantCalificaciones = cantCalificaciones;
        this.calificacionDelUsuario = calificacionDelUsuario;
        this.ingredientes.addAll(ingredientes);
        this.categoriasReceta.addAll(categoriasReceta);
        this.categoriasIngrediente.addAll(categoriasIngrediente);
        this.multimedia.addAll(multimedia);
    }

    // Receta para resultado de búsqueda
    public Receta(
            int id,
            String autor,
            String titulo,
            String descripcion,
            float calificacion,
            int cantCalificaciones,
            int calificacionDelUsuario
    ) {
        this.id = id;
        this.autor = autor;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.calificacion = calificacion;
        this.cantCalificaciones = cantCalificaciones;
        this.calificacionDelUsuario = calificacionDelUsuario;
    }

    // Receta para subir
    // TODO: Implementar multimedia
    public Receta(
                  String titulo,
                  String descripcion,
                  String instrucciones,
                  List<Ingrediente> ingredientes,
                  List<CategoriaReceta> categorias,
                  List<Multimedia> multimedia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.instrucciones = instrucciones;
        this.ingredientes.addAll(ingredientes);
        this.categoriasReceta.addAll(categorias);
        this.multimedia.addAll(multimedia);
    }

    public int getId() {
        return id;
    }

    public String getAutor() {
        return autor;
    }

    public String getTitulo() { return titulo; }

    public String getDescripcion() {
        return descripcion;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public ArrayList<CategoriaReceta> getCategoriasReceta() {
        return categoriasReceta;
    }

    public ArrayList<CategoriaIngrediente> getCategoriasIngrediente() {
        return categoriasIngrediente;
    }

    public ArrayList<Multimedia> getMultimedia() {
        return multimedia;
    }

    public int getCalificacionPropia() {
        return calificacionDelUsuario;
    }

    // Devuelve la calificación del usuario logueado
    public int actualizarCalificacionPropia() {
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
            } catch(Exception e) {
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
        if ((mensajeServidor.size() - 1) % 7 != 0 || !mensajeServidor.get(0).equals("RESPCONSULTA"))
            throw new MensajeServerInvalidoException();

        int i = 1;
        while (i < mensajeServidor.size()) {
            Receta receta = new Receta(
                    Integer.parseInt(mensajeServidor.get(i++)),
                    mensajeServidor.get(i++),
                    mensajeServidor.get(i++),
                    mensajeServidor.get(i++),
                    Float.parseFloat(mensajeServidor.get(i++)),
                    Integer.parseInt(mensajeServidor.get(i++)),
                    Integer.parseInt(mensajeServidor.get(i++))
            );
            resultado.add(receta);
        }

        return resultado;
    }

    public static ArrayList<Receta> getRecetasCategorias(int pagina, List<CategoriaReceta> categorias)
            throws IOException, ClassNotFoundException {
        ArrayList<Receta> resultado = new ArrayList<>();

        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("CONSRECETASCATREC");
        mensajeEnviar.add(String.valueOf(pagina));
        for (CategoriaReceta c : categorias) {
            mensajeEnviar.add(String.valueOf(c.getId()));
        }

        ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);

        switch (mensajeRecibir.get(0)) {
            case "RESPCONSULTA": {
                int i = 1;
                while (i < mensajeRecibir.size()) {
                    Receta receta = new Receta(
                            Integer.parseInt(mensajeRecibir.get(i++)),
                            mensajeRecibir.get(i++),
                            mensajeRecibir.get(i++),
                            mensajeRecibir.get(i++),
                            Float.parseFloat(mensajeRecibir.get(i++)),
                            Integer.parseInt(mensajeRecibir.get(i++)),
                            Integer.parseInt(mensajeRecibir.get(i++))
                    );
                    resultado.add(receta);
                }
            }
            break;
            case "RESPOCONSULTAFAIL": {
                Alerta alerta = new Alerta(Alert.AlertType.NONE, "Upsi", mensajeRecibir.get(1));
                alerta.showAndWait();
            }
            break;
            default: {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "Hubo un error inesperado");
                alerta.showAndWait();
            }
        }

        return resultado;
    }

    public static ArrayList<Receta> getRecetasIngredientes(int pagina, List<Ingrediente> ingredientes)
            throws IOException, ClassNotFoundException{
        ArrayList<Receta> resultado = new ArrayList<>();

        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("CONSRECETAING");
        mensajeEnviar.add(String.valueOf(pagina));
        for (Ingrediente i : ingredientes) {
            mensajeEnviar.add(String.valueOf(i.getId()));
        }

        ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);

        switch (mensajeRecibir.get(0)) {
            case "RESPCONSULTA": {
                int i = 1;
                while (i < mensajeRecibir.size()) {
                    Receta receta = new Receta(
                            Integer.parseInt(mensajeRecibir.get(i++)),
                            mensajeRecibir.get(i++),
                            mensajeRecibir.get(i++),
                            mensajeRecibir.get(i++),
                            Float.parseFloat(mensajeRecibir.get(i++)),
                            Integer.parseInt(mensajeRecibir.get(i++)),
                            Integer.parseInt(mensajeRecibir.get(i++))
                    );
                    resultado.add(receta);
                }
            }
            break;
            case "RESPOCONSULTAFAIL": {
                Alerta alerta = new Alerta(Alert.AlertType.NONE, "Upsi", mensajeRecibir.get(1));
                alerta.showAndWait();
            }
            break;
            default: {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "Hubo un error inesperado");
                alerta.showAndWait();
            }
        }

        return resultado;
    }

    public static ArrayList<Receta> getTopRecetas(int pagina)
            throws IOException, ClassNotFoundException, MensajeServerInvalidoException {
        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("CONSTOPRECETAS");
        mensajeEnviar.add(String.valueOf(pagina));

        ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);
        return Receta.getRecetasConsultaBusquedas(mensajeRecibir);
    }

    public static ArrayList<Receta> getRecetasUsuario(String usuario){
        ArrayList<Receta> resultado = new ArrayList<>();
        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("RECETASDEUSUARIO");
        mensajeEnviar.add(usuario);


        if (Conexion.isSvResponse()) {
            try {
                ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);
                switch (mensajeRecibir.get(0)) {
                    case "RESPCONSULTA": {
                        int i = 1;
                        while (i < mensajeRecibir.size()) {
                            Receta receta = new Receta(
                                    Integer.parseInt(mensajeRecibir.get(i++)),
                                    mensajeRecibir.get(i++),
                                    mensajeRecibir.get(i++),
                                    mensajeRecibir.get(i++),
                                    Float.parseFloat(mensajeRecibir.get(i++)),
                                    Integer.parseInt(mensajeRecibir.get(i++)),
                                    Integer.parseInt(mensajeRecibir.get(i++))
                            );
                            resultado.add(receta);
                        }
                    }
                    break;
                    case "RESPCONSULTAFAIL": {
                    }
                    break;
                    default: {
                        Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                                "Error inesperado",
                                "Hubo un error inesperado");
                        alerta.showAndWait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "Ocurrió un error inesperado.");
                alerta.showAndWait();
            }
        } else {
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error de conexión",
                    "Hubo un problema al conectarse al servidor");
            alerta.showAndWait();
        }

        return resultado;
    }

    // Devuelve los datos completos de una receta
    public static Receta getReceta(int idBusqueda) throws IOException, ClassNotFoundException {
        Receta resultado = null;

        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("DATOSRECETA");
        mensajeEnviar.add(String.valueOf(idBusqueda));

        ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);

        if (mensajeRecibir.get(0).equals("DATOSRECETAOK")) {
            int id = Integer.parseInt(mensajeRecibir.get(1));
            String autor = mensajeRecibir.get(2);
            String titulo = mensajeRecibir.get(3);
            String descripcion = mensajeRecibir.get(4);
            String instrucciones = mensajeRecibir.get(5);
            float promedioCalificacion = Float.parseFloat(mensajeRecibir.get(6));
            int cantCalificaciones = Integer.parseInt(mensajeRecibir.get(7));
            int calificacionDelUsuario = Integer.parseInt(mensajeRecibir.get(8));

            ArrayList<Ingrediente> ingredientes = new ArrayList<>();
            ArrayList<CategoriaReceta> categoriasReceta = new ArrayList<>();
            ArrayList<CategoriaIngrediente> categoriasIngrediente = new ArrayList<>();
            ArrayList<Multimedia> multimedia = new ArrayList<>();

            int i = 9;

            // Recibe los ingredinetes
            while (!mensajeRecibir.get(i).equals("CATEGORIASRECETA")) {
                int idIngrediente = Integer.parseInt(mensajeRecibir.get(i++));
                String nombreIngrediente = mensajeRecibir.get(i++);
                String unidadIngrediente = mensajeRecibir.get(i++);
                int cantIngrediente = Integer.parseInt(mensajeRecibir.get(i++));
                ingredientes.add(new Ingrediente(idIngrediente, nombreIngrediente, unidadIngrediente, cantIngrediente));

            }
            i++;

            // Recibe las categorías de receta
            while (!mensajeRecibir.get(i).equals("CATEGORIASING")) {
                int idCategoria = Integer.parseInt(mensajeRecibir.get(i++));
                String nombreCategoria = mensajeRecibir.get(i++);
                categoriasReceta.add(new CategoriaReceta(idCategoria, nombreCategoria));
            }
            i++;

            // Recibe las categorías de ingrediente
            while (!mensajeRecibir.get(i).equals("MULTIMEDIA")) {
                int idCategoria = Integer.parseInt(mensajeRecibir.get(i++));
                String nombreCategoria = mensajeRecibir.get(i++);
                categoriasIngrediente.add(new CategoriaIngrediente(idCategoria, nombreCategoria));
            }
            i++;

            // Recibe el multimedia
            while (i < mensajeRecibir.size()) {
                int idMultimedia = Integer.parseInt(mensajeRecibir.get(i++));
                String nombreMultimedia = mensajeRecibir.get(i++);
                multimedia.add(new Multimedia(idMultimedia, nombreMultimedia));
            }

            resultado = new Receta(
                    id,
                    autor,
                    titulo,
                    descripcion,
                    instrucciones,
                    promedioCalificacion,
                    cantCalificaciones,
                    calificacionDelUsuario,
                    ingredientes,
                    categoriasReceta,
                    categoriasIngrediente,
                    multimedia
            );

        } else {
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    mensajeRecibir.get(0).equals("DATOSRECETAFAIL") ? mensajeRecibir.get(1)
                            : "Ocurrió un error inesperado al abrir la receta.");
            alerta.showAndWait();
        }

        return resultado;
    }

    public void calificar(int calificacion) {
        ArrayList<String> mensajeEnviar = new ArrayList<>();
        mensajeEnviar.add("CALIFICAR");
        mensajeEnviar.add(RecetasDivertidas.username);
        mensajeEnviar.add(String.valueOf(this.id));
        mensajeEnviar.add(String.valueOf(calificacion));

        try {
            Conexion.sendMessage(mensajeEnviar);
        } catch (Exception e) {
            Alerta alerta =
                    new Alerta(Alert.AlertType.ERROR, "Error inesperado", "Hubo un error inesperado");
            alerta.showAndWait();
            e.printStackTrace();
        }
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
                    this.calificacionDelUsuario = Integer.parseInt(respuesta.get(8));
                }
            }
        } catch (Exception e) {
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
            mensajeEnviar.add(this.titulo);
            mensajeEnviar.add(this.descripcion);
            mensajeEnviar.add(this.instrucciones);

            for (Ingrediente i : this.ingredientes) {
                mensajeEnviar.add(String.valueOf(i.getId()));
                mensajeEnviar.add(String.valueOf(i.getCantidad()));
                mensajeEnviar.add(i.getUnidad() == null || i.getUnidad().equals("") ? "SinUnidad" : i.getUnidad());
            }

            mensajeEnviar.add("CATEGORIASRECETA");
            for (CategoriaReceta c : this.categoriasReceta) {
                mensajeEnviar.add(String.valueOf(c.getId()));
            }

            mensajeEnviar.add("INICIOMULTIMEDIA");
            for (Multimedia m : this.multimedia) {
                mensajeEnviar.add(String.valueOf(m.getUrl()));
            }

            ArrayList<String> mensajeRecibir = Conexion.sendMessage(mensajeEnviar);
            Alerta alerta;
            switch (mensajeRecibir.get(0)) {
                case "SUBIRRECETAOK": {
                    alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                            "Receta subida",
                            "Su receta se ha subido exitosamente.");
                    alerta.showAndWait();
                }
                break;
                case "SUBIRRECETAFAIL": {
                    alerta = new Alerta(Alert.AlertType.ERROR,
                            "Error al subir receta",
                            "Error: " + mensajeRecibir.get(1));
                    alerta.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }
    }
}
