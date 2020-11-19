package io.github.recetasDivertidas.pkgBusquedas;

import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;

public class BusquedaIngredientes {
    @FXML Button btnBuscar;
    @FXML CheckComboBox chkcmbIngredientes;
    int current_pag;

    @FXML
    public void initialize() throws IOException {
        chkcmbIngredientes.getItems().addAll(Ingrediente.getIngredientes());
        current_pag=0;
    }

    public void nextPag() {
    }

    public void prevPag() {
    }

    public void buscar() {
    }
}
