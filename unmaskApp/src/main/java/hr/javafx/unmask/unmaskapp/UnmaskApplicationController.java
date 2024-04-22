package hr.javafx.unmask.unmaskapp;

import hr.javafx.unmask.unmaskapp.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.*;


public class UnmaskApplicationController {

    @FXML
    public Label welcomeLabel;

    public void initialize(){
        Session session = Session.setGuest();
        welcomeLabel.setText("Welcome to Unmask,\n" + session.getUserName());
    }
}