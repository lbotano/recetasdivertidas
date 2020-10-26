package io.github.recetasDivertidas.pkgInicio;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
//Para ser honesto, todavia no se que chota poner en el inicio
//Hagamos de cuenta que es reddit y queremos que haya posts, asi que podria
//ser un vbox y que se vaya stackeando uno abajo del otro, qcyo
public class InicioLayout extends BorderPane {
    public InicioLayout() {
        this.setTop(new Button());
        this.setLeft(addVBox());
    }
    private VBox addVBox(){
        VBox vbox = new VBox();
        vbox.setFillWidth(true);

        

        return vbox;
    }
}
