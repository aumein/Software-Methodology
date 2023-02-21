package songlib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SongLib extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("songlib.fxml"));
        loader.setController(new Controller());

        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.initialize();
        
        primaryStage.setTitle("Songlib");
        primaryStage.setScene(new Scene(root, 900, 400));
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
