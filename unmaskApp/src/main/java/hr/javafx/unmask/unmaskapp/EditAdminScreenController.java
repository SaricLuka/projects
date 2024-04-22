package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.model.Admin;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.records.AlertRecord;
import hr.javafx.unmask.unmaskapp.threads.EditAdminFromFileThread;
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

public class EditAdminScreenController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private TextField usernameTextField;
    @FXML
    private TableView<Admin> adminsTableView;
    @FXML
    TableColumn<Admin, String> adminUsernameTableColumn;

    @FXML
    private void initialize(){
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
    public void adminEdit(){
        String newUsername = usernameTextField.getText();
        SelectionModel<Admin> selectionModel = adminsTableView.getSelectionModel();
        Admin admin = selectionModel.getSelectedItem();
        if(admin != null){
            AlertRecord confirmAlert = new AlertRecord("Please confirm", "Are you sure you want to edit this record?", Alert.AlertType.CONFIRMATION, true);
            if(JavaFXUtils.showAlert(confirmAlert)){
                Admin newAdmin = new Admin(admin);
                if(!(newUsername.isEmpty())){
                    newAdmin.setUsername(newUsername);
                }
                EditAdminFromFileThread editAdminThreadFile = new EditAdminFromFileThread(newAdmin);
                Thread editThreadAdminFile = new Thread(editAdminThreadFile);
                editThreadAdminFile.start();
                try {
                    editThreadAdminFile.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }


                Session instance = Session.setGuest();
                SerializeChangesListThread changesThread = new SerializeChangesListThread(new Changes<>(LocalDateTime.now(), newAdmin, admin, instance.getUserName()));
                Thread threadChanges = new Thread(changesThread);
                threadChanges.start();
                try {
                    threadChanges.join();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }

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
        }else{
            AlertRecord missingSelection = new AlertRecord("Unsuccessful", "A record has to be selected", Alert.AlertType.ERROR, false);
            JavaFXUtils.showAlert(missingSelection);
        }
    }
}
