package io.github.recetasDivertidas.pkgRecetasDivertidas;

import io.github.recetasDivertidas.pkgAdmin.AdminLayout;
import io.github.recetasDivertidas.pkgBusquedaCategoria.BusquedaCategoriaLayout;
import io.github.recetasDivertidas.pkgBusquedaTexto.BusquedaTextoLayout;
import io.github.recetasDivertidas.pkgMisRecetas.MisRecetasLayout;
import io.github.recetasDivertidas.pkgSubir.SubirLayout;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import io.github.recetasDivertidas.pkgBusquedaIngredientes.BusquedaIngredientesLayout;

import java.io.IOException;

public class RecetasDivertidas extends Stage{

    public static boolean admin;
    public BorderPane root;
    public final static String BACKGROUND = "#FFFFFF";
    public final static String HOVERED = "#F1F1F1";
    public static String username;
    public enum pestanias
    {
        INICIO,
        BUSQUEDAINGREDIENTES,
        BUSQUEDACATEGORIA,
        BUSQUEDATEXTO,
        SUBIRRECETA,
        MISRECETAS,
        ADMIN
    }
    private pestanias current;

    public RecetasDivertidas(boolean admin, String username){
        super();
        RecetasDivertidas.admin = admin;
        RecetasDivertidas.username = username;

        root = new BorderPane();
        root.setTop(topPane());
        Scene scene = new Scene(root,800,600);
        this.setScene(scene);
        this.current = pestanias.INICIO;
        this.setTitle("Recetas Divertidas");
        this.getIcons().add(new Image(getClass().getResourceAsStream("/logo_chiquito.png")));

    }

    private HBox topPane(){
        HBox hbox = new HBox();
        //vbox.setFillWidth(true);

        int btnI;
        if(admin){
            btnI = 7;
        }else {
            btnI = 6;
        }

        Button[] btn = new Button[btnI];
        Image[] img = new Image[btnI];
        ImageView[] imageViews = new ImageView[btnI];

        //Este for setea los botones de la izquierda y les dice como verse y que hacer

        for (int i = 0; i < btnI; i++) {
            btn[i] = new Button();


            switch (i) {
                case 0 ->{
                    btn[i].setText("Inicio");
                    img[i] = new Image(getClass().getResourceAsStream("/inicio.png"));
                    btn[i].setOnAction(e -> {
                        try {
                            cambiarPestania(pestanias.INICIO);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }

                case 1 ->{
                    btn[i].setText("Buscar por Ingrediente");
                    btn[i].setOnAction(e -> {
                        try {
                            cambiarPestania(pestanias.BUSQUEDAINGREDIENTES);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }
                case 2 ->{
                    btn[i].setText("Buscar por Texto");
                    btn[i].setOnAction(e -> {
                        try {
                            cambiarPestania(pestanias.BUSQUEDATEXTO);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }
                case 3 ->{
                    btn[i].setText("Buscar por Categoria");
                    btn[i].setOnAction(e -> {
                        try {
                            cambiarPestania(pestanias.BUSQUEDACATEGORIA);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }
                case 4 ->{
                    btn[i].setText("Mis Recetas");
                    btn[i].setOnAction(e -> {
                        try {
                            cambiarPestania(pestanias.MISRECETAS);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }
                case 5 ->{
                    btn[i].setText("Subir Receta");
                    btn[i].setOnAction(e -> {
                        try {
                            cambiarPestania(pestanias.SUBIRRECETA);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }
                case 6 ->{
                    btn[i].setText("Admin");
                    btn[i].setOnAction(e -> {
                        try {
                            cambiarPestania(pestanias.ADMIN);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }
            }

            imageViews[i] = new ImageView(img[i]);
            imageViews[i].setFitHeight(100);
            imageViews[i].setFitWidth(100);

            int finalI = i;
            btn[i].setOnMouseEntered((EventHandler<Event>) event -> btn[finalI].setStyle("-fx-background-color: " + HOVERED));
            btn[i].setOnMouseExited((EventHandler<Event>) event -> btn[finalI].setStyle("-fx-background-color: " + BACKGROUND));
            //btn[i].setGraphic(imageViews[i]);
            btn[i].setStyle("-fx-background-color: " + BACKGROUND);
            hbox.getChildren().add(btn[i]);
        }

        hbox.setStyle("-fx-background-color: "+ BACKGROUND);
        root.setStyle("-fx-background-color: " + BACKGROUND);
        //Estoy enojado
        return hbox;
    }

    private void cambiarPestania(pestanias pestania) throws IOException {
        switch (pestania){
            case INICIO -> {
                if(!current.equals(pestanias.INICIO)) {
                    this.root.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.INICIO;
                }
            }
            case BUSQUEDAINGREDIENTES -> {
                if(!current.equals(pestanias.BUSQUEDAINGREDIENTES)) {
                    this.root.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.BUSQUEDAINGREDIENTES;
                }
            }
            case BUSQUEDACATEGORIA -> {
                if(!current.equals(pestanias.BUSQUEDACATEGORIA)) {
                    this.root.setCenter(new BusquedaCategoriaLayout());
                    current = pestanias.BUSQUEDACATEGORIA;
                }
            }
            case BUSQUEDATEXTO -> {
                if(!current.equals(pestanias.BUSQUEDATEXTO)) {
                    this.root.setCenter(new BusquedaTextoLayout());
                    current = pestanias.BUSQUEDATEXTO;
                }
            }
            case SUBIRRECETA -> {
                if(!current.equals(pestanias.SUBIRRECETA)) {
                    this.root.setCenter(new SubirLayout());
                    current = pestanias.SUBIRRECETA;
                }
            }
            case MISRECETAS -> {
                if(!current.equals(pestanias.MISRECETAS)) {
                    this.root.setCenter(new MisRecetasLayout());
                    current = pestanias.MISRECETAS;
                }
            }
            case ADMIN -> {
                if(!current.equals(pestanias.ADMIN)) {
                    this.root.setCenter(new AdminLayout());
                    current = pestanias.ADMIN;
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + pestania);
        }
    }

}
