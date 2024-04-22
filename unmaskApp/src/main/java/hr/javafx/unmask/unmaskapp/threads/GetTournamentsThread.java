package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.model.Tournament;

import java.util.List;

public class GetTournamentsThread extends DatabaseUtilsThread implements Runnable{
    private List<Tournament> tournaments;
    @Override
    public void run() {
        tournaments = super.getTournaments();
    }

    public List<Tournament> getTournamentsThread() {
        return tournaments;
    }
}
