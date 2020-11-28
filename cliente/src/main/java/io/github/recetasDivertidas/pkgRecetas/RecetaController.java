package io.github.recetasDivertidas.pkgRecetas;

import io.github.recetasDivertidas.pkgAbrirMultimedia.MultimediaStage;
import io.github.recetasDivertidas.pkgAplicacion.Alerta;
import io.github.recetasDivertidas.pkgAplicacion.Aplicacion;
import io.github.recetasDivertidas.pkgComponentes.Calificador;
import io.github.recetasDivertidas.pkgComponentes.CategoriaBajar;
import io.github.recetasDivertidas.pkgComponentes.IngredienteBajar;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgRecetasDivertidas.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class RecetaController {
    @FXML private Button btnBorrar;
    @FXML private Label lblTitulo;
    @FXML private Label lblCalificacion;
    @FXML private Calificador calificador;
    @FXML private Label lblCantCalificaciones;
    @FXML private Label lblDescripcion;
    @FXML private Label lblInstrucciones;
    @FXML private VBox vboxIngredientes;
    @FXML private VBox vboxCategorias;
    @FXML private VBox vboxMultimediaBox;
    @FXML private FlowPane fpMultimedia;

    private Receta receta;

    public void setReceta(Receta receta) {
        this.receta = receta;

        btnBorrar.setVisible(false);
        //Si sos el autor o admin entonces podes borrar receta
        if (this.receta.getAutor().equals(RecetasDivertidas.username) || RecetasDivertidas.logueadoComoAdmin)
            btnBorrar.setVisible(true);
        lblTitulo.setText(this.receta.getTitulo());
        lblCalificacion.setText(String.valueOf(this.receta.getCalificacion()));
        calificador.setCalificacionApariencia(this.receta.getCalificacionPropia());
        lblCantCalificaciones.setText("(" + this.receta.getCantCalificaciones() + ")");
        lblDescripcion.setText(this.receta.getDescripcion());
        lblInstrucciones.setText(this.receta.getInstrucciones());

        // Añadir ingredientes
        try {
            for (Ingrediente i : this.receta.getIngredientes()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/ingrediente_bajar.fxml"));
                Pane ingPane = loader.load();
                IngredienteBajar ingController = loader.getController();
                ingController.setIngrediente(i);
                vboxIngredientes.getChildren().add(ingPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado");
            alerta.showAndWait();
        }

        // Añadir categorías de receta
        try {
            for (CategoriaReceta c : this.receta.getCategoriasReceta()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/categoria_bajar.fxml"));
                Pane catPane = loader.load();
                CategoriaBajar catController = loader.getController();
                catController.setCategoria(c);
                vboxCategorias.getChildren().add(catPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado.");
            alerta.showAndWait();
        }

        // Añadir categorías de ingrediente
        try {
            for (CategoriaIngrediente c : this.receta.getCategoriasIngrediente()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/categoria_bajar.fxml"));
                Pane catPane = loader.load();
                CategoriaBajar catController = loader.getController();
                catController.setCategoria(c);
                vboxCategorias.getChildren().add(catPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                    "Error inesperado",
                    "Hubo un error inesperado.");
            alerta.showAndWait();
        }

        // Añadir multimedia
        if (this.receta.getMultimedia().size() < 1 && fpMultimedia.getParent() instanceof Pane) {
            // Si no hay multimedia elimina todos los elementos gráficos que tengan que ver con ella.
            ((Pane) fpMultimedia.getParent()).getChildren().remove(vboxMultimediaBox);
        } else {
            for (Multimedia m : this.receta.getMultimedia()) {
                ImageView multimediaImg = new ImageView(m.getImg());
                multimediaImg.setUserData(m);

                multimediaImg.setFitWidth(190);
                multimediaImg.setFitHeight(100);
                multimediaImg.setPreserveRatio(true);
                multimediaImg.setStyle("-fx-cursor: hand");

                fpMultimedia.getChildren().add(multimediaImg);

                multimediaImg.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        ImageView source = (ImageView) mouseEvent.getSource();
                        Multimedia multimedia = (Multimedia) source.getUserData();
                        if (multimedia.esVideoYoutube()) {
                            try {
                                Desktop.getDesktop().browse(new URI(multimedia.getUrl()));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                                        "Error inesperado",
                                        "No se pudo abrir el video");
                                alerta.showAndWait();
                            }
                        } else {
                            MultimediaStage ventanaMultimedia = new MultimediaStage((Multimedia) source.getUserData());
                            ventanaMultimedia.show();
                        }
                    }
                });
            }
        }

        calificador.setOnAction(actionEvent -> {
            receta.calificar(calificador.getCalificacionPuesta());
            receta.actualizarSimple();
            calificador.setCalificacionApariencia(receta.getCalificacionPropia());
        });
    }

    @FXML
    private void borrarReceta() {
        //Alerta de si quiere borrar receta
        Alert alertPregunta = new Alert(Alert.AlertType.CONFIRMATION);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ImageView imgView = new ImageView(new Image(getClass().getResourceAsStream("/atention.png")));
        imgView.setSmooth(false);
        alertPregunta.setTitle("Recetas divertidas");
        alertPregunta.setHeaderText("¿Está segurx que quiere borrar la receta?");
        alertPregunta.setGraphic(imgView);
        alertPregunta.setContentText("No hay marcha atrás en esta acción");
        alertPregunta.setX(screenSize.getWidth()/2.5);
        alertPregunta.setY(screenSize.getHeight()/4);
        alertPregunta.initOwner(Aplicacion.window.getScene().getWindow());

        //Obtiene la respuesta
        alertPregunta.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ArrayList<String> msg = new ArrayList<>();
                if(RecetasDivertidas.logueadoComoAdmin){
                    msg.add("BORRARREC");
                    msg.add(String.valueOf(this.receta.getId()));
                }else {
                    msg.add("BORRARRECUSU");
                    msg.add(String.valueOf(this.receta.getId()));
                    msg.add(RecetasDivertidas.username);
                }

                try {
                    ArrayList<String> ans = Conexion.sendMessage(msg);
                    switch (ans.get(0)){
                        case "BORRARRECOK" -> {
                            Alerta alerta = new Alerta(Alert.AlertType.CONFIRMATION,
                                    "Borrado exitoso",
                                    "La receta se ha borrado con exito");
                            alerta.showAndWait();
                            Stage stage = (Stage) btnBorrar.getScene().getWindow();
                            stage.close();
                        }
                        case "BORRARRECFAIL" -> {
                            Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                                    "Error al borrar receta",
                                    ans.get(1));
                            alerta.showAndWait();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    Alerta a = new Alerta(Alert.AlertType.ERROR,
                            "Error inesperado",
                            "Hubo un error inesperado");
                    a.showAndWait();
                }
            }
        });
    }
}
