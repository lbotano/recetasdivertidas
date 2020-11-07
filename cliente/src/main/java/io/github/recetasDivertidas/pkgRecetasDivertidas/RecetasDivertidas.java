package io.github.recetasDivertidas.pkgRecetasDivertidas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class RecetasDivertidas{
    public static boolean admin;
    public final static String BACKGROUND = "#FFFFFF";
    public final static String HOVERED = "#F1F1F1";
    public static String username;
    public BorderPane borderPane;

    private enum pestanias
    {
        INICIO,
        PERFIL,
        BUSQUEDAINGREDIENTES,
        BUSQUEDACATEGORIA,
        BUSQUEDATEXTO,
        SUBIRRECETA,
        MISRECETAS,
        ADMIN
    }
    private pestanias current;
    @FXML Button btnInicio;
    @FXML Button btnPerfil;
    @FXML Button btnBuscarIng;
    @FXML Button btnBuscarCat;
    @FXML Button btnBuscarTexto;
    @FXML Button btnSubirReceta;
    @FXML Button btnAdmin;
    private Pane center;

    @FXML
    private void initialize(){

        current = pestanias.INICIO;

        btnInicio.setOnAction(e -> {
            try {
                cambiarPestania(pestanias.INICIO);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        btnInicio.setOnMouseEntered(e -> btnInicio.setStyle("-fx-background-color:" + HOVERED +";"));
        btnInicio.setOnMouseExited(e -> btnInicio.setStyle("-fx-background-color:" + BACKGROUND +";"));

        btnPerfil.setOnAction(e -> {
            try {
                cambiarPestania(pestanias.PERFIL);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        btnPerfil.setOnMouseEntered(e -> btnPerfil.setStyle("-fx-background-color:" + HOVERED +";"));
        btnPerfil.setOnMouseExited(e -> btnPerfil.setStyle("-fx-background-color:" + BACKGROUND +";"));

        btnBuscarIng.setOnAction(e -> {
            try {
                cambiarPestania(pestanias.BUSQUEDAINGREDIENTES);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        btnBuscarIng.setOnMouseEntered(e -> btnBuscarIng.setStyle("-fx-background-color:" + HOVERED +";"));
        btnBuscarIng.setOnMouseExited(e -> btnBuscarIng.setStyle("-fx-background-color:" + BACKGROUND +";"));

        btnBuscarTexto.setOnAction(e -> {
            try {
                cambiarPestania(pestanias.BUSQUEDATEXTO);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        btnBuscarTexto.setOnMouseEntered(e -> btnBuscarTexto.setStyle("-fx-background-color:" + HOVERED +";"));
        btnBuscarTexto.setOnMouseExited(e -> btnBuscarTexto.setStyle("-fx-background-color:" + BACKGROUND +";"));

        btnBuscarCat.setOnAction(e -> {
            try {
                cambiarPestania(pestanias.BUSQUEDACATEGORIA);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        btnBuscarCat.setOnMouseEntered(e -> btnBuscarCat.setStyle("-fx-background-color:" + HOVERED +";"));
        btnBuscarCat.setOnMouseExited(e -> btnBuscarCat.setStyle("-fx-background-color:" + BACKGROUND +";"));

        btnSubirReceta.setOnAction(e -> {
            try {
                cambiarPestania(pestanias.SUBIRRECETA);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        btnSubirReceta.setOnMouseEntered(e -> btnSubirReceta.setStyle("-fx-background-color:" + HOVERED +";"));
        btnSubirReceta.setOnMouseExited(e -> btnSubirReceta.setStyle("-fx-background-color:" + BACKGROUND +";"));

        btnAdmin.setOnAction(e -> {
            try {
                cambiarPestania(pestanias.ADMIN);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        btnAdmin.setOnMouseEntered(e -> btnAdmin.setStyle("-fx-background-color:" + HOVERED +";"));
        btnAdmin.setOnMouseExited(e -> btnAdmin.setStyle("-fx-background-color:" + BACKGROUND +";"));

    }

    @FXML
    private void cambiarPestania(pestanias pestania) throws IOException {
        switch (pestania){
            case INICIO -> {
                if(!current.equals(pestanias.INICIO)) {
                    this.borderPane.setCenter(FXMLLoader.load(getClass().getResource("/fxml/inicio.fxml")));
                    current = pestanias.INICIO;
                }
            }
            case PERFIL -> {
                if(!current.equals(pestanias.PERFIL)) {
                    this.borderPane.setCenter(FXMLLoader.load(getClass().getResource("/fxml/perfil.fxml")));
                    current = pestanias.MISRECETAS;
                }
            }
            case BUSQUEDAINGREDIENTES -> {
                if(!current.equals(pestanias.BUSQUEDAINGREDIENTES)) {
                    this.borderPane.setCenter(FXMLLoader.load(getClass().getResource("/fxml/busqueda_ingredientes.fxml")));
                    current = pestanias.BUSQUEDAINGREDIENTES;
                }
            }
            case BUSQUEDACATEGORIA -> {
                if(!current.equals(pestanias.BUSQUEDACATEGORIA)) {
                    this.borderPane.setCenter(FXMLLoader.load(getClass().getResource("/fxml/busqueda_categorias.fxml")));
                    current = pestanias.BUSQUEDACATEGORIA;
                }
            }
            case BUSQUEDATEXTO -> {
                if(!current.equals(pestanias.BUSQUEDATEXTO)) {
                    center = FXMLLoader.load(getClass().getResource("/fxml/busqueda_texto.fxml"));

                    this.borderPane.setCenter(center);
                    current = pestanias.BUSQUEDATEXTO;
                }
            }
            case SUBIRRECETA -> {
                if(!current.equals(pestanias.SUBIRRECETA)) {
                    this.borderPane.setCenter(FXMLLoader.load(getClass().getResource("/fxml/subir_receta.fxml")));
                    current = pestanias.SUBIRRECETA;
                }
            }
            case ADMIN -> {
                if(!current.equals(pestanias.ADMIN)) {
                    this.borderPane.setCenter(FXMLLoader.load(getClass().getResource("/fxml/admin.fxml")));
                    current = pestanias.ADMIN;
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + pestania);
        }
    }

}
