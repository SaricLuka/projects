package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Match;
import hr.javafx.unmask.unmaskapp.model.Team;

import java.util.List;

public class GetTeamsThread extends DatabaseUtilsThread implements Runnable{
    private List<Team> teams;
    @Override
    public void run() {
        teams = super.getTeams();
    }

    public List<Team> getTeamsThread() {
        return teams;
    }
}
