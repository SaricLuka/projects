package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Player;

public class EditPlayerFromFileThread extends FileUtilsThread implements Runnable {
    private final Player newPlayer;

    public EditPlayerFromFileThread(Player newPlayer) {
        this.newPlayer = newPlayer;
    }
    @Override
    public void run() {
        super.editPlayerFromFile(newPlayer);
    }
}
