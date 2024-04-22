package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Team;

public class AddTeamThread extends DatabaseUtilsThread implements Runnable{
    private final Team team;

    public AddTeamThread(Team team) {
        this.team = team;
    }
    @Override
    public void run() {
        super.addTeam(team);
    }
}
