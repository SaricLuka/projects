package hr.javafx.unmask.unmaskapp.threads;

public class DeleteTeamThread extends DatabaseUtilsThread implements Runnable{
    private final Long id;

    public DeleteTeamThread(Long id) {
        this.id = id;
    }
    @Override
    public void run() {
        super.deleteTeam(id);
    }
}
