package io.github.recetasDivertidas.pkgInicio;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import io.github.recetasDivertidas.pkgBusquedaIngredientes.BusquedaIngredientesLayout;

public class RecetasDivertidas extends Stage{

    public static boolean admin;
    private int btnI;
    public BorderPane borderPane;
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

        borderPane = new BorderPane();
        borderPane.setLeft(sidePane());
        //borderPane.setCenter(addGridPane());
        borderPane.getLeft().autosize();
        Scene scene = new Scene(borderPane);
        this.setScene(scene);
        this.current = pestanias.INICIO;

        if (admin){
            this.setWidth(1380);
            this.setHeight(795);
        }else{
            this.setWidth(1380);
            this.setHeight(687);
        }
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

    private VBox sidePane(){
        VBox vbox = new VBox();
        vbox.setFillWidth(true);
        pestanias current = pestanias.ADMIN;

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
                    img[i] = new Image(getClass().getResourceAsStream("/inicio.png"));
                    btn[i].setOnAction(e -> cambiarPestania(pestanias.INICIO));
                }

                case 1 ->{
                    img[i] = new Image(getClass().getResourceAsStream("/busqueda_ingrediente.png"));
                    btn[i].setOnAction(e -> cambiarPestania(pestanias.BUSQUEDAINGREDIENTES) );
                }
                case 2 ->{
                    img[i] = new Image(getClass().getResourceAsStream("/busqueda_texto.png"));
                    btn[i].setOnAction(e -> cambiarPestania(pestanias.BUSQUEDATEXTO));
                }
                case 3 ->{
                    img[i] = new Image(getClass().getResourceAsStream("/images.jpg"));
                    btn[i].setOnAction(e -> cambiarPestania(pestanias.BUSQUEDACATEGORIA));
                }
                //default -> throw new IllegalStateException("Unexpected value: " + i);
            }

            imageViews[i] = new ImageView(img[i]);
            imageViews[i].setFitHeight(100);
            imageViews[i].setFitWidth(100);


            btn[i].setGraphic(imageViews[i]);
            //btn[i].setStyle("-fx-background-color: pink");
            vbox.getChildren().add(btn[i]);
        }

        return vbox;
    }

    private void cambiarPestania(pestanias pestania){
        switch (pestania){
            case INICIO -> {
                if(!current.equals(pestanias.INICIO)) {
                    this.borderPane.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.INICIO;
                }
            }
            case BUSQUEDAINGREDIENTES -> {
                if(!current.equals(pestanias.BUSQUEDAINGREDIENTES)) {
                    this.borderPane.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.BUSQUEDAINGREDIENTES;
                }
            }
            case BUSQUEDACATEGORIA -> {
                if(!current.equals(pestanias.BUSQUEDACATEGORIA)) {
                    this.borderPane.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.BUSQUEDACATEGORIA;
                }
            }
            case BUSQUEDATEXTO -> {
                if(!current.equals(pestanias.BUSQUEDATEXTO)) {
                    this.borderPane.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.BUSQUEDATEXTO;
                }
            }
            case SUBIRRECETA -> {
                if(!current.equals(pestanias.SUBIRRECETA)) {
                    this.borderPane.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.SUBIRRECETA;
                }
            }
            case MISRECETAS -> {
                if(!current.equals(pestanias.MISRECETAS)) {
                    this.borderPane.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.MISRECETAS;
                }
            }
            case ADMIN -> {
                if(!current.equals(pestanias.ADMIN)) {
                    this.borderPane.setCenter(new BusquedaIngredientesLayout());
                    current = pestanias.ADMIN;
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + pestania);
        }
    }

}
