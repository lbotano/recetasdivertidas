package pkgRecetasDivertidas;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pkgLogin.LayoutLogin;

public final class RecetasDivertidas extends Application {

    //Stage significa la ventana
    //Scene es el grupo de contenido de la ventana
    //Las escenas contienen Layouts tales como el gridpane, hbox, etc
    //Los layouts poseen todos los nodos
    //Los nodos serian las "cosas" que podes poner, como los botones, textboxes,etc
    public static Stage window = new Stage();
    public static Scene Login;
    public LayoutLogin layoLg;

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        window.setScene(Login);
        window.getIcons().add(new Image("https://cdn.discordapp.com/attachments/453644623168929803/764301261523779614/logo_chiquito.png"));
        window.setTitle("Recetas Divertidas");
        window.setResizable(false);
        window.show();
    }

    public static void setScene(Scene scnName){
        window.setScene(scnName);
    }

    @Override
    public void init() throws Exception {
        super.init();
        layoLg = new LayoutLogin();
        Login = new Scene(layoLg);
    }
}
