package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Player;

import java.util.List;

public class GetPlayersFromFileThread extends FileUtilsThread implements Runnable {
    private List<Player> players;

    @Override
    public void run() {
        players = super.getPlayersFromFile();
    }

    public List<Player> getPlayersFromFileThread() {
        return players;
    }
}
