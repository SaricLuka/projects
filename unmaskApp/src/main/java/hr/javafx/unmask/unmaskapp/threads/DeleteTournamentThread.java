package hr.javafx.unmask.unmaskapp.threads;

public class DeleteTournamentThread extends DatabaseUtilsThread implements Runnable{
    private final Long id;

    public DeleteTournamentThread(Long id) {
        this.id = id;
    }
    @Override
    public void run() {
        super.deleteTournament(id);
    }
}
