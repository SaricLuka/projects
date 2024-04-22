package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.DeleteTeamThread;
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

public class SearchTeamsScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private TextField creatorTextField;
    @FXML
    private TextField teamNameTextField;
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
    private Button deleteButton;

    @FXML
    private void initialize(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.GUEST.getPermission()){
            deleteButton.setStyle("-fx-text-fill: grey;");
        }

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
    public void teamSearch(){
        GetTeamsThread teamsThread = new GetTeamsThread();
        Thread threadTeams = new Thread(teamsThread);
        threadTeams.start();
        try {
            threadTeams.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Team> teams = teamsThread.getTeamsThread();

        String creator = creatorTextField.getText();
        String name = teamNameTextField.getText();

        List<Team> filteredTeams = teams.stream()
                .filter(p -> p.getCreator().getUsername().contains(creator))
                .filter(p -> p.getName().contains(name))
                .toList();

        teamsTableView.setItems(FXCollections.observableArrayList(filteredTeams));
    }

    @FXML
    public void deleteTeam(){
        SelectionModel<Team> selectionModel = teamsTableView.getSelectionModel();
        Team selectedItem = selectionModel.getSelectedItem();
        Session instance = Session.setGuest();
        if(instance.getPrivileges() == Permission.ADMIN.getPermission()){
            if(selectedItem != null){
                AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to delete this record?", Alert.AlertType.CONFIRMATION, true);
                if(JavaFXUtils.showAlert(confirmAlert)){
                    Long id = selectedItem.getId();

                    GetTeamsThread teamsThread = new GetTeamsThread();
                    Thread threadTeams = new Thread(teamsThread);
                    threadTeams.start();
                    try {
                        threadTeams.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    Team team = teamsThread.getTeamsThread().stream().filter(a -> a.getId().equals(id)).findFirst().get();

                    DeleteTeamThread deleteTeamThread = new DeleteTeamThread(id);
                    Thread deleteThreadTeam = new Thread(deleteTeamThread);
                    deleteThreadTeam.start();
                    try {
                        deleteThreadTeam.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    teamSearch();

                    SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), new Team(), team, instance.getUserName()));
                    Thread threadChanges = new Thread(changesThread);
                    threadChanges.start();
                    try {
                        threadChanges.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                }
            }else{
                AlertRecord missingSelection = new AlertRecord("Unsuccessful", "A record has to be selected", Alert.AlertType.ERROR, false);
                JavaFXUtils.showAlert(missingSelection);
            }
        } else if(selectedItem != null) {
            if (selectedItem.getCreator().getUsername().equals(instance.getUserName())) {
                AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to delete this record?", Alert.AlertType.CONFIRMATION, true);
                if(JavaFXUtils.showAlert(confirmAlert)){
                    Long id = selectedItem.getId();


                    GetTeamsThread teamsThread = new GetTeamsThread();
                    Thread threadTeams = new Thread(teamsThread);
                    threadTeams.start();
                    try {
                        threadTeams.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    Team team = teamsThread.getTeamsThread().stream().filter(a -> a.getId().equals(id)).findFirst().get();

                    DeleteTeamThread deleteTeamThread = new DeleteTeamThread(id);
                    Thread deleteThreadTeam = new Thread(deleteTeamThread);
                    deleteThreadTeam.start();
                    try {
                        deleteThreadTeam.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }

                    teamSearch();

                    SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), new Team(), team, instance.getUserName()));
                    Thread threadChanges = new Thread(changesThread);
                    threadChanges.start();
                    try {
                        threadChanges.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                }
            }else{
                AlertRecord notCreator = new AlertRecord("Unsuccessful", "You can only delete teams that you created", Alert.AlertType.ERROR, false);
                JavaFXUtils.showAlert(notCreator);
            }
        } else{
            AlertRecord alertInfoAdmin = new AlertRecord("Unsuccessful", "Only Admins can delete records", Alert.AlertType.INFORMATION, false);
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
}
