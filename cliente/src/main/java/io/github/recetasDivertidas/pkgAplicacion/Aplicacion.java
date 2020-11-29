package io.github.recetasDivertidas.pkgAplicacion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import io.github.recetasDivertidas.pkgConexion.Conexion;
import io.github.recetasDivertidas.pkgLogin.Login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

public final class Aplicacion extends Application {

    // Stage significa la ventana
    // Scene es el grupo de contenido de la ventana
    // Las escenas contienen Layouts tales como el gridpane, hbox, etc
    // Los layouts poseen todos los nodos
    // Los nodos serian las "cosas" que podes poner, como los botones, textboxes,etc
    public static Stage window = new Stage();
    public static Scene loginScene;
    public Login layoLg;
    private final String pathConfig = "./configClient.properties";

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        window.setScene(loginScene);
        window.getIcons().add(new Image(getClass().getResourceAsStream("/logo_recetas_divertidas.png")));
        window.setTitle("Recetas Divertidas");
        File archivoConfig = new File(pathConfig);
        //si creo el archivo
        if(archivoConfig.createNewFile()){
            FileWriter writer = new FileWriter(pathConfig);
            writer.write("ServerIP=0.0.0.0");
            writer.close();
            Alerta alerta = new Alerta(Alert.AlertType.INFORMATION,
                    "Se ha creado el archivo de configuración",
                    "Se encuentra en el mismo directorio que el aplicativo, por favor configurelo y vuelva a iniciar el cliente");
            alerta.showAndWait();
            //si no creo el archivo cargar los datos
        }else{
            cargarDatosArchivo();
            System.out.println("Configuración cargada");
            Conexion.iniciarConexion();
        }
        System.out.println(Conexion.HOST);
        if (Conexion.isSvResponse()) {
            window.show();
        } else {
            Alerta alerta = new Alerta(Alert.AlertType.ERROR, "No se ha podido conectar con el servidor.",
                    "Asegúrese de estar conectado antes de iniciar el programa.");
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
        layoLg = new Login();
        loginScene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/login.fxml")));
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void cargarDatosArchivo(){
        try {
            Properties propiedades = new Properties();

            FileInputStream file = new FileInputStream(pathConfig);
            propiedades.load(file);
            file.close();
            if(!Conexion.setServerIP(propiedades.getProperty("ServerIP"))){

                Alerta alerta = new Alerta(Alert.AlertType.ERROR,
                        "No se ha podido establecer la IP del server",
                        "Compruebe que sea un IP valida");
                alerta.showAndWait();
            }
        } catch (Exception e) {
            System.out.println("Hubo un error inesperado al leer el archivo de configuración.");
        }
    }
}
