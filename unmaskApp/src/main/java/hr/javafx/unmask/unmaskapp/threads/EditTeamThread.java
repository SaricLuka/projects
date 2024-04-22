package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Team;

public class EditTeamThread extends DatabaseUtilsThread implements Runnable{
    private Team newTeam;

    public EditTeamThread(Team newTeam) {
        this.newTeam = newTeam;
    }
    @Override
    public void run() {
        super.editTeam(newTeam);
    }
}
