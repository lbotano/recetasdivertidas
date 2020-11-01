package io.github.recetasDivertidas.pkgBusquedaTexto;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;

public class BusquedaTextoLayout extends BorderPane {
    private VBox vbox;
    private int pagActual = 0;
    private TextField txtBusqueda;

    /*Aca se maneja como se ve la pestaña de busqueda de texto y se intenta que todo lo que es funcionalidad
      se maneje en BusquedaTextoFuncionalidad, por ende los eventos se manejan desde ahi
     */
    public BusquedaTextoLayout(){
        super();

        this.setTop(getGridPane());
        this.setCenter(getScrollPane());
        this.setBottom(getHbox());

    }

    private GridPane getGridPane(){
        GridPane gridPane = new GridPane();
        Label lblExplain = new Label();
        txtBusqueda = new TextField();
        vbox = new VBox();

        lblExplain.setText("Encuentra recetas escribiendo aqui");
        lblExplain.setFont(new Font("Arial", 16));

        txtBusqueda.setPromptText("Escríbeme");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> {
            try {
                BusquedaTextoFuncionalidad.obtenerRecetas(vbox,pagActual,txtBusqueda.getText());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.add(lblExplain,0,0);
        gridPane.add(txtBusqueda,0,1);
        gridPane.add(btnBuscar,1,1);

        return gridPane;
    }

    private ScrollPane getScrollPane(){
        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setContent(vbox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        return scrollPane;
    }

    private HBox getHbox(){
        HBox hbox = new HBox();
        Button btnNextPage = new Button("->"); // TODO: Poner conitos UwU
        Button btnPrevPage = new Button("<-");

        //Toy desarrollando esto
        btnNextPage.setOnAction(e -> {
            try {
                BusquedaTextoFuncionalidad.obtenerRecetas(vbox,pagActual + 1, txtBusqueda.getText());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        btnPrevPage.setOnAction(e -> {
            if (pagActual != 0) {
                try {
                    BusquedaTextoFuncionalidad.obtenerRecetas(vbox,pagActual - 1, txtBusqueda.getText());
                    pagActual--;
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        hbox.getChildren().addAll(btnPrevPage,btnNextPage);

        return hbox;
    }



}
