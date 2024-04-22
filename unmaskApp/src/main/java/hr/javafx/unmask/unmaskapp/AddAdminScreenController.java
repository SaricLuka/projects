package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.model.Admin;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.AddAdminsToFileThread;
import hr.javafx.unmask.unmaskapp.threads.GetAdminsFromFileThread;
import hr.javafx.unmask.unmaskapp.threads.SerializeChangesListThread;
import hr.javafx.unmask.unmaskapp.utils.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AddAdminScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField plainPasswordTextField;

    @FXML
    private void initialize() {}
    @FXML
    public void adminAdd() {
        Optional<String> errorMsg = Optional.empty();
        String username = usernameTextField.getText();
        if(username.isEmpty()){
            errorMsg = Optional.of(errorMsg.orElse("") + "\nUsername can not be empty");
        }
        GetAdminsFromFileThread adminsThread = new GetAdminsFromFileThread();
        Thread threadAdmins = new Thread(adminsThread);
        threadAdmins.start();
        try {
            threadAdmins.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        if(adminsThread.getAdminsFromFileThread().stream()
                .anyMatch(person -> person.getUsername().equals(username))){
            errorMsg = Optional.of(errorMsg.orElse("") + "\nUsername taken");
        }
        String plainPassword = plainPasswordTextField.getText();
        if(plainPassword.isEmpty()){
            errorMsg = Optional.of(errorMsg.orElse("") + "\nPassword can not be empty");
        }

        if(errorMsg.isEmpty()) {
            Thread threadAdmins2 = new Thread(adminsThread);
            threadAdmins2.start();
            try {
                threadAdmins2.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            Long id = adminsThread.getAdminsFromFileThread().getLast().getId() + 1L;
            Admin admin = new Admin(id, username, HashUtils.hashPassword(plainPassword));

            Thread threadAdmins3 = new Thread(adminsThread);
            threadAdmins3.start();
            try {
                threadAdmins3.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            List<Admin> admins = adminsThread.getAdminsFromFileThread();
            admins.add(admin);

            AddAdminsToFileThread addAdminsThread = new AddAdminsToFileThread(admins);
            Thread addThreadAdmins = new Thread(addAdminsThread);
            addThreadAdmins.start();
            try {
                addThreadAdmins.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }


            Session instance = Session.setGuest();
            SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), admin, new Admin(), instance.getUserName()));
            Thread threadChanges = new Thread(changesThread);
            threadChanges.start();
            try {
                threadChanges.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }

            AlertRecord alertInfo = new AlertRecord("Successful", "Saving successful", Alert.AlertType.INFORMATION, false);
            JavaFXUtils.showAlert(alertInfo);
        }
        else{
            AlertRecord alertInfo = new AlertRecord("Unsuccessful", errorMsg.get(), Alert.AlertType.ERROR, false);
            JavaFXUtils.showAlert(alertInfo);
        }
    }
}
