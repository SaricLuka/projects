package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.model.Admin;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.DeleteAdminFromFileThread;
import hr.javafx.unmask.unmaskapp.threads.GetAdminsFromFileThread;
import hr.javafx.unmask.unmaskapp.threads.SerializeChangesListThread;
import hr.javafx.unmask.unmaskapp.utils.FileUtils;
import hr.javafx.unmask.unmaskapp.utils.JavaFXUtils;
import hr.javafx.unmask.unmaskapp.utils.Session;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SearchAdminsScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private TextField usernameTextField;
    @FXML
    private TableView<Admin> adminsTableView;
    @FXML
    TableColumn<Admin, String> adminUsernameTableColumn;
    @FXML
    private Button deleteButton;

    @FXML
    private void initialize(){
        Session session = Session.setGuest();
        if(session.getPrivileges() != Permission.ADMIN.getPermission()){
            deleteButton.setStyle("-fx-text-fill: grey;");
        }
        adminUsernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        GetAdminsFromFileThread adminsThread = new GetAdminsFromFileThread();
        Thread threadAdmins = new Thread(adminsThread);
        threadAdmins.start();
        try {
            threadAdmins.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Admin> admins = adminsThread.getAdminsFromFileThread();
        adminsTableView.setItems(FXCollections.observableArrayList(admins));
    }
    @FXML
    public void adminSearch(){
        GetAdminsFromFileThread adminsThread = new GetAdminsFromFileThread();
        Thread threadAdmins = new Thread(adminsThread);
        threadAdmins.start();
        try {
            threadAdmins.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Admin> admins = adminsThread.getAdminsFromFileThread();

        String adminUsername = usernameTextField.getText();

        List<Admin> filteredPlayers = admins.stream()
                .filter(a -> a.getUsername().contains(adminUsername))
                .toList();

        adminsTableView.setItems(FXCollections.observableArrayList(filteredPlayers));
    }

    public void deleteAdmin(){
        SelectionModel<Admin> selectionModel = adminsTableView.getSelectionModel();
        Admin selectedItem = selectionModel.getSelectedItem();
        Session instance = Session.setGuest();
        if(instance.getPrivileges() == Permission.ADMIN.getPermission()){
            if(selectedItem != null){
                AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to delete this record?", Alert.AlertType.CONFIRMATION, true);
                if(JavaFXUtils.showAlert(confirmAlert)){
                    Long id = selectedItem.getId();
                    GetAdminsFromFileThread adminsThread = new GetAdminsFromFileThread();
                    Thread threadAdmins = new Thread(adminsThread);
                    threadAdmins.start();
                    try {
                        threadAdmins.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    Admin admin = adminsThread.getAdminsFromFileThread().stream().filter(a -> a.getId().equals(id)).findFirst().get();

                    DeleteAdminFromFileThread deleteAdminThreadFile = new DeleteAdminFromFileThread(id);
                    Thread deleteThreadAdminFile = new Thread(deleteAdminThreadFile);
                    deleteThreadAdminFile.start();
                    try {
                        deleteThreadAdminFile.join();
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage());
                    }
                    adminSearch();

                    SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), new Admin(), admin, instance.getUserName()));
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
        }else{
            AlertRecord alertInfoAdmin = new AlertRecord("Unsuccessful", "Only Admins can delete records", Alert.AlertType.INFORMATION, false);
            JavaFXUtils.showAlert(alertInfoAdmin);
        }
    }
}
