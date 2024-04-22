package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.utils.JavaFXUtils;
import hr.javafx.unmask.unmaskapp.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class LogOutScreenController {
    @FXML
    public void logOut(){
        AlertRecord alertInfo = new AlertRecord("Please confirm", "Do you want to proceed?", Alert.AlertType.CONFIRMATION, true);
        if(JavaFXUtils.showAlert(alertInfo)){
            Session.cleanUserSession();
            Session.setGuest();
            JavaFXUtils.changeScene("unmask.fxml");
        }
    }
}
