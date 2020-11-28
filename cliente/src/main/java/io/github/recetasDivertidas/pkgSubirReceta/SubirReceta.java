package io.github.recetasDivertidas.pkgSubirReceta;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.CategoriaSubir;
import io.github.recetasDivertidas.pkgComponentes.IngredienteSubir;
import io.github.recetasDivertidas.pkgComponentes.MultimediaCargado;
import io.github.recetasDivertidas.pkgRecetasDivertidas.*;
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
import java.io.InvalidObjectException;
import java.util.ArrayList;

public class SubirReceta {
    @FXML private TextField txtMultimedia;
    @FXML private VBox vboxMultimedia;
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
        } catch (Exception e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }

        // Poner las categorías en el combobox
        try {
            cmbCategorias.getItems().addAll(CategoriaReceta.getCategorias());
        } catch (Exception e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }
    }

    private ArrayList<Ingrediente> getIngredientes() throws InvalidObjectException {
        ArrayList<Ingrediente> ingredientes = new ArrayList<>();

        for (Node n : vboxIngredientes.getChildren()) {
            Ingrediente i = (Ingrediente) n.getUserData();
            if (i.getCantidad() <= 0) throw new InvalidObjectException("Hay un ingrediente que no tiene cantidad");
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

    private ArrayList<Multimedia> getMultimedia() {
        ArrayList<Multimedia> multimedia = new ArrayList<>();

        for (Node n : vboxMultimedia.getChildren()) {
            Multimedia m = (Multimedia) n.getUserData();
            multimedia.add(m);
        }

        return multimedia;
    }

    @FXML
    private void agregarIngrediente() {

        // Detecta que el combobox tenga seleccionado algo y que no se repita
        try {
            if (cmbIngredientes.getValue() != null && !getIngredientes().contains(cmbIngredientes.getValue())) {
                try {
                    FXMLLoader loader =
                            new FXMLLoader(getClass().getResource("/fxml/componentes/ingrediente_subir.fxml"));
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
        } catch (InvalidObjectException e) {
            // Se ignora porque no importa por ahora si un ingrediente no tiene especificada su cantidad
        }

        // Reinicia el combobox
        cmbIngredientes.getSelectionModel().clearSelection();
    }

    @FXML
    private void agregarCategoria() {
        // Detecta que el combobox tenga seleccionado algo y que no se repita
        if (cmbCategorias.getValue() != null && !getCategorias().contains(cmbCategorias.getValue())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/categoria_subir.fxml"));
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

    public void agregarMultimedia() throws IOException {
        try {
            if (txtMultimedia.getText().length() > 0) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/multimedia.fxml"));
                Pane multimedia = loader.load();
                MultimediaCargado multimediaController = loader.getController();
                multimediaController.setMultimedia(txtMultimedia.getText());

                txtMultimedia.setText("");
                vboxMultimedia.getChildren().add(multimedia);
            }
        } catch (IllegalArgumentException e) {
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Enlace inválido",
                    "El enlace multimedia es inválido.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void subirReceta() {
        try {
            if (checkItems()) {
                Receta receta = new Receta(txtTitulo.getText(),
                        txaDescripcion.getText(),
                        txaInstrucciones.getText(),
                        getIngredientes(),
                        getCategorias(),
                        getMultimedia());

                receta.subir();
                limpiarCampos();
            }
        } catch (InvalidObjectException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error al subir receta",
                    "Al menos uno de los ingredientes no tiene especificada una cantidad");
            alerta.showAndWait();
        }
    }

    private boolean checkItems() {
        boolean datos_ok = true;
        Alerta alerta;
        String partes = "";
        final String STYLE_BUENO = "-fx-control-inner-background: #CCFFCC";
        final String STYLE_MALO = "-fx-control-inner-background: #FFCCCC";

        if (txtTitulo.getText().length() < 3) {
            txtTitulo.setStyle(STYLE_MALO);
            partes += "Titulo\n";
            datos_ok = false;
        } else {
            txtTitulo.setStyle(STYLE_BUENO);
        }

        if (txaDescripcion.getText().length() < 6) {
            txaDescripcion.setStyle(STYLE_MALO);
            partes += "Descripcion\n";
            datos_ok = false;
        } else {
            txaDescripcion.setStyle(STYLE_BUENO);
        }

        if (txaInstrucciones.getText().length() < 6) {
            txaInstrucciones.setStyle(STYLE_MALO);
            partes += "Instrucciones\n";
            datos_ok = false;
        } else {
            txaInstrucciones.setStyle(STYLE_BUENO);
        }

        try {
            if (getIngredientes().size() == 0) {
                partes += "Ingredientes\n";
                datos_ok = false;
            }
        } catch (InvalidObjectException e) {
            // Hay un ingrediente sin unidad
        }

        if (getCategorias().size() == 0) {
            partes += "Categorias";
            datos_ok = false;
        }

        if (!datos_ok){
            alerta = new Alerta(Alert.AlertType.ERROR,
                    "Los siguientes campos están vacíos o son muy cortos:",
                    partes);
            alerta.showAndWait();
        }

        return datos_ok;
    }

    // Limpia todos los campos
    private void limpiarCampos() {
        txtTitulo.setStyle("");
        txaDescripcion.setStyle("");
        txaInstrucciones.setStyle("");

        txtTitulo.clear();
        txaDescripcion.clear();
        txaInstrucciones.clear();
        txtMultimedia.clear();
        cmbIngredientes.getSelectionModel().clearSelection();
        cmbCategorias.getSelectionModel().clearSelection();
        vboxIngredientes.getChildren().clear();
        vboxCategorias.getChildren().clear();
        txtMultimedia.clear();
        vboxMultimedia.getChildren().clear();
    }
}
