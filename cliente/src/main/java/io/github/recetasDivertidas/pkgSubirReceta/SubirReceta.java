package io.github.recetasDivertidas.pkgSubirReceta;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.CategoriaSubir;
import io.github.recetasDivertidas.pkgComponentes.IngredienteSubir;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaReceta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class SubirReceta {
    @FXML private VBox vboxLeft;
    @FXML private VBox vboxRight;
    @FXML private VBox vboxIngredientes;
    @FXML private VBox vboxCategorias;
    @FXML private TextField txtTitulo;
    @FXML private TextArea txaDescripcion;
    @FXML private TextArea txaInstrucciones;

    @FXML private ComboBox<Ingrediente> cmbIngredientes;
    @FXML private ComboBox<CategoriaReceta> cmbCategorias;

    @FXML
    private void initialize() {
        // Hack para que los combobox no cambien de tamaño al clickearlos
        vboxLeft.setPrefSize(vboxLeft.getWidth(), vboxLeft.getHeight());
        vboxRight.setPrefSize(vboxRight.getWidth(), vboxRight.getHeight());

        // Poner los ingredientes en el combobox
        try {
            cmbIngredientes.getItems().addAll(Ingrediente.getIngredientes());
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }

        // Poner las categorías en el combobox
        try {
            cmbCategorias.getItems().addAll(CategoriaReceta.getCategorias());
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }
    }

    private ArrayList<Ingrediente> getIngredientes() {
        ArrayList<Ingrediente> ingredientes = new ArrayList<>();

        for (Node n : vboxIngredientes.getChildren()) {
            Ingrediente i = (Ingrediente) n.getUserData();
            ingredientes.add(i);
        }

        return ingredientes;
    }

    private ArrayList<CategoriaReceta> getCategorias() {
        ArrayList<CategoriaReceta> categorias = new ArrayList<>();

        for (Node n : vboxCategorias.getChildren()) {
            CategoriaReceta c = (CategoriaReceta) n.getUserData();
            categorias.add(c);
        }

        return categorias;
    }

    @FXML
    private void agregarIngrediente() {

        // Detecta que el combobox tenga seleccionado algo y que no se repita
        if (cmbIngredientes.getValue() != null && !getIngredientes().contains(cmbIngredientes.getValue())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/ingrediente.fxml"));
                Pane paneIngrediente = loader.load();
                IngredienteSubir controllerIngrediente = loader.getController();
                controllerIngrediente.setIngrediente(cmbIngredientes.getValue());
                vboxIngredientes.getChildren().add(paneIngrediente);
            } catch (IOException e) {
                e.printStackTrace();
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "Hubo un error inesperado");
                alerta.showAndWait();
            }
        }

        // Reinicia el combobox
        cmbIngredientes.getSelectionModel().clearSelection();
    }

    @FXML
    private void agregarCategoria() {
        // Detecta que el combobox tenga seleccionado algo y que no se repita
        if (cmbCategorias.getValue() != null && !getCategorias().contains(cmbCategorias.getValue())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/categoria.fxml"));
                Pane paneCategoria = loader.load();
                CategoriaSubir controllerCategoria = loader.getController();
                controllerCategoria.setCategoria(cmbCategorias.getValue());
                vboxCategorias.getChildren().add(paneCategoria);
            } catch (IOException e) {
                e.printStackTrace();
                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "Error inesperado",
                        "Hubo un error inesperado");
                alerta.showAndWait();
            }
        }

        // Reinicia el combobox
        cmbCategorias.getSelectionModel().clearSelection();
    }

    @FXML
    private void subirReceta() {
        Receta receta = new Receta(RecetasDivertidas.username,
                txtTitulo.getText(),
                txaDescripcion.getText(),
                txaInstrucciones.getText(),
                cmbIngredientes.getItems(),
                cmbCategorias.getItems());

        receta.subir();
    }
}
