package hr.javafx.unmask.unmaskapp.threads;

public class DeletePlayerThread extends DatabaseUtilsThread implements Runnable{
    private final Long id;

    public DeletePlayerThread(Long id) {
        this.id = id;
    }
    @Override
    public void run() {
        super.deletePlayer(id);
    }
}
