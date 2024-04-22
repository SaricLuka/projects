package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Tournament;

public class EditTournamentThread extends DatabaseUtilsThread implements Runnable{
    private Tournament newTournament;

    public EditTournamentThread(Tournament newTournament) {
        this.newTournament = newTournament;
    }
    @Override
    public void run() {
        super.editTournament(newTournament);
    }
}
