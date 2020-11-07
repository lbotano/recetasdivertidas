package io.github.recetasDivertidas.pkgRecetasDivertidas;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RecetaController {
    @FXML Label titulo;
    @FXML Label descripcion;
    @FXML Label instrucciones;
    @FXML Label calificacion;
    @FXML Label ingredientes;
    private Receta receta;
    private ArrayList<Ingrediente> listIngredientes;
    private ArrayList<CategoriaReceta> catReceta;
    private ArrayList<CategoriaIngrediente> catIngrediente;

    @FXML
    public void initialize(){
    }

    public void setReceta(Receta receta){
        /*this.receta = receta;
        this.titulo.setText(receta.titulo);
        this.descripcion.setText(receta.descripcion);
        this.instrucciones.setText(receta.instrucciones);
        this.calificacion.setText(receta.calificacion);
        this.ingredientes.*/
    }

}
