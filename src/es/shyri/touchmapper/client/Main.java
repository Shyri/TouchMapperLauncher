package es.shyri.touchmapper.client;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        URL location = getClass().getResource("main.fxml");
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        Parent root = fxmlLoader.load(location.openStream());
        primaryStage.setTitle("Touch Mapper Launcher");
        primaryStage.show();
        Scene scene = new Scene(root, 500, 275);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        ((MainController) fxmlLoader.getController()).terminate();
        super.stop();
    }
}
