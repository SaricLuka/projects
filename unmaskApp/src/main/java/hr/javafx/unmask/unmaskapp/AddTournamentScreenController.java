package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Player;
import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.model.Tournament;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.AddTournamentThread;
import hr.javafx.unmask.unmaskapp.threads.GetTeamsThread;
import hr.javafx.unmask.unmaskapp.threads.GetTournamentsThread;
import hr.javafx.unmask.unmaskapp.threads.SerializeChangesListThread;
import hr.javafx.unmask.unmaskapp.utils.DatabaseUtils;
import hr.javafx.unmask.unmaskapp.utils.FileUtils;
import hr.javafx.unmask.unmaskapp.utils.JavaFXUtils;
import hr.javafx.unmask.unmaskapp.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

public class AddTournamentScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private TextField nameTextField;
    @FXML
    private ListView<String> teamsListView;

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
        teamsListView.setItems(teams);
        teamsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    @FXML
    public void tournamentAdd(){
        Optional<String> errorMsg = Optional.empty();
        String name = nameTextField.getText();
        if(name.isEmpty()){
            errorMsg = Optional.of(errorMsg.orElse("") + "\nName can not be empty");
        }

        GetTournamentsThread tournamentsThread = new GetTournamentsThread();
        Thread threadTournaments = new Thread(tournamentsThread);
        threadTournaments.start();
        try {
            threadTournaments.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        if (tournamentsThread.getTournamentsThread().stream()
                .filter(t -> Objects.nonNull(t.getName()))
                .anyMatch(t -> t.getName().equals(name))) {
            errorMsg = Optional.of(errorMsg.orElse("") + "\nName taken");
        }
        List<String> teamNames = teamsListView.getSelectionModel().getSelectedItems();
        if(teamNames.size() < 2){
            errorMsg = Optional.of(errorMsg.orElse("") + "\nAt least 2 teams have to be selected");
        }

        if(errorMsg.isEmpty()) {
            Set<Team> teams = new HashSet<>();
            for (String teamName : teamNames) {

                GetTeamsThread teamsThread = new GetTeamsThread();
                Thread threadTeams = new Thread(teamsThread);
                threadTeams.start();
                try {
                    threadTeams.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
                for (Team team : teamsThread.getTeamsThread()) {
                    if(teamName.equals(team.getName())){
                        teams.add(team);
                    }
                }
            }
            Tournament tournament = new Tournament.Builder()
                    .name(name)
                    .teams(teams)
                    .winner(new Team())
                    .status(true)
                    .build();

            AddTournamentThread addTournamentsThread = new AddTournamentThread(tournament);
            Thread addThreadTournaments = new Thread(addTournamentsThread);
            addThreadTournaments.start();
            try {
                addThreadTournaments.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }

            Session instance = Session.setGuest();
            SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), tournament, new Tournament.Builder().winner(new Team()).build(), instance.getUserName()));
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
