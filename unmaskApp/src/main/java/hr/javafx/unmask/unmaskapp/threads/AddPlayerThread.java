package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Player;
import hr.javafx.unmask.unmaskapp.model.Team;

public class AddPlayerThread  extends DatabaseUtilsThread implements Runnable{
    private final Player player;
    private final Team team;

    @Override
    public void run() {
        super.addPlayer(player, team);
    }
    public AddPlayerThread(Player player, Team team) {
        this.player = player;
        this.team = team;
    }
}
