package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Player;
import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.AddTeamThread;
import hr.javafx.unmask.unmaskapp.threads.GetPlayersThread;
import hr.javafx.unmask.unmaskapp.threads.GetTeamsThread;
import hr.javafx.unmask.unmaskapp.threads.SerializeChangesListThread;
import hr.javafx.unmask.unmaskapp.utils.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddTeamScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private TextField nameTextField;

    @FXML
    private void initialize() {}
    @FXML
    public void teamAdd() {
        Optional<String> errorMsg = Optional.empty();
        String name = nameTextField.getText();
        if(name.isEmpty()){
            errorMsg = Optional.of(errorMsg.orElse("") + "\nName can not be empty");
        }
        GetTeamsThread teamsThread = new GetTeamsThread();
        Thread threadTeams = new Thread(teamsThread);
        threadTeams.start();
        try {
            threadTeams.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        if(teamsThread.getTeamsThread().stream()
                .anyMatch(t -> t.getName().equals(name))){
            errorMsg = Optional.of(errorMsg.orElse("") + "\nName taken");
        }

        if(errorMsg.isEmpty()) {
            Session instance = Session.setGuest();
            GetPlayersThread playersThread = new GetPlayersThread();
            Thread threadPlayers = new Thread(playersThread);
            threadPlayers.start();
            try {
                threadPlayers.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            Player player = playersThread.getPlayersThread().stream()
                    .filter(p -> p.getUsername().equals(instance.getUserName()))
                    .findFirst()
                    .get();
            Team team = new Team(null, player, name, null, 0,0,0,0);

            AddTeamThread addTeamsThread = new AddTeamThread(team);
            Thread addThreadTeam = new Thread(addTeamsThread);
            addThreadTeam.start();
            try {
                addThreadTeam.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }

            SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), team, new Team(), instance.getUserName()));
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
