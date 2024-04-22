package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.threads.DeserializeChangesListThread;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ShowChangesController {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    @FXML
    private TableView<Changes<?>> changesTableView;
    @FXML
    TableColumn<Changes<?>, String> dateTimeTableColumn;
    @FXML
    TableColumn<Changes<?>, String> entityTableColumn;
    @FXML
    TableColumn<Changes<?>, String> newValueTableColumn;
    @FXML
    TableColumn<Changes<?>, String> oldValueTableColumn;
    @FXML
    TableColumn<Changes<?>, String> personUsernameTableColumn;

    @FXML
    private void initialize(){
        dateTimeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
        entityTableColumn.setCellValueFactory(new PropertyValueFactory<>("entity"));
        newValueTableColumn.setCellValueFactory(new PropertyValueFactory<>("newValue"));
        oldValueTableColumn.setCellValueFactory(new PropertyValueFactory<>("oldValue"));
        personUsernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("personUsername"));

        DeserializeChangesListThread changesThread = new DeserializeChangesListThread();
        Thread threadChanges = new Thread(changesThread);
        threadChanges.start();
        try {
            threadChanges.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        List<Changes<?>> deserializedChangesList = changesThread.getChanges();
        if(deserializedChangesList != null){
            changesTableView.setItems(FXCollections.observableArrayList(deserializedChangesList));
        }
    }
}
