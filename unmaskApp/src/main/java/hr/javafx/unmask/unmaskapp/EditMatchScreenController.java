package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.exceptions.UnableToStatusOngoing;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Match;
import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.EditMatchThread;
import hr.javafx.unmask.unmaskapp.threads.GetMatchesThread;
import hr.javafx.unmask.unmaskapp.threads.GetTeamsThread;
import hr.javafx.unmask.unmaskapp.threads.SerializeChangesListThread;
import hr.javafx.unmask.unmaskapp.utils.DatabaseUtils;
import hr.javafx.unmask.unmaskapp.utils.FileUtils;
import hr.javafx.unmask.unmaskapp.utils.JavaFXUtils;
import hr.javafx.unmask.unmaskapp.utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EditMatchScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private TextField teamOneScoreTextField;
    @FXML
    private TextField teamTwoScoreTextField;
    @FXML
    private ComboBox<String> teamsComboBox;
    @FXML
    private CheckBox statusCheckBox;
    @FXML
    private TableView<Match> matchesTableView;
    @FXML
    private TableColumn<Match, String> teamOneTableColumn;
    @FXML
    private TableColumn<Match, String> teamTwoTableColumn;
    @FXML
    private TableColumn<Match, String> teamOneScoreTableColumn;
    @FXML
    private TableColumn<Match, String> teamTwoScoreTableColumn;
    @FXML
    private TableColumn<Match, String> winnerTableColumn;
    @FXML
    private TableColumn<Match, String> ongoingTableColumn;

    @FXML
    private void initialize(){
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
        teams.add("");
        teamsComboBox.setItems(teams);
        teamsComboBox.setValue(teams.get(0));

        teamOneTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeamOne().getName()));
        teamTwoTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeamTwo().getName()));
        teamOneScoreTableColumn.setCellValueFactory(new PropertyValueFactory<>("scoreTeamOne"));
        teamTwoScoreTableColumn.setCellValueFactory(new PropertyValueFactory<>("scoreTeamTwo"));
        winnerTableColumn.setCellValueFactory(cellData -> {
            Team winner = cellData.getValue().getWinner();
            String winnerName = (winner != null) ? winner.getName() : "NYD";
            return new SimpleStringProperty(winnerName);
        });
        ongoingTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        GetMatchesThread matchesThread = new GetMatchesThread();
        Thread threadMatches = new Thread(matchesThread);
        threadMatches.start();
        try {
            threadMatches.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Match> matches = matchesThread.getMatchesThread();
        matchesTableView.setItems(FXCollections.observableArrayList(matches));
    }
    @FXML
    public void matchEdit(){
        Boolean one = true;
        Boolean two = true;
        Integer newTeamOneScore = 0;
        Integer newTeamTwoScore= 0;
        try {
            newTeamOneScore = Integer.valueOf(teamOneScoreTextField.getText());
        } catch (NumberFormatException e) {
            one = false;
        }
        try {
            newTeamTwoScore = Integer.valueOf(teamTwoScoreTextField.getText());
        } catch (NumberFormatException e) {
            two = false;
        }
        String newWinnerName = teamsComboBox.getValue();
        Boolean status = statusCheckBox.isSelected();
        Team newWinner = null;
        GetTeamsThread teamsThread = new GetTeamsThread();
        Thread threadTeams = new Thread(teamsThread);
        threadTeams.start();
        try {
            threadTeams.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        for (Team t : teamsThread.getTeamsThread()) {
            if(t.getName().equals(newWinnerName)){
                newWinner = t;
                break;
            }
        }
        if(newWinnerName.isEmpty()){
            newWinner = new Team();
        }
        SelectionModel<Match> selectionModel = matchesTableView.getSelectionModel();
        Match match = selectionModel.getSelectedItem();
        if(match != null){
            AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to edit this record?", Alert.AlertType.CONFIRMATION, true);
            if(JavaFXUtils.showAlert(confirmAlert)){
                Match newMatch = new Match(match);
                if(one){
                    newMatch.setScoreTeamOne(newTeamOneScore);
                }
                if(two){
                    newMatch.setScoreTeamTwo(newTeamTwoScore);
                }
                newMatch.setWinner(newWinner);
                try{
                    newMatch.setStatus(newMatch.statusEnd(!status));
                }catch(UnableToStatusOngoing e){
                    logger.info(e.getMessage());
                }
                EditMatchThread editMatchThread = new EditMatchThread(newMatch);
                Thread editThreadMatch = new Thread(editMatchThread);
                editThreadMatch.start();
                try {
                    editThreadMatch.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }


                Session instance = Session.setGuest();
                match.setWinner(new Team());
                SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), newMatch, match, instance.getUserName()));
                Thread threadChanges = new Thread(changesThread);
                threadChanges.start();
                try {
                    threadChanges.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }

                GetMatchesThread matchesThread = new GetMatchesThread();
                Thread threadMatches = new Thread(matchesThread);
                threadMatches.start();
                try {
                    threadMatches.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
                List<Match> matches = matchesThread.getMatchesThread();
                matchesTableView.setItems(FXCollections.observableArrayList(matches));
            }
        }else{
            AlertRecord missingSelection = new AlertRecord("Unsuccessful", "A record has to be selected", Alert.AlertType.ERROR, false);
            JavaFXUtils.showAlert(missingSelection);
        }
    }
}
