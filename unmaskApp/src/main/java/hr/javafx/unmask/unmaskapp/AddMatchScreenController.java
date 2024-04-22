package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.model.*;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.*;
import hr.javafx.unmask.unmaskapp.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddMatchScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private ComboBox<String> tournamentComboBox;
    @FXML
    private ComboBox<String> teamOneComboBox;
    @FXML
    private ComboBox<String> teamTwoComboBox;

    @FXML
    private void initialize() {
        GetTournamentsThread tournamentsThread = new GetTournamentsThread();
        Thread threadTournaments = new Thread(tournamentsThread);
        threadTournaments.start();
        try {
            threadTournaments.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        ObservableList<String> tournamentNames = FXCollections.observableArrayList(tournamentsThread.getTournamentsThread().stream()
                .map(t -> t.getName())
                .toList());
        tournamentComboBox.setItems(tournamentNames);
        tournamentComboBox.setValue(tournamentNames.get(0));
        selectedTournament();
    }
    @FXML
    public void matchAdd() {
        Optional<String> errorMsg = Optional.empty();
        String tournamentName = tournamentComboBox.getValue();
        String teamOneName = teamOneComboBox.getValue();
        String teamTwoName = teamTwoComboBox.getValue();
        if(teamOneName.equals(teamTwoName)){
            errorMsg = Optional.of(errorMsg.orElse("") + "\nCan not be same teams");
        }

        if(errorMsg.isEmpty()) {
            Team teamOne = null;
            Team teamTwo = null;

            GetTeamsThread teamsThread = new GetTeamsThread();
            Thread threadTeams = new Thread(teamsThread);
            threadTeams.start();
            try {
                threadTeams.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            for (Team t : teamsThread.getTeamsThread()) {
                if(t.getName().equals(teamOneName)){
                    teamOne = t;
                }
                if(t.getName().equals(teamTwoName)){
                    teamTwo = t;
                }
            }

            Tournament tournament = null;
            GetTournamentsThread tournamentsThread = new GetTournamentsThread();
            Thread threadTournaments = new Thread(tournamentsThread);
            threadTournaments.start();
            try {
                threadTournaments.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
            for (Tournament t : tournamentsThread.getTournamentsThread()) {
                if(t.getName().equals(tournamentName)){
                    tournament = t;
                    break;
                }
            }

            Match match = new Match.Builder()
                    .teamOne(teamOne)
                    .teamTwo(teamTwo)
                    .winner(new Team())
                    .scoreTeamOne(0)
                    .scoreTeamTwo(0)
                    .build();

            AddMatchThread addMatchesThread = new AddMatchThread(match, tournament.getId());
            Thread addThreadMatches = new Thread(addMatchesThread);
            addThreadMatches.start();
            try {
                addThreadMatches.join();
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
            SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), match, new Match.Builder().teamOne(new Team()).teamTwo(new Team()).winner(new Team()).build(), instance.getUserName()));
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
    @FXML
    public void selectedTournament(){
        String tournamentString = tournamentComboBox.getValue();
        GetTournamentsThread tournamentsThread = new GetTournamentsThread();
        Thread threadTournaments = new Thread(tournamentsThread);
        threadTournaments.start();
        try {
            threadTournaments.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Tournament> tournaments = tournamentsThread.getTournamentsThread();
        Tournament tournament  = tournaments.stream()
                .filter(t -> t.getName().equals(tournamentString))
                .findFirst()
                .get();

        ObservableList<String> teamNames = FXCollections.observableArrayList(tournament.getTeams().stream()
                .map(t -> t.getName())
                .sorted()
                .toList());
        teamOneComboBox.setItems(teamNames);
        teamOneComboBox.setValue(teamNames.get(0));
        teamTwoComboBox.setItems(teamNames);
        teamTwoComboBox.setValue(teamNames.get(0));
    }

}
