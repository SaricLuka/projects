package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Match;
import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.DeleteMatchThread;
import hr.javafx.unmask.unmaskapp.threads.GetMatchesThread;
import hr.javafx.unmask.unmaskapp.threads.SerializeChangesListThread;
import hr.javafx.unmask.unmaskapp.threads.UpdateMatchesThread;
import hr.javafx.unmask.unmaskapp.utils.DatabaseUtils;
import hr.javafx.unmask.unmaskapp.utils.FileUtils;
import hr.javafx.unmask.unmaskapp.utils.JavaFXUtils;
import hr.javafx.unmask.unmaskapp.utils.Session;
import javafx.application.Platform;
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

public class SearchMatchesScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private TextField winnerTextField;
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
    private Button deleteButton;

    @FXML
    private void initialize(){
        UpdateMatchesThread updateMatchesThread = new UpdateMatchesThread(this);
        updateMatchesThread.start();

        Session session = Session.setGuest();
        if(session.getPrivileges() != Permission.ADMIN.getPermission()){
            deleteButton.setStyle("-fx-text-fill: grey;");
        }
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
    public void matchSearch(){
        GetMatchesThread matchesThread = new GetMatchesThread();
        Thread threadMatches = new Thread(matchesThread);
        threadMatches.start();
        try {
            threadMatches.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Match> matches = matchesThread.getMatchesThread();
        String winner = winnerTextField.getText();
        Boolean status = statusCheckBox.isSelected();

        List<Match> filteredMatches = matches.stream()
                .filter(m -> Optional.ofNullable(m.getWinner())
                        .map(Team::getName)
                        .orElse("")
                        .contains(winner))
                .filter(m -> m.getStatus().equals(status))
                .toList();

        matchesTableView.setItems(FXCollections.observableArrayList(filteredMatches));
    }

    @FXML
    public void deleteMatch(){
        SelectionModel<Match> selectionModel = matchesTableView.getSelectionModel();
        Match selectedItem = selectionModel.getSelectedItem();
        Session instance = Session.setGuest();
        if(instance.getPrivileges() == Permission.ADMIN.getPermission()){
            if(selectedItem != null){
                AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to delete this record?", Alert.AlertType.CONFIRMATION, true);
                if(JavaFXUtils.showAlert(confirmAlert)){
                    Long id = selectedItem.getId();

                    GetMatchesThread matchesThread = new GetMatchesThread();
                    Thread threadMatches = new Thread(matchesThread);
                    threadMatches.start();
                    try {
                        threadMatches.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    Match match = matchesThread.getMatchesThread().stream()
                            .filter(a -> a.getId().equals(id))
                            .findFirst()
                            .get();
                    if(match.getWinner() == null) {
                        match.setWinner(new Team());
                    }
                    DeleteMatchThread deleteMatchThread = new DeleteMatchThread(id);
                    Thread deleteThreadMatch = new Thread(deleteMatchThread);
                    deleteThreadMatch.start();
                    try {
                        deleteThreadMatch.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    matchSearch();

                    SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), new Match.Builder().teamOne(new Team()).teamTwo(new Team()).winner(new Team()).build(), match, instance.getUserName()));
                    Thread threadChanges = new Thread(changesThread);
                    threadChanges.start();
                    try {
                        threadChanges.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                }
            }else{
                AlertRecord missingSelection = new AlertRecord("Unsuccessful", "A record has to be seleted", Alert.AlertType.ERROR, false);
                JavaFXUtils.showAlert(missingSelection);
            }
        }else{
            AlertRecord alertInfoAdmin = new AlertRecord("Unsuccessful", "Only Admins can delete records", Alert.AlertType.INFORMATION, false);
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
}
