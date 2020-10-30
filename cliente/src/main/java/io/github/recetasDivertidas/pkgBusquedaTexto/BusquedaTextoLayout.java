package io.github.recetasDivertidas.pkgBusquedaTexto;

import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import javafx.event.Event;
import javafx.event.EventHandler;
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

public class BusquedaTextoLayout extends BorderPane {
    private VBox resultado;
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
        resultado = new VBox();

        lblExplain.setText("Aqui puedes buscar recetas escribiendo!!");
        lblExplain.setFont(new Font("Arial", 16));

        txtBusqueda.setPromptText("Escribeme");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> BusquedaTextoFuncionalidad.buscarReceta(resultado));

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

        scrollPane.setContent(resultado);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        return scrollPane;
    }

    private HBox getHbox(){
        HBox hbox = new HBox();
        Button btnNextPage = new Button("->");
        Button btnPrevPage = new Button("<-");

        //Toy desarrollando esto
        btnNextPage.setOnAction(e -> BusquedaTextoFuncionalidad.obtenerRecetas(pagActual + 1, txtBusqueda.getText()));
        btnPrevPage.setOnAction(e -> BusquedaTextoFuncionalidad.obtenerRecetas(pagActual - 1, txtBusqueda.getText()));

        hbox.getChildren().addAll(btnPrevPage,btnNextPage);

        return hbox;
    }



}
