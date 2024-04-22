package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.exceptions.LogInException;
import hr.javafx.unmask.unmaskapp.model.Person;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.GetPeopleFromFilesThread;
import hr.javafx.unmask.unmaskapp.utils.FileUtils;
import hr.javafx.unmask.unmaskapp.utils.HashUtils;
import hr.javafx.unmask.unmaskapp.utils.JavaFXUtils;
import hr.javafx.unmask.unmaskapp.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogInScreenController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;

    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);

    public void logIn(){
        String username = usernameTextField.getText();
        String plainPassword = passwordPasswordField.getText();

        GetPeopleFromFilesThread peopleThread = new GetPeopleFromFilesThread();
        Thread threadPeople = new Thread(peopleThread);
        threadPeople.start();
        try {
            threadPeople.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Person> people = peopleThread.getPeopleFromFilesThread();
        List<Person> filteredPeople = people.stream()
                .filter(person -> person.getUsername().equals(username))
                .toList();
        try{
            createSession(filteredPeople, plainPassword);
            AlertRecord alertInfo = new AlertRecord("Success", "Logged in Successfully!", Alert.AlertType.INFORMATION,false);
            JavaFXUtils.showAlert(alertInfo);
            JavaFXUtils.changeScene("unmask.fxml");
        }catch(LogInException e){
            logger.info(e.getMessage());
            AlertRecord alertInfo = new AlertRecord("Unsuccessful", e.getMessage(), Alert.AlertType.INFORMATION, false);
            JavaFXUtils.showAlert(alertInfo);
        }
    }
    private void createSession(List<Person> filteredPeople, String plainPassword) throws LogInException{
        if(filteredPeople.isEmpty()){
            throw new LogInException();
        }

        Person person = filteredPeople.get(0);
        if (HashUtils.verifyPassword(plainPassword, person.getHashedPassword())) {
            Session.cleanUserSession();
            Session.getInstance(person.getUsername(), Permission.valueOf(person.getClass().getSimpleName().toUpperCase()).getPermission());
        }else{
            throw new LogInException();
        }
    }
}
