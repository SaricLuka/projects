package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.model.Tournament;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.DeleteTournamentThread;
import hr.javafx.unmask.unmaskapp.threads.GetTournamentsThread;
import hr.javafx.unmask.unmaskapp.threads.SerializeChangesListThread;
import hr.javafx.unmask.unmaskapp.threads.UpdateTournamentsThread;
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

public class SearchTournamentsScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField winnerTextField;
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
    private Button deleteButton;

    @FXML
    private void initialize(){
        UpdateTournamentsThread updateTournamentsThread = new UpdateTournamentsThread(this);
        updateTournamentsThread.start();

        Session session = Session.setGuest();
        if(session.getPrivileges() != Permission.ADMIN.getPermission()){
            deleteButton.setStyle("-fx-text-fill: grey;");
        }
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
    public void tournamentSearch(){
        GetTournamentsThread tournamentsThread = new GetTournamentsThread();
        Thread threadTournaments = new Thread(tournamentsThread);
        threadTournaments.start();
        try {
            threadTournaments.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Tournament> tournaments = tournamentsThread.getTournamentsThread();
        String name = nameTextField.getText();
        String winner = winnerTextField.getText();
        Boolean status = statusCheckBox.isSelected();

        List<Tournament> filteredTournaments = tournaments.stream()
                .filter(t -> t.getName().contains(name))
                .filter(t -> Optional.ofNullable(t.getWinner())
                        .map(Team::getName)
                        .orElse("")
                        .contains(winner))
                .filter(t -> t.getStatus().equals(status))
                .toList();

        tournamentsTableView.setItems(FXCollections.observableArrayList(filteredTournaments));
    }
    @FXML
    public void deleteTournament(){
        SelectionModel<Tournament> selectionModel = tournamentsTableView.getSelectionModel();
        Tournament selectedItem = selectionModel.getSelectedItem();
        Session instance = Session.setGuest();
        if(instance.getPrivileges() == Permission.ADMIN.getPermission()){
            if(selectedItem != null){
                AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to delete this record?", Alert.AlertType.CONFIRMATION, true);
                if(JavaFXUtils.showAlert(confirmAlert)){
                    Long id = selectedItem.getId();

                    GetTournamentsThread tournamentsThread = new GetTournamentsThread();
                    Thread threadTournaments = new Thread(tournamentsThread);
                    threadTournaments.start();
                    try {
                        threadTournaments.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    Tournament tournament = tournamentsThread.getTournamentsThread().stream().filter(a -> a.getId().equals(id)).findFirst().get();
                    if(tournament.getWinner() == null) {
                        tournament.setWinner(new Team());
                    }
                    DeleteTournamentThread deleteTournamentThread = new DeleteTournamentThread(id);
                    Thread deleteThreadTournament = new Thread(deleteTournamentThread);
                    deleteThreadTournament.start();
                    try {
                        deleteThreadTournament.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    tournamentSearch();

                    SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), new Tournament.Builder().winner(new Team()).build(), tournament, instance.getUserName()));
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
