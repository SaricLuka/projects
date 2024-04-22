package hr.javafx.unmask.unmaskapp.exceptions;

public class LogInException extends Exception{
    public LogInException(){
        super("Wrong username or password.");
    }
}
