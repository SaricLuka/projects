package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.exceptions.HowDidWeGetHereException;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.utils.JavaFXUtils;
import hr.javafx.unmask.unmaskapp.utils.Session;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuBarController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    AlertRecord alertInfo = new AlertRecord("Unsuccessful", "Can not log in if already logged in", Alert.AlertType.INFORMATION, false);
    AlertRecord alertInfoError = new AlertRecord("Unsuccessful", "Please restart the app.", Alert.AlertType.INFORMATION, false);
    AlertRecord alertInfoAdmin = new AlertRecord("Unsuccessful", "Only Admin can access", Alert.AlertType.INFORMATION, false);
    AlertRecord alertInfoPlayer = new AlertRecord("Unsuccessful", "Only Player can access", Alert.AlertType.INFORMATION, false);
    AlertRecord alertInfoGuest = new AlertRecord("Unsuccessful", "Only Guests can access", Alert.AlertType.INFORMATION, false);
    @FXML
    public MenuItem logIn;
    @FXML
    public MenuItem logOut;
    @FXML
    public MenuItem searchAdmin;
    @FXML
    public MenuItem addPlayer;
    @FXML
    public MenuItem addTeam;
    @FXML
    public MenuItem addMatch;
    @FXML
    public MenuItem addTournament;
    @FXML
    public MenuItem addAdmin;
    @FXML
    public MenuItem editPlayer;
    @FXML
    public MenuItem editTeam;
    @FXML
    public MenuItem editMatch;
    @FXML
    public MenuItem editTournament;
    @FXML
    public MenuItem editAdmin;
    @FXML
    public MenuItem showChanges;

    public void initialize(){
        Session session = Session.setGuest();
        //moze samo guest
        if(session.getPrivileges() < Permission.GUEST.getPermission()) {
            logIn.setStyle("-fx-text-fill: gray;");
            addPlayer.setStyle("-fx-text-fill: gray;");
        } else{
            logIn.setStyle("-fx-text-fill: black;");
            addPlayer.setStyle("-fx-text-fill: black;");
        }
        //ne moze guest
        if(session.getPrivileges() == Permission.GUEST.getPermission()) {
            logOut.setStyle("-fx-text-fill: gray;");
        } else{
            logOut.setStyle("-fx-text-fill: black;");
        }
        //moze samo admin
        if(session.getPrivileges() > Permission.ADMIN.getPermission()) {
            searchAdmin.setStyle("-fx-text-fill: gray;");
            showChanges.setStyle("-fx-text-fill: gray;");
            addMatch.setStyle("-fx-text-fill: gray;");
            addTournament.setStyle("-fx-text-fill: gray;");
            addAdmin.setStyle("-fx-text-fill: gray;");
            editAdmin.setStyle("-fx-text-fill: gray;");
            editMatch.setStyle("-fx-text-fill: gray;");
            editPlayer.setStyle("-fx-text-fill: gray;");
            editTeam.setStyle("-fx-text-fill: gray;");
            editTournament.setStyle("-fx-text-fill: gray;");
        } else{
            searchAdmin.setStyle("-fx-text-fill: black;");
            showChanges.setStyle("-fx-text-fill: black;");
            addMatch.setStyle("-fx-text-fill: black;");
            addTournament.setStyle("-fx-text-fill: black;");
            addAdmin.setStyle("-fx-text-fill: black;");
            editAdmin.setStyle("-fx-text-fill: black;");
            editTournament.setStyle("-fx-text-fill: black;");
            editPlayer.setStyle("-fx-text-fill: black;");
            editMatch.setStyle("-fx-text-fill: black;");
            editTeam.setStyle("-fx-text-fill: black;");
        }
        //moze samo player
        if(session.getPrivileges() < Permission.PLAYER.getPermission() || session.getPrivileges() > Permission.PLAYER.getPermission()) {
            addTeam.setStyle("-fx-text-fill: gray;");
        } else{
            addTeam.setStyle("-fx-text-fill: black;");
        }
    }

    @FXML
    public void showLogInScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.GUEST.getPermission()) {
            JavaFXUtils.changeScene("logInScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfo);
        }
    }
    @FXML
    public void showLogOutScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() < Permission.GUEST.getPermission()) {
            JavaFXUtils.changeScene("logOutScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfo);
        }
    }
    @FXML
    public void showSearchPlayersScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() <= Permission.GUEST.getPermission()) {
            JavaFXUtils.changeScene("searchPlayersScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoError);
            String message = "This shouldn't be possible";
            logger.info(message);
            throw new HowDidWeGetHereException(message);
        }
    }
    @FXML
    public void showSearchTeamsScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() <= Permission.GUEST.getPermission()) {
            JavaFXUtils.changeScene("searchTeamsScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoError);
            String message = "This shouldn't be possible";
            logger.info(message);
            throw new HowDidWeGetHereException(message);
        }
    }
    @FXML
    public void showSearchMatchesScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() <= Permission.GUEST.getPermission()) {
            JavaFXUtils.changeScene("searchMatchesScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoError);
            String message = "This shouldn't be possible";
            logger.info(message);
            throw new HowDidWeGetHereException(message);
        }
    }
    @FXML
    public void showSearchTournamentsScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() <= Permission.GUEST.getPermission()) {
            JavaFXUtils.changeScene("searchTournamentsScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoError);
            String message = "This shouldn't be possible";
            logger.info(message);
            throw new HowDidWeGetHereException(message);
        }
    }
    @FXML
    public void showSearchAdminsScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.ADMIN.getPermission()) {
            JavaFXUtils.changeScene("searchAdminsScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
    @FXML
    public void showAddPlayerScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.GUEST.getPermission()) {
            JavaFXUtils.changeScene("addPlayerScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoGuest);
        }
    }
    @FXML
    public void showAddTeamScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.PLAYER.getPermission()) {
            JavaFXUtils.changeScene("addTeamScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoPlayer);
        }
    }
    @FXML
    public void showAddMatchScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.ADMIN.getPermission()) {
            JavaFXUtils.changeScene("addMatchScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
    @FXML
    public void showAddTournamentScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.ADMIN.getPermission()) {
            JavaFXUtils.changeScene("addTournamentScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
    @FXML
    public void showAddAdminScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.ADMIN.getPermission()) {
            JavaFXUtils.changeScene("addAdminScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
    @FXML
    public void showEditPlayerScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.ADMIN.getPermission()) {
            JavaFXUtils.changeScene("editPlayerScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
    @FXML
    public void showEditMatchScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.ADMIN.getPermission()) {
            JavaFXUtils.changeScene("editMatchScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
    @FXML
    public void showEditTeamScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.ADMIN.getPermission()) {
            JavaFXUtils.changeScene("editTeamScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
    @FXML
    public void showEditTournamentScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.ADMIN.getPermission()) {
            JavaFXUtils.changeScene("editTournamentScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
    @FXML
    public void showEditAdminScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.ADMIN.getPermission()) {
            JavaFXUtils.changeScene("editAdminScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
    @FXML
    public void showChangesScreen(){
        Session session = Session.setGuest();
        if(session.getPrivileges() == Permission.ADMIN.getPermission()) {
            JavaFXUtils.changeScene("showChangesScreen.fxml");
        }else{
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
}
