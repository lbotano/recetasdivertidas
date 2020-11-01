package io.github.recetasDivertidas.pkgSubir;

import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaIngrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaReceta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;

public class SubirLayout extends GridPane {

    public SubirLayout() throws IOException {
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setVgap(8);
        this.setHgap(10);
        this.setGridLinesVisible(false);


        Label lblTitulo = new Label("Conoces una receta y quieres añadirla? Subela aqui!");
        lblTitulo.setFont(new Font("Arial",16));
        this.add(lblTitulo, 0, 0,1,1);

        Label lblCategoria = new Label("Que tipo de comida es?");
        lblCategoria.setFont(new Font("Arial",16));
        this.add(lblCategoria, 1, 1,1,1);

        CheckComboBox<CategoriaReceta> cmbCategoriaReceta = new CheckComboBox<>();
        if (Conexion.isSvResponse()) {
            cmbCategoriaReceta.getItems().addAll(SubirFuncionalidad.getCategoriasReceta());
        }
        this.add(cmbCategoriaReceta,2,1,1,1);

        TextField tbTitulo = new TextField();
        tbTitulo.setPrefSize(200, 10);
        tbTitulo.setPromptText("Titulo de la receta");
        this.add(tbTitulo, 0, 1,1,1);

        TextArea tbDescripcion = new TextArea();
        tbDescripcion.setPrefSize(200,50);
        tbDescripcion.setPromptText("Descripcion de la receta");
        this.add(tbDescripcion,0,2,3,1);

        TextArea tbInstrucciones = new TextArea();
        tbInstrucciones.setPrefSize(800,400);
        tbInstrucciones.setPromptText("Instrucciones de la receta");
        this.add(tbInstrucciones,0,3,3,1);

        //Tengo que meter esto en un ScrollPane y añadir un boton de "+" y otro de "-" para añadir y sacar ingredientes
        HBox hboxIng = getHbox();
        this.add(hboxIng,0,4,1,1);


        Button btnSubirReceta = new Button("Subir Receta");
        btnSubirReceta.setOnAction(e -> SubirFuncionalidad.subirReceta());

        this.add(btnSubirReceta,0,5,1,1);
    }

    private HBox getHbox() throws IOException {
        HBox hbox = new HBox();

        ComboBox<CategoriaIngrediente> cmbCategoriaIngrediente = new ComboBox<>();
        cmbCategoriaIngrediente.setPromptText("Que tipo de ingrediente es?");
        if (Conexion.isSvResponse()) {
            cmbCategoriaIngrediente.getItems().addAll(SubirFuncionalidad.getCategoriasIngrediente());
        }

        ComboBox<Ingrediente> cmbIngrediente = new ComboBox<>();
        cmbIngrediente.setPromptText("Que ingrediente es?");
        if (Conexion.isSvResponse()) {
            cmbIngrediente.getItems().addAll(SubirFuncionalidad.getIngredientes());
        }

        hbox.getChildren().addAll(cmbCategoriaIngrediente,cmbIngrediente);

        return hbox;
    }
}
