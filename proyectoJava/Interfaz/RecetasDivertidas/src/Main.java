import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch();

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(new Login()));
        stage.getIcons().add(new Image("https://cdn.discordapp.com/attachments/453644623168929803/764301261523779614/logo_chiquito.png"));
        stage.setTitle("Recetas Divertidas");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
