package io.github.recetasDivertidas.pkgComponentes;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import java.io.IOException;
import java.util.*;

public class Calificador extends HBox {
    private final ArrayList<Button> botones = new ArrayList<>(FXCollections.observableArrayList());
    @FXML Button btn1;
    @FXML Button btn2;
    @FXML Button btn3;
    @FXML Button btn4;
    @FXML Button btn5;

    // Los objetos Property pueden ser usados desde los handlers de los eventos.
    private final IntegerProperty calificacionPuesta = new SimpleIntegerProperty();

    private EventHandler<ActionEvent> handlerOnAction;

    public Calificador() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/componentes/calificador.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    public int getCalificacionPuesta() {
        return calificacionPuesta.get();
    }

    @FXML
    private void initialize() {
        botones.add(btn1);
        botones.add(btn2);
        botones.add(btn3);
        botones.add(btn4);
        botones.add(btn5);
    }

    // Cambia visualmente la calificación que está puesta
    public void setCalificacionApariencia(int calificacion) {
        for (Button b : botones)
            b.setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.STAR_ALT));
        for (int i = 0; i < calificacion; i++)
            botones.get(i).setGraphic(new Glyph("FontAwesome", FontAwesome.Glyph.STAR));
    }

    @FXML
    private void calificar(ActionEvent e) {
        // Cambia la calificacion
        Button btn = (Button) e.getSource();
        int calificacion = Integer.parseInt((String) btn.getUserData());
        calificacionPuesta.set(calificacion);

        // Dispara el evento
        ActionEvent evento = new ActionEvent();
        handlerOnAction.handle(evento);
    }

    public void setOnAction(EventHandler<ActionEvent> handler) {
        this.handlerOnAction = handler;
    }
}