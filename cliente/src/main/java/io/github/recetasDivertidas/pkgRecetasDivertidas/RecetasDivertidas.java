package io.github.recetasDivertidas.pkgRecetasDivertidas;

import io.github.recetasDivertidas.pkgBusquedaTexto.BusquedaTextoLayout;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import io.github.recetasDivertidas.pkgBusquedaIngredientes.BusquedaIngredientesLayout;

public class RecetasDivertidas extends Stage{

    public static boolean admin;
    private int btnI;
    public BorderPane root;
    public final static String background = "#FFFFFF";
    public final static String hovered = "#F1F1F1";
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

    public RecetasDivertidas(boolean admin){
        super();
        RecetasDivertidas.admin = admin;

        root = new BorderPane();
        root.setTop(sidePane());
        root.setCenter(addGridPane());
        Scene scene = new Scene(root,800,600);
        this.setScene(scene);
        this.current = pestanias.INICIO;

        /*if (admin){
            this.setWidth(1380);
            this.setHeight(795);
        }else{
            this.setWidth(1380);
            this.setHeight(687);
        }*/


    }

    private GridPane addGridPane() {

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);


        //Username setup
        Label lblUsername = new Label("Usuario:");
        grid.add(lblUsername, 0, 0);

        TextField tbUsername = new TextField();
        tbUsername.setPrefSize(180, 10);
        tbUsername.setPromptText("Escriba su usuario aqui");
        grid.add(tbUsername, 1, 0);

        //Password setup
        Label lblPassword = new Label("Contraseña:");
        grid.add(lblPassword, 0, 1);

        TextField tbPassword = new TextField();
        tbPassword.setPrefSize(180, 10);
        tbPassword.setPromptText("Escriba su contraseña aqui");
        grid.add(tbPassword, 1, 1);


        return grid;
    }

    private HBox sidePane(){
        HBox hbox = new HBox();
        //vbox.setFillWidth(true);

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
                    btn[i].setOnAction(e -> cambiarPestania(pestanias.INICIO));
                }

                case 1 ->{
                    btn[i].setText("Buscar por Ingrediente");
                    img[i] = new Image(getClass().getResourceAsStream("/busqueda_ingrediente.png"));
                    btn[i].setOnAction(e -> cambiarPestania(pestanias.BUSQUEDAINGREDIENTES) );
                }
                case 2 ->{
                    btn[i].setText("Buscar por Texto");
                    img[i] = new Image(getClass().getResourceAsStream("/busqueda_texto.png"));
                    btn[i].setOnAction(e -> cambiarPestania(pestanias.BUSQUEDATEXTO));
                }
                case 3 ->{
                    btn[i].setText("Buscar por Categoria");
                    img[i] = new Image(getClass().getResourceAsStream("/busqueda_categoria.png"));
                    btn[i].setOnAction(e -> cambiarPestania(pestanias.BUSQUEDACATEGORIA));
                }
                case 4 ->{
                    btn[i].setText("Mis Recetas");
                    img[i] = new Image(getClass().getResourceAsStream("/mis_recetas.png"));
                    btn[i].setOnAction(e -> cambiarPestania(pestanias.MISRECETAS));
                }
                case 5 ->{
                    btn[i].setText("Subir Receta");
                    img[i] = new Image(getClass().getResourceAsStream("/subir.jpg"));
                    btn[i].setOnAction(e -> cambiarPestania(pestanias.SUBIRRECETA));
                }
                case 6 ->{
                    btn[i].setText("Admin");
                    img[i] = new Image(getClass().getResourceAsStream("/admin.png"));
                    btn[i].setOnAction(e -> cambiarPestania(pestanias.ADMIN));
                }
            }

            imageViews[i] = new ImageView(img[i]);
            imageViews[i].setFitHeight(100);
            imageViews[i].setFitWidth(100);

            int finalI = i;
            btn[i].setOnMouseEntered((EventHandler<Event>) event -> btn[finalI].setStyle("-fx-background-color: " + hovered));
            btn[i].setOnMouseExited((EventHandler<Event>) event -> btn[finalI].setStyle("-fx-background-color: " + background));
            //btn[i].setGraphic(imageViews[i]);
            btn[i].setStyle("-fx-background-color: " + background);
            hbox.getChildren().add(btn[i]);
        }

        hbox.setStyle("-fx-background-color: "+ background );
        root.setStyle("-fx-background-color: " + background);
        //Estoy enojado
        return hbox;
    }

    private void cambiarPestania(pestanias pestania){
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
                    this.root.setCenter(new BusquedaIngredientesLayout());
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
                    this.root.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.SUBIRRECETA;
                }
            }
            case MISRECETAS -> {
                if(!current.equals(pestanias.MISRECETAS)) {
                    this.root.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.MISRECETAS;
                }
            }
            case ADMIN -> {
                if(!current.equals(pestanias.ADMIN)) {
                    this.root.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.ADMIN;
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + pestania);
        }
    }

}
