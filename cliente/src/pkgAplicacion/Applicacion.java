package pkgAplicacion;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pkgConexion.Conexion;
import pkgInicio.RecetasDivertidas;
import pkgLogin.LayoutLogin;

public final class Applicacion extends Application {

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
        window = new RecetasDivertidas(false);
        window.getIcons().add(new Image(getClass().getResourceAsStream("/res/logo_chiquito.png")));
        window.setTitle("Recetas Divertidas");
        window.setResizable(true);

        //Si queres probar el cliente sin conexion metele un ! aca y en LayoutLogin
        if(!Conexion.isSvResponse()){
            window.show();
        }else{
            Alerta alerta = new Alerta(Alert.AlertType.ERROR, "No se ha podido conectar con el servidor",
                    "Asegurese de estar conectado antes de iniciar el programa");
            alerta.showAndWait();

            stop();
        }

    }

    public static void hide(){
        window.hide();
    }

    @Override
    public void init() throws Exception {
        super.init();
        layoLg = new LayoutLogin();
        Login = new Scene(layoLg);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}