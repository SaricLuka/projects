package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.EditTeamThread;
import hr.javafx.unmask.unmaskapp.threads.GetTeamsThread;
import hr.javafx.unmask.unmaskapp.threads.SerializeChangesListThread;
import hr.javafx.unmask.unmaskapp.utils.DatabaseUtils;
import hr.javafx.unmask.unmaskapp.utils.FileUtils;
import hr.javafx.unmask.unmaskapp.utils.JavaFXUtils;
import hr.javafx.unmask.unmaskapp.utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditTeamScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private TextField teamNameTextField;
    @FXML
    private TextField matchesWonTextField;
    @FXML
    private TextField matchesLostTextField;
    @FXML
    private TextField tournamentsWonTextField;
    @FXML
    private TextField tournamentsPlayedTextField;
    @FXML
    private TableView<Team> teamsTableView;
    @FXML
    TableColumn<Team, String> teamCreatorTableColumn;
    @FXML
    TableColumn<Team, String> teamNameTableColumn;
    @FXML
    TableColumn<Team, String> matchesWonTableColumn;
    @FXML
    TableColumn<Team, String> matchesLostTableColumn;
    @FXML
    TableColumn<Team, String> tournamentsWonTableColumn;
    @FXML
    TableColumn<Team, String> tournamentsPlayedTableColumn;

    @FXML
    private void initialize(){
        teamCreatorTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreator().getUsername()));
        teamNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        matchesWonTableColumn.setCellValueFactory(new PropertyValueFactory<>("matchesWon"));
        matchesLostTableColumn.setCellValueFactory(new PropertyValueFactory<>("matchesLost"));
        tournamentsWonTableColumn.setCellValueFactory(new PropertyValueFactory<>("tournamentsWon"));
        tournamentsPlayedTableColumn.setCellValueFactory(new PropertyValueFactory<>("tournamentsPlayed"));

        GetTeamsThread teamsThread = new GetTeamsThread();
        Thread threadTeams = new Thread(teamsThread);
        threadTeams.start();
        try {
            threadTeams.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Team> teams = teamsThread.getTeamsThread();
        teamsTableView.setItems(FXCollections.observableArrayList(teams));
    }
    @FXML
    public void teamEdit(){
        String newTeamName = teamNameTextField.getText();
        Boolean one = true;
        Boolean two = true;
        Boolean three = true;
        Boolean four = true;
        Integer newMatchesWon = 0;
        Integer newMatchesLost= 0;
        Integer newTournamentsWon = 0;
        Integer newTournamentsPlayed = 0;
        try {
            newMatchesWon = Integer.valueOf(matchesWonTextField.getText());
        } catch (NumberFormatException e) {
            one = false;
        }
        try {
            newMatchesLost = Integer.valueOf(matchesLostTextField.getText());
        } catch (NumberFormatException e) {
            two = false;
        }
        try {
            newTournamentsWon = Integer.valueOf(tournamentsWonTextField.getText());
        } catch (NumberFormatException e) {
            three = false;
        }
        try {
            newTournamentsPlayed = Integer.valueOf(tournamentsPlayedTextField.getText());
        } catch (NumberFormatException e) {
            four = false;
        }

        SelectionModel<Team> selectionModel = teamsTableView.getSelectionModel();
        Team team = selectionModel.getSelectedItem();
        if(team != null){
            AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to edit this record?", Alert.AlertType.CONFIRMATION, true);
            if(JavaFXUtils.showAlert(confirmAlert)){
                Team newTeam = new Team(team);
                if(!(newTeamName.isEmpty())){
                    newTeam.setName(newTeamName);
                }
                if(one){
                    newTeam.setMatchesWon(newMatchesWon);
                }
                if(two){
                    newTeam.setMatchesLost(newMatchesLost);
                }
                if(three){
                    newTeam.setTournamentsWon(newTournamentsWon);
                }
                if(four){
                    newTeam.setTournamentsPlayed(newTournamentsPlayed);
                }
                EditTeamThread editTeamThread = new EditTeamThread(newTeam);
                Thread editThreadTeam = new Thread(editTeamThread);
                editThreadTeam.start();
                try {
                    editThreadTeam.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }


                Session instance = Session.setGuest();
                SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), newTeam, team, instance.getUserName()));
                Thread threadChanges = new Thread(changesThread);
                threadChanges.start();
                try {
                    threadChanges.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }

                GetTeamsThread teamsThread = new GetTeamsThread();
                Thread threadTeams = new Thread(teamsThread);
                threadTeams.start();
                try {
                    threadTeams.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
                List<Team> teams = teamsThread.getTeamsThread();
                teamsTableView.setItems(FXCollections.observableArrayList(teams));
            }
        }else{
            AlertRecord missingSelection = new AlertRecord("Unsuccessful", "A record has to be selected", Alert.AlertType.ERROR, false);
            JavaFXUtils.showAlert(missingSelection);
        }
    }
}
