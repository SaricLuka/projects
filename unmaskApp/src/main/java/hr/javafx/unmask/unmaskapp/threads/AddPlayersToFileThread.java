package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Player;

import java.util.List;

public class AddPlayersToFileThread extends FileUtilsThread implements Runnable {
    private final List<Player> players;

    public AddPlayersToFileThread(List<Player> players) {
        this.players = players;
    }
    @Override
    public void run() {
        super.addPlayersToFile(players);
    }
}
