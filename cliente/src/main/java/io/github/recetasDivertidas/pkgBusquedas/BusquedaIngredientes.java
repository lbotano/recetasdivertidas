package io.github.recetasDivertidas.pkgBusquedas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.ResultadoBusqueda;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.MensajeServerInvalidoException;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusquedaIngredientes {
    public VBox vboxResultados;
    @FXML Button btnBuscar;
    @FXML CheckComboBox<Ingrediente> chkcmbIngredientes;
    int current_pag;

    @FXML
    public void initialize() throws IOException {
        chkcmbIngredientes.getItems().addAll(Ingrediente.getIngredientes());
        current_pag=0;
    }

    public void nextPag() throws IOException {
        current_pag++;
        if(!buscar()){
            current_pag--;
        }
    }

    public void prevPag() throws IOException {
        if(current_pag >0) {
            current_pag--;
            buscar();
        }
    }

    public boolean buscar() throws IOException {
        ArrayList<Receta> recetas;
        ArrayList<String> message = new ArrayList<>();
        message.add("CONSRECETAING");
        message.add(String.valueOf(current_pag));

        List<Ingrediente> ingredientesSelected = chkcmbIngredientes.getCheckModel().getCheckedItems();
        for(Ingrediente ing : ingredientesSelected){
            System.out.println("Selected: " + ing.toString());
            System.out.println("Send: " + ing.getId());
            message.add(String.valueOf(ing.getId()));
        }

        vboxResultados.getChildren().clear();

        if (Conexion.isSvResponse()){
            ArrayList<String> ans = Conexion.sendMessage(message);
            if (ans.size() > 0) {
                switch (ans.get(0)) {
                    case "RESPCONSULTA" -> {
                        try {
                            // Convierte los strings de la consulta en objetos Receta
                            recetas = Receta.getRecetasConsultaBusquedas(ans);
                            // Muestra las recetas
                            for (Receta r : recetas) {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/componentes/resultado_busqueda.fxml"));
                                HBox paneReceta = fxmlLoader.load();
                                ResultadoBusqueda controllerResultadoBusqueda = fxmlLoader.getController();
                                controllerResultadoBusqueda.ponerReceta(r);
                                vboxResultados.getChildren().add(paneReceta);
                            }
                            return true;
                        } catch (MensajeServerInvalidoException e) {
                            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                                    "Error inesperado",
                                    "Hubo un error inesperado");
                            alerta.showAndWait();
                            return false;
                        }
                    }
                    case "RESPOCONSULTAFAIL" -> {
                        Alerta alerta = new Alerta(Alert.AlertType.NONE, "Upsi", ans.get(1));
                        alerta.showAndWait();
                        return false;
                    }
                    case "MESSAGEERROR", "ELEMENTBLANK", "FORMATERROR" -> {
                        Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error inesperado",
                                "Hubo un error inesperado");
                        alerta.showAndWait();
                        return false;
                    }
                }
            }

        } else {
            Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error al conectarse con el servidor.",
                    "Verifique su conexión a internet.");
            alerta.showAndWait();
            return false;
        }
        return false;
    }
}
