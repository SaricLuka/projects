package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Tournament;

public class AddTournamentThread extends DatabaseUtilsThread implements Runnable{
    private final Tournament tournament;

    public AddTournamentThread(Tournament tournament) {
        this.tournament = tournament;
    }
    @Override
    public void run() {
        super.addTournament(tournament);
    }
}
