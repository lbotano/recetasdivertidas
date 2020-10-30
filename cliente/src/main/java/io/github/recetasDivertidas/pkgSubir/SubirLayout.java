package io.github.recetasDivertidas.pkgSubir;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import org.controlsfx.control.CheckComboBox;

public class SubirLayout extends GridPane {

    public SubirLayout(){
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

        CheckComboBox<String> cmbCategoriaReceta = new CheckComboBox<>();
        cmbCategoriaReceta.getItems().add("SI");
        cmbCategoriaReceta.getItems().add("NO");
        cmbCategoriaReceta.getItems().add("JAMAS!");
        this.add(cmbCategoriaReceta,1,2);

        CheckComboBox cmbCategoriaIngrediente = new CheckComboBox();

        CheckComboBox cmbIngrediente = new CheckComboBox();



        Button btnSubirReceta = new Button("Subir Receta");
        //btnSubirReceta.setOnAction(e -> SubirFuncionalidad.subirReceta());
    }
}
