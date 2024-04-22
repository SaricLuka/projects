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

public class EditPlayerScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
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
    public void playerEdit(){
        String newUsername = playerUsernameTextField.getText();
        String newTeam = teamComboBox.getValue();
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
            if(t.getName().equals(newTeam)){
                team = t;
                break;
            }
        }
        SelectionModel<Player> selectionModel = playersTableView.getSelectionModel();
        Player player = selectionModel.getSelectedItem();
        if(player != null){
            AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to edit this record?", Alert.AlertType.CONFIRMATION, true);
            if(JavaFXUtils.showAlert(confirmAlert)){
                Player newPlayer = new Player(player);
                if(!(newUsername.isEmpty())){
                    newPlayer.setUsername(newUsername);
                }
                EditPlayerThread editPlayerThread = new EditPlayerThread(newPlayer, team);
                Thread editThreadPlayer = new Thread(editPlayerThread);
                editThreadPlayer.start();
                try {
                    editThreadPlayer.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
                EditPlayerFromFileThread editPlayerThreadFile = new EditPlayerFromFileThread(newPlayer);
                Thread editThreadPlayerFile = new Thread(editPlayerThreadFile);
                editThreadPlayerFile.start();
                try {
                    editThreadPlayerFile.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }


                Session instance = Session.setGuest();
                SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), newPlayer, player, instance.getUserName()));
                Thread threadChanges = new Thread(changesThread);
                threadChanges.start();
                try {
                    threadChanges.join();
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
                playersTableView.setItems(FXCollections.observableArrayList(players));
            }
        }else{
            AlertRecord missingSelection = new AlertRecord("Unsuccessful", "A record has to be selected", Alert.AlertType.ERROR, false);
            JavaFXUtils.showAlert(missingSelection);
        }
    }
}
