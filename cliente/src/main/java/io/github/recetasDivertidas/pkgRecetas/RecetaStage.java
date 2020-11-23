package io.github.recetasDivertidas.pkgRecetas;

import io.github.recetasDivertidas.pkgRecetasDivertidas.Receta;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class RecetaStage extends Stage {
    BorderPane receta;
    RecetaController recetaController;

    public RecetaStage(Receta r) throws IOException {
        super();

        // Cosas de apariencia
        this.setWidth(800);
        this.setHeight(600);
        this.setTitle(r.getTitulo());
        this.getIcons().add(new Image(getClass().getResourceAsStream("/logo_recetas_divertidas.png")));

        // Carga el componente
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/receta.fxml"));
        receta = loader.load();
        recetaController = loader.getController();
        recetaController.setReceta(r);

        // Muestra el componente
        Scene scene = new Scene(receta);
        setScene(scene);
    }
}
