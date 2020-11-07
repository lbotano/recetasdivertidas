package io.github.recetasDivertidas.pkgRecetasDivertidas;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Hashtable;

public class RecetasDivertidas{
    public static boolean logueadoComoAdmin;
    public static String username;
    public BorderPane borderPane;

    private Hashtable<String, BorderPane> pestanas = new Hashtable<>();

    @FXML BorderPane bpanePrincipal;
    @FXML Button btnInicio;
    @FXML Button btnPerfil;
    @FXML Button btnBusquedaIng;
    @FXML Button btnBusquedaCat;
    @FXML Button btnBusquedaTexto;
    @FXML Button btnSubirReceta;
    @FXML Button btnAdmin;

    private BorderPane nodeInicio;
    private BorderPane nodePerfil;
    private BorderPane nodeBusquedaIng;
    private BorderPane nodeBusquedaCat;
    private BorderPane nodeBusquedaTexto;
    private BorderPane nodeSubirReceta;

    public RecetasDivertidas() {
        // Carga todas las pestañas
        try {
            nodeInicio = FXMLLoader.load(getClass().getResource("/fxml/inicio.fxml"));
            nodePerfil = FXMLLoader.load(getClass().getResource("/fxml/perfil.fxml"));
            nodeBusquedaIng = FXMLLoader.load(getClass().getResource("/fxml/busqueda_ingredientes.fxml"));
            nodeBusquedaCat = FXMLLoader.load(getClass().getResource("/fxml/busqueda_categorias.fxml"));
            nodeBusquedaTexto = FXMLLoader.load(getClass().getResource("/fxml/busqueda_texto.fxml"));
            nodeSubirReceta = FXMLLoader.load(getClass().getResource("/fxml/subir_receta.fxml"));

            // Llena el diccionario de pestañas
            pestanas.put("btnInicio", nodeInicio);
            pestanas.put("btnPerfil", nodePerfil);
            pestanas.put("btnBusquedaIng", nodeBusquedaIng);
            pestanas.put("btnBusquedaCat", nodeBusquedaCat);
            pestanas.put("btnBusquedaTexto", nodeBusquedaTexto);
            pestanas.put("btnSubirReceta", nodeSubirReceta);
        } catch (IOException e) {
            new Alerta(Alert.AlertType.ERROR, "Error Inesperado", "Ocurrió un error inesperado.");
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        bpanePrincipal.setCenter(nodeInicio);
    }

    @FXML
    private void cambiarPestana(ActionEvent event) {
        Button btn = (Button) event.getSource();
        System.out.println(btn.getId());
        bpanePrincipal.setCenter(pestanas.get(btn.getId()));
    }
}
