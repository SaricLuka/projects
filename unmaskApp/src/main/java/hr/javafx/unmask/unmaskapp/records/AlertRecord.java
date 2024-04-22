package hr.javafx.unmask.unmaskapp.records;

import javafx.scene.control.Alert;

public record AlertRecord(String title, String content, Alert.AlertType alertType, Boolean question) {
}
