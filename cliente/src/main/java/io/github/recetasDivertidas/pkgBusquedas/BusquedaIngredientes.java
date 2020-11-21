package io.github.recetasDivertidas.pkgBusquedas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.ResultadoBusqueda;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.util.ArrayList;

public class BusquedaIngredientes {
    public VBox vboxResultados;
    @FXML
    Button btnBuscar;
    @FXML
    CheckComboBox<Ingrediente> chkcmbIngredientes;
    int paginaActual;

    @FXML
    public void initialize() throws IOException {
        chkcmbIngredientes.getItems().addAll(Ingrediente.getIngredientes());
        paginaActual = 0;
    }

    public void nextPag() {
        if(vboxResultados.getChildren().size() == 10){
            paginaActual++;
            buscar();
        }
    }

    public void prevPag() {
        if (paginaActual > 0) {
            paginaActual--;
            buscar();
        }
    }

    public void buscar() {
        try {
            ArrayList<Receta> recetasEncontradas = Receta.getRecetasIngredientes(paginaActual, chkcmbIngredientes.getCheckModel().getCheckedItems());
            if (recetasEncontradas.size() > 0) {
                vboxResultados.getChildren().clear();
                for (Receta receta : recetasEncontradas) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/resultado_busqueda.fxml"));
                    Pane res = loader.load();
                    ResultadoBusqueda resController = loader.getController();
                    resController.ponerReceta(receta);
                    vboxResultados.getChildren().add(res);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }
    }
}
