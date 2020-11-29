package io.github.recetasDivertidas.pkgBusquedas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgComponentes.ResultadoBusqueda;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.MensajeServerInvalidoException;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class BusquedaTexto {
    @FXML TextField txtBuscar;
    @FXML Button btnBuscar;
    @FXML Button btnNextPag;
    @FXML Button btnPrevPag;
    @FXML VBox vboxResultados;
    private int paginaActual = 0;

    @FXML
    private void buscar() throws IOException, ClassNotFoundException {
        if(txtBuscar.getText().length() > 0) {
            ArrayList<Receta> recetas;
            ArrayList<String> message = new ArrayList<>();
            message.add("CONSRECETATEXT");
            message.add(String.valueOf(paginaActual));
            message.add(txtBuscar.getText());

            if (Conexion.isSvResponse()) {
                ArrayList<String> ans = Conexion.sendMessage(message);
                if (ans.size() > 0) {
                    switch (ans.get(0)) {
                        case "RESPCONSULTA":
                            try {
                                // Convierte los strings de la consulta en objetos Receta
                                recetas = Receta.getRecetasConsultaBusquedas(ans);
                                if(recetas.size() > 0) {
                                    vboxResultados.getChildren().clear();
                                    // Muestra las recetas
                                    for (Receta r : recetas) {
                                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/componentes/resultado_busqueda.fxml"));
                                        HBox paneReceta = fxmlLoader.load();
                                        ResultadoBusqueda controllerResultadoBusqueda = fxmlLoader.getController();
                                        controllerResultadoBusqueda.ponerReceta(r);
                                        vboxResultados.getChildren().add(paneReceta);
                                    }
                                }
                            } catch (MensajeServerInvalidoException e) {
                                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                                        "Error inesperado",
                                        "Hubo un error inesperado");
                                alerta.showAndWait();
                            }
                        break;
                        case "RESPOCONSULTAFAIL":
                            Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error: ", ans.get(1));
                            alerta.showAndWait();
                        break;
                        case "MESSAGEERROR", "ELEMENTBLANK", "FORMATERROR":
                            alerta = new Alerta(Alert.AlertType.ERROR, "Error inesperado",
                                    "Hubo un error inesperado");
                            alerta.showAndWait();
                        break;
                    }
                }

            } else {
                Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error al conectarse con el servidor.",
                        "Verifique su conexión a internet.");
                alerta.showAndWait();
            }
        }
    }

    public void nextPag() throws IOException, ClassNotFoundException {
        if(vboxResultados.getChildren().size() == 10){
            paginaActual++;
            buscar();
        }
    }

    public void prevPag() throws IOException, ClassNotFoundException {
        if (paginaActual > 0) {
            paginaActual--;
            buscar();
        }
    }
}
