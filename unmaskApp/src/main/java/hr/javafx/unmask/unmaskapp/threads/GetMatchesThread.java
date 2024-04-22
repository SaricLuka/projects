package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Match;
import hr.javafx.unmask.unmaskapp.model.Player;

import java.util.List;

public class GetMatchesThread extends DatabaseUtilsThread implements Runnable{
    private List<Match> matches;
    @Override
    public void run() {
        matches = super.getMatches();
    }

    public List<Match> getMatchesThread() {
        return matches;
    }
}
