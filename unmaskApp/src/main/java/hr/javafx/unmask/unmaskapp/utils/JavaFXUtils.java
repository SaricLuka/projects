package hr.javafx.unmask.unmaskapp.utils;

import hr.javafx.unmask.unmaskapp.UnmaskApplication;
import hr.javafx.unmask.unmaskapp.UnmaskApplicationController;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class JavaFXUtils {
    public static Boolean showAlert(AlertRecord alertRecord) {
        Alert alert = new Alert(alertRecord.alertType());
        alert.setTitle(alertRecord.title());
        alert.setHeaderText(null);
        alert.setContentText(alertRecord.content());
        if(!alertRecord.question()) {
            alert.showAndWait();
            return false;
        }else{
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == buttonTypeYes) {
                return true;
            } else {
                return false;
            }
        }
    }
    public static void changeScene(String name){
        FXMLLoader fxmlLoader = new FXMLLoader(UnmaskApplication.class.getResource(name));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
            logger.info(e.getMessage());
        }

        UnmaskApplication.getStage().setScene(scene);
        UnmaskApplication.getStage().show();
    }
}
