package io.github.recetasDivertidas.pkgBusquedaTexto;

import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class BusquedaTextoLayout extends GridPane {
    private VBox resultado;

    public BusquedaTextoLayout(){
        super();

        Label lblExplain = new Label();
        TextField txtBusqueda = new TextField();
        resultado = new VBox();

        lblExplain.setText("Aqui puedes buscar recetas escribiendo!!");
        lblExplain.setFont(new Font("Arial", 16));

        txtBusqueda.setPromptText("Escribeme");

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> BusquedaTextoFuncionalidad.buscarReceta(resultado));

        this.setPadding(new Insets(10, 10, 10, 10));
        this.setVgap(10);
        this.setHgap(10);

        this.add(lblExplain,0,0);
        this.add(txtBusqueda,0,1);
        this.add(btnBuscar,1,1);
        this.add(resultado,0,2);

    }

    private void addStuff(){

    }



}
