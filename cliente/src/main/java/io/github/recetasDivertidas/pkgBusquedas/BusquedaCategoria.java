package io.github.recetasDivertidas.pkgBusquedas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaIngrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.CategoriaReceta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.util.ArrayList;

public class BusquedaCategoria {
    @FXML Button btnBuscar;
    @FXML CheckComboBox chkcmbCategorias;
    @FXML VBox vbox;
    private int current_pag;

    @FXML
    private void initialize() throws IOException {
        chkcmbCategorias.getItems().addAll(CategoriaReceta.getCategorias());
        current_pag=0;
    }

    @FXML
    private void buscar() throws IOException {
        ArrayList<Pane> recetas = new ArrayList<>();
        ArrayList<String> message = new ArrayList<>();
        message.add("CONSRECETASCATREC");
        message.add(String.valueOf(current_pag));
        for (Object ob: chkcmbCategorias.getCheckModel().getCheckedItems()) {
            message.add(ob.toString());
        }

        if (Conexion.isSvResponse()){
            ArrayList<String> ans = Conexion.sendMessage(message);
            switch(ans.get(0)){
                case "RESPCONSULTA" ->{
                    for (String receta : ans) {
                        /*Pane res = FXMLLoader.load(getClass().getResource("/fxml/resultado_busqueda.fxml"));
                        recetas.add(res);*/
                        System.out.println(receta);
                    }//Se me olvido como relacionar el resultado con sus id
                    vbox.getChildren().addAll(recetas);
                }
                case "RESPOCONSULTAFAIL" -> {
                    Alerta alerta = new Alerta(Alert.AlertType.ERROR, "No se encontraron recetas", ans.get(1));
                    alerta.showAndWait();
                }
                case "MESSAGEERROR" ->{
                    Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                            "Hubo un problema al comunicarse con el servidor");
                    alerta.showAndWait();
                }
                case "ELEMENTBLANK" ->{
                    Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                            "El mensaje contenia espacios en blanco");
                    alerta.showAndWait();
                }
                case "FORMATERROR" ->{
                    Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error en el mensaje",
                            "Hubo un problema en el formato del mensaje");
                    alerta.showAndWait();
                }
            }
        }else{
            Alerta alerta = new Alerta(Alert.AlertType.ERROR, "Error al conectarse con el servidor.",
                    "Hubo un problema al intentar buscar.");
            alerta.showAndWait();
        }
    }

    public void prevPag(ActionEvent actionEvent) {
    }

    public void nextPag(ActionEvent actionEvent) {
    }
}
