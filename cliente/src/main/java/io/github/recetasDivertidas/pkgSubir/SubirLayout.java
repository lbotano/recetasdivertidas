package io.github.recetasDivertidas.pkgSubir;

import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Categoria;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;

public class SubirLayout extends GridPane {

    public SubirLayout() throws IOException {
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setVgap(8);
        this.setHgap(10);
        this.setGridLinesVisible(true);


        Label lblTitulo = new Label("Conoces una receta y quieras añadirla? Subela aqui!");
        lblTitulo.setFont(new Font("Arial",16));
        this.add(lblTitulo, 0, 0);

        TextField tbTitulo = new TextField();
        tbTitulo.setPrefSize(200, 10);
        tbTitulo.setPromptText("Titulo de la receta");
        this.add(tbTitulo, 0, 2);

        TextArea tbDescripcion = new TextArea();
        tbDescripcion.setPrefSize(300,50);
        tbDescripcion.setPromptText("Descripcion de la receta");
        //tbDescripcion.setAlignment(Pos.TOP_LEFT);
        tbDescripcion.setOpaqueInsets(new Insets(100,100,10,10));
        this.add(tbDescripcion,0,3,2,1);

        TextArea tbInstrucciones = new TextArea();
        tbInstrucciones.setPrefSize(300,250);
        tbInstrucciones.setPromptText("Instrucciones de la receta");
        tbInstrucciones.setOpaqueInsets(new Insets(100,100,10,10));
        this.add(tbInstrucciones,0,4,2,1);

        CheckComboBox<Categoria> cmbCategoriaReceta = new CheckComboBox<>();
        if (Conexion.isSvResponse()) {
            cmbCategoriaReceta.getItems().addAll(SubirFuncionalidad.getCategoriasReceta());
        }
        this.add(cmbCategoriaReceta,1,2,2,1);

        CheckComboBox cmbCategoriaIngrediente = new CheckComboBox();

        CheckComboBox cmbIngrediente = new CheckComboBox();



        Button btnSubirReceta = new Button("Subir Receta");
        //btnSubirReceta.setOnAction(e -> SubirFuncionalidad.subirReceta());
    }
}
