package io.github.recetasDivertidas.pkgBusquedas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.MensajeServerInvalidoException;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class BusquedaTexto {
    @FXML Label lblBuscaTexto;
    @FXML TextField txtBuscar;
    @FXML Button btnBuscar;
    @FXML Button btnNextPag;
    @FXML Button btnPrevPag;
    @FXML VBox vboxResultados;
    private int current_pag;

    @FXML
    private void initialize(){
        current_pag = 0;
    }

    @FXML
    private void buscarRecetas() throws IOException {
        ArrayList<Receta> recetas;
        ArrayList<String> message = new ArrayList<>();
        message.add("CONSRECETATEXT");
        message.add(String.valueOf(current_pag));
        message.add(txtBuscar.getText());

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
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/resultado_busqueda.fxml"));
                                GridPane paneReceta = fxmlLoader.load();
                                ResultadoBusqueda controllerResultadoBusqueda = fxmlLoader.getController();
                                controllerResultadoBusqueda.ponerReceta(r);
                                vboxResultados.getChildren().add(paneReceta);
                            }
                            //vbox.getChildren().addAll(recetas);
                        } catch (MensajeServerInvalidoException e) {
                            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                                "Error inesperado",
                                "Hubo un error inesperado");
                            alerta.showAndWait();
                        }
                    }
                    case "RESPOCONSULTAFAIL" -> {
                        Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error: ", ans.get(1));
                        alerta.showAndWait();
                    }
                    case "MESSAGEERROR", "ELEMENTBLANK", "FORMATERROR" -> {
                        Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error inesperado",
                                "Hubo un error inesperado");
                        alerta.showAndWait();
                    }
                }
            }

        } else {
            Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error al conectarse con el servidor.",
                    "Verifique su conexión a internet.");
            alerta.showAndWait();
        }
    }


    @FXML
    private void prevPag() {

    }

    @FXML
    private void nextPag() {

    }
}
