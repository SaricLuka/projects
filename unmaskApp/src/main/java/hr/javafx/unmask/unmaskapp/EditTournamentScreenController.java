package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.exceptions.UnableToStatusOngoing;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Match;
import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.model.Tournament;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.EditTournamentThread;
import hr.javafx.unmask.unmaskapp.threads.GetTeamsThread;
import hr.javafx.unmask.unmaskapp.threads.GetTournamentsThread;
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
import java.util.Optional;

public class EditTournamentScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private TextField nameTextField;
    @FXML
    private ComboBox<String> teamsComboBox;
    @FXML
    private CheckBox statusCheckBox;
    @FXML
    private TableView<Tournament> tournamentsTableView;
    @FXML
    private TableColumn<Tournament, String> nameTableColumn;
    @FXML
    private TableColumn<Tournament, String> winnerTableColumn;
    @FXML
    private TableColumn<Tournament, String> ongoingTableColumn;

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

        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        winnerTableColumn.setCellValueFactory(cellData -> {
            Team winner = cellData.getValue().getWinner();
            String winnerName = (winner != null) ? winner.getName() : "NYD";
            return new SimpleStringProperty(winnerName);
        });
        ongoingTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        GetTournamentsThread tournamentsThread = new GetTournamentsThread();
        Thread threadTournaments = new Thread(tournamentsThread);
        threadTournaments.start();
        try {
            threadTournaments.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Tournament> tournaments = tournamentsThread.getTournamentsThread();
        tournamentsTableView.setItems(FXCollections.observableArrayList(tournaments));
    }
    @FXML
    public void tournamentEdit() {
        String newName = nameTextField.getText();
        String newWinnerName = teamsComboBox.getValue();
        Boolean status = statusCheckBox.isSelected();
        SelectionModel<Tournament> selectionModel = tournamentsTableView.getSelectionModel();
        Tournament tournament = selectionModel.getSelectedItem();
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
        if(tournament != null){
            AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to edit this record?", Alert.AlertType.CONFIRMATION, true);
            if(JavaFXUtils.showAlert(confirmAlert)){
                Tournament newTournament = new Tournament(tournament);
                if(!(newName.isEmpty())){
                    newTournament.setName(newName);
                }
                newTournament.setWinner(newWinner);
                try{
                    newTournament.setStatus(newTournament.statusEnd(!status));
                }catch(UnableToStatusOngoing e){
                    logger.info(e.getMessage());
                }
                EditTournamentThread editTournamentThread = new EditTournamentThread(newTournament);
                Thread editThreadTournament = new Thread(editTournamentThread);
                editThreadTournament.start();
                try {
                    editThreadTournament.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }

                Session instance = Session.setGuest();
                tournament.setWinner(new Team());
                SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), newTournament, tournament, instance.getUserName()));
                Thread threadChanges = new Thread(changesThread);
                threadChanges.start();
                try {
                    threadChanges.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }

                GetTournamentsThread tournamentsThread = new GetTournamentsThread();
                Thread threadTournaments = new Thread(tournamentsThread);
                threadTournaments.start();
                try {
                    threadTournaments.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
                List<Tournament> tournaments = tournamentsThread.getTournamentsThread();
                tournamentsTableView.setItems(FXCollections.observableArrayList(tournaments));
            }
        }else{
            AlertRecord missingSelection = new AlertRecord("Unsuccessful", "A record has to be selected", Alert.AlertType.ERROR, false);
            JavaFXUtils.showAlert(missingSelection);
        }
    }
}
