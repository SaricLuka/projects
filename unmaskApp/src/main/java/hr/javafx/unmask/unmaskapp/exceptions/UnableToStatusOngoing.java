package hr.javafx.unmask.unmaskapp.exceptions;

public class UnableToStatusOngoing extends Exception {
    public UnableToStatusOngoing(){
        super("Unable to change status to Ongoing (true).");
    }
}
