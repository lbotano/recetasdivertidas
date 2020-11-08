package io.github.recetasDivertidas.pkgBusquedas;

import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.Ingrediente;
import io.github.recetasDivertidas.pkgRecetasDivertidas.RecetasDivertidas;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class BusquedaTexto {
    @FXML Label lblBuscaTexto;
    @FXML TextField txtBuscar;
    @FXML Button btnBuscar;
    @FXML ScrollPane scrollPane;
    @FXML BorderPane borderPane;
    @FXML ButtonBar btnBar;
    @FXML Button btnNextPag;
    @FXML Button btnPrevPag;
    @FXML VBox vbox;
    private int current_pag;

    @FXML
    private void initialize(){
        current_pag = 0;
    }

    @FXML
    private void buscarRecetas() throws IOException {
        ArrayList<GridPane> recetas = new ArrayList<>();
        ArrayList<String> message = new ArrayList<>();
        message.add("CONSRECETATEXT");
        message.add(String.valueOf(current_pag));
        message.add(txtBuscar.getText());

        if (Conexion.isSvResponse()){
            ArrayList<String> ans = Conexion.sendMessage(message);
            if (ans.size() > 0) {
                switch (ans.get(0)) {
                    case "RESPCONSULTA" -> {
                        recetas = getRecetas(ans);
                        if (recetas != null) {
                            vbox.getChildren().addAll(recetas);
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

    //Precisa que RESPCONSULTA este implementada
    private ArrayList<GridPane> getRecetas(ArrayList<String> ans){
        for (int i = 1; i < ans.size(); i++) {
            if (ans.get(i).equals("id")){}
        }


        return null;
    }


    @FXML
    private void prevPag() {

    }

    @FXML
    private void nextPag() {

    }
}
