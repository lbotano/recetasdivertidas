package io.github.recetasDivertidas.pkgSubir;

import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaIngrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaReceta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;

public class SubirLayout extends BorderPane {

    public SubirLayout() throws IOException {
        Label lblTitulo = new Label("Conoces una receta y quieres añadirla? Subela aqui!");
        lblTitulo.setFont(new Font("Arial",16));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(getGridpane());

        Button btnSubirReceta = new Button("Subir Receta");
        btnSubirReceta.setOnAction(e -> SubirFuncionalidad.subirReceta());

        this.setTop(lblTitulo);
        this.setCenter(scrollPane);
        this.setBottom(btnSubirReceta);
    }

    private GridPane getGridpane() throws IOException {
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);
        gridPane.setGridLinesVisible(false);




        Label lblCategoria = new Label("Que tipo de comida es?");
        lblCategoria.setFont(new Font("Arial",16));
        gridPane.add(lblCategoria, 1, 1,1,1);

        CheckComboBox<CategoriaReceta> cmbCategoriaReceta = new CheckComboBox<>();
        if (Conexion.isSvResponse()) {
            cmbCategoriaReceta.getItems().addAll(SubirFuncionalidad.getCategoriasReceta());
        }
        gridPane.add(cmbCategoriaReceta,2,1,1,1);

        TextField tbTitulo = new TextField();
        tbTitulo.setPrefSize(200, 10);
        tbTitulo.setPromptText("Titulo de la receta");
        gridPane.add(tbTitulo, 0, 1,1,1);

        TextArea tbDescripcion = new TextArea();
        tbDescripcion.setPrefSize(200,50);
        tbDescripcion.setPromptText("Descripcion de la receta");
        gridPane.add(tbDescripcion,0,2,3,1);

        TextArea tbInstrucciones = new TextArea();
        tbInstrucciones.setPrefSize(800,400);
        tbInstrucciones.setPromptText("Instrucciones de la receta");
        gridPane.add(tbInstrucciones,0,3,3,1);

        //Tengo que meter esto en un ScrollPane y añadir un boton de "+" y otro de "-" para añadir y sacar ingredientes
        VBox vboxIngredientes = new VBox();
        SubirFuncionalidad.addIngrediente(vboxIngredientes);
        gridPane.add(vboxIngredientes,0,5,1,1);


        return gridPane;
    }

}
