package io.github.recetasDivertidas.pkgRecetasDivertidas;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.Hashtable;

public class RecetasDivertidas{
    public static boolean logueadoComoAdmin;
    public static String username;
    public final static String HOVERED="E7E7E7";
    public final static String EXITED="FFFFFF";
    public BorderPane borderPane;
    public Button btnAdmin;

    private Hashtable<String, BorderPane> pestanas = new Hashtable<>();

    @FXML BorderPane bpanePrincipal;

    private BorderPane nodeInicio;
    private BorderPane nodePerfil;
    private BorderPane nodeBusquedaIng;
    private BorderPane nodeBusquedaCat;
    private BorderPane nodeBusquedaTexto;
    private BorderPane nodeSubirReceta;
    private BorderPane nodeAdmin;

    public RecetasDivertidas() {
        // Carga todas las pestañas
        try {
            nodeInicio = FXMLLoader.load(getClass().getResource("/fxml/inicio.fxml"));
            nodePerfil = FXMLLoader.load(getClass().getResource("/fxml/perfil.fxml"));
            nodeBusquedaIng = FXMLLoader.load(getClass().getResource("/fxml/busqueda_ingredientes.fxml"));
            nodeBusquedaCat = FXMLLoader.load(getClass().getResource("/fxml/busqueda_categorias.fxml"));
            nodeBusquedaTexto = FXMLLoader.load(getClass().getResource("/fxml/busqueda_texto.fxml"));
            nodeSubirReceta = FXMLLoader.load(getClass().getResource("/fxml/subir_receta.fxml"));
            nodeAdmin = FXMLLoader.load(getClass().getResource("/fxml/admin.fxml"));
            // Llena el diccionario de pestañas
            pestanas.put("btnInicio", nodeInicio);
            pestanas.put("btnPerfil", nodePerfil);
            pestanas.put("btnBusquedaIng", nodeBusquedaIng);
            pestanas.put("btnBusquedaCat", nodeBusquedaCat);
            pestanas.put("btnBusquedaTexto", nodeBusquedaTexto);
            pestanas.put("btnSubirReceta", nodeSubirReceta);
            pestanas.put("btnAdmin", nodeAdmin);
        } catch (IOException e) {
            new Alerta(Alert.AlertType.ERROR, "Error Inesperado", "Ocurrió un error inesperado.");
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        if(!logueadoComoAdmin){
            btnAdmin.setVisible(false); //TODO: No se eliminar el boton
        }
        bpanePrincipal.setCenter(nodeInicio);
    }

    @FXML
    private void cambiarPestana(ActionEvent event) {
        Button btn = (Button) event.getSource();
        System.out.println("Pressed " + btn.getId());
        bpanePrincipal.setCenter(pestanas.get(btn.getId()));
    }
}
