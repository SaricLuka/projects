package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Player;

import java.util.List;

public class GetPlayersThread extends DatabaseUtilsThread implements Runnable{
    private List<Player> players;
    @Override
    public void run() {
        players = super.getPlayers();
    }
    public List<Player> getPlayersThread() {
        return players;
    }
}
