package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Player;
import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.*;
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

public class SearchPlayersScreenController {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
    @FXML
    private TextField playerUsernameTextField;
    @FXML
    private ComboBox<String> teamComboBox;
    @FXML
    private TableView<Player> playersTableView;
    @FXML
    TableColumn<Player, String> playerUsernameTableColumn;
    @FXML
    TableColumn<Player, String> playerTeamTableColumn;
    @FXML
    private Button deleteButton;

    @FXML
    private void initialize(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.GUEST.getPermission()){
            deleteButton.setStyle("-fx-text-fill: grey;");
        }

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
        teamComboBox.setItems(teams);

        playerUsernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        playerTeamTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeamName()));

        GetPlayersThread playersThread = new GetPlayersThread();
        Thread threadPlayers = new Thread(playersThread);
        threadPlayers.start();
        try {
            threadPlayers.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Player> players = playersThread.getPlayersThread();
        playersTableView.setItems(FXCollections.observableArrayList(players));
    }
    @FXML
    public void playerSearch(){
        GetPlayersThread playersThread = new GetPlayersThread();
        Thread threadPlayers = new Thread(playersThread);
        threadPlayers.start();
        try {
            threadPlayers.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Player> players = playersThread.getPlayersThread();

        String playerUsername = playerUsernameTextField.getText();
        String playerTeamName = Optional.ofNullable(teamComboBox.getValue()).orElse("");

        List<Player> filteredPlayers = players.stream()
                .filter(p -> p.getUsername().contains(playerUsername))
                .filter(p -> Optional.ofNullable(p.getTeamName())
                        .orElse("")
                        .contains(playerTeamName))
                .toList();

        playersTableView.setItems(FXCollections.observableArrayList(filteredPlayers));
    }
    @FXML
    public void deletePlayer(){
        SelectionModel<Player> selectionModel = playersTableView.getSelectionModel();
        Player selectedItem = selectionModel.getSelectedItem();
        Session instance = Session.setGuest();
        if(instance.getPrivileges() == Permission.ADMIN.getPermission()){
            if(selectedItem != null){
                AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to delete this record?", Alert.AlertType.CONFIRMATION, true);
                if(JavaFXUtils.showAlert(confirmAlert)){
                    Long id = selectedItem.getId();
                    GetPlayersThread playersThread = new GetPlayersThread();
                    Thread threadPlayers = new Thread(playersThread);
                    threadPlayers.start();
                    try {
                        threadPlayers.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    Player player = playersThread.getPlayersThread().stream().filter(a -> a.getId().equals(id)).findFirst().get();

                    DeletePlayerThread deletePlayerThread = new DeletePlayerThread(id);
                    Thread deleteThreadPlayer = new Thread(deletePlayerThread);
                    deleteThreadPlayer.start();
                    try {
                        deleteThreadPlayer.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    DeletePlayerFromFileThread deletePlayerThreadFile = new DeletePlayerFromFileThread(id);
                    Thread deleteThreadPlayerFile = new Thread(deletePlayerThreadFile);
                    deleteThreadPlayerFile.start();
                    try {
                        deleteThreadPlayerFile.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    playerSearch();

                    SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), new Player.Builder().build(), player, instance.getUserName()));
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
            if (selectedItem.getUsername().equals(instance.getUserName())) {
                AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to delete this record?", Alert.AlertType.CONFIRMATION, true);
                if(JavaFXUtils.showAlert(confirmAlert)){
                    Long id = selectedItem.getId();
                    GetPlayersThread playersThread = new GetPlayersThread();
                    Thread threadPlayers = new Thread(playersThread);
                    threadPlayers.start();
                    try {
                        threadPlayers.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    Player player = playersThread.getPlayersThread().stream().filter(a -> a.getId().equals(id)).findFirst().get();
                    DeletePlayerThread deletePlayerThread = new DeletePlayerThread(id);
                    Thread deleteThreadPlayer = new Thread(deletePlayerThread);
                    deleteThreadPlayer.start();
                    try {
                        deleteThreadPlayer.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    DeletePlayerFromFileThread deletePlayerThreadFile = new DeletePlayerFromFileThread(id);
                    Thread deleteThreadPlayerFile = new Thread(deletePlayerThreadFile);
                    deleteThreadPlayerFile.start();
                    try {
                        deleteThreadPlayerFile.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    playerSearch();

                    SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), new Player.Builder().build(), player, instance.getUserName()));
                    Thread threadChanges = new Thread(changesThread);
                    threadChanges.start();
                    try {
                        threadChanges.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                }
            }else{
                AlertRecord notCreator = new AlertRecord("Unsuccessful", "You can only delete your account", Alert.AlertType.ERROR, false);
                JavaFXUtils.showAlert(notCreator);
            }
        } else{
            AlertRecord alertInfoAdmin = new AlertRecord("Unsuccessful", "Only Admins can delete records", Alert.AlertType.INFORMATION, false);
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
}
