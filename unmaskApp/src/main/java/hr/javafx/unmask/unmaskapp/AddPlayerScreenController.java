package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Player;
import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.*;
import hr.javafx.unmask.unmaskapp.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddPlayerScreenController {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField plainPasswordTextField;
    @FXML
    private ComboBox<String> teamsComboBox;

    @FXML
    private void initialize() {
        GetTeamsThread teamsThread = new GetTeamsThread();
        Thread threadTeams = new Thread(teamsThread);
        threadTeams.start();
        try {
            threadTeams.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        ObservableList<String> teams = FXCollections.observableArrayList(teamsThread.getTeamsThread().stream()
                .map(team -> team.getName())
                .toList());
        teamsComboBox.setItems(teams);
        teamsComboBox.setValue(teams.get(0));
    }
    @FXML
    public void playerAdd(){
        Optional<String> errorMsg = Optional.empty();
        String username = usernameTextField.getText();
        if(username.isEmpty()){
            errorMsg = Optional.of(errorMsg.orElse("") + "\nUsername can not be empty");
        }
        GetPlayersFromFileThread playersThreadFile = new GetPlayersFromFileThread();
        Thread threadPlayersFile = new Thread(playersThreadFile);
        threadPlayersFile.start();
        try {
            threadPlayersFile.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        if(playersThreadFile.getPlayersFromFileThread().stream().anyMatch(person -> person.getUsername().equals(username))){
            errorMsg = Optional.of(errorMsg.orElse("") + "\nUsername taken");
        }
        String plainPassword = plainPasswordTextField.getText();
        if(plainPassword.isEmpty()){
            errorMsg = Optional.of(errorMsg.orElse("") + "\nPassword can not be empty");
        }
        String teamName = teamsComboBox.getValue();

        if(errorMsg.isEmpty()) {
            Team team = null;
            GetTeamsThread teamsThread = new GetTeamsThread();
            Thread threadTeams = new Thread(teamsThread);
            threadTeams.start();
            try {
                threadTeams.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            for (Team t : teamsThread.getTeamsThread()) {
                if(t.getName().equals(teamName)){
                    team = t;
                    break;
                }
            }
            Thread threadPlayersFile2 = new Thread(playersThreadFile);
            threadPlayersFile2.start();
            try {
                threadPlayersFile2.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            Long id = playersThreadFile.getPlayersFromFileThread().getLast().getId() + 1L;
            Player player = new Player.Builder()
                    .id(id)
                    .username(username)
                    .hashedPassword(HashUtils.hashPassword(plainPassword))
                    .teamName(team.getName())
                    .build();

            AddPlayerThread addPlayersThread = new AddPlayerThread(player, team);
            Thread addThreadPlayers = new Thread(addPlayersThread);
            addThreadPlayers.start();
            try {
                addThreadPlayers.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }

            GetPlayersThread playersThread = new GetPlayersThread();
            Thread threadPlayers = new Thread(playersThread);
            threadPlayers.start();
            try {
                threadPlayers.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            List<Player> players = playersThread.getPlayersThread();

            AddPlayersToFileThread addPlayersThreadFile = new AddPlayersToFileThread(players);
            Thread addThreadPlayersFile = new Thread(addPlayersThreadFile);
            addThreadPlayersFile.start();
            try {
                addThreadPlayersFile.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }


            Session instance = Session.setGuest();
            SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), player, new Player.Builder().build(), instance.getUserName()));
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
