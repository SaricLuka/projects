package hr.javafx.unmask.unmaskapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UnmaskApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(UnmaskApplication.class.getResource("unmask.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Unmask");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
        logger.info("Program started.");
        launch();
    }
}