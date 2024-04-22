package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Player;
import hr.javafx.unmask.unmaskapp.model.Team;

public class EditPlayerThread extends DatabaseUtilsThread implements Runnable{
    private Player newPlayer;
    private Team newTeam;

    // Constructor
    public EditPlayerThread(Player newPlayer, Team newTeam) {
        this.newPlayer = newPlayer;
        this.newTeam = newTeam;
    }
    @Override
    public void run() {
        super.editPlayer(newPlayer, newTeam);
    }
}
