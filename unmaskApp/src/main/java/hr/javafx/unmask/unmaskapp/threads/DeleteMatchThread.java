package hr.javafx.unmask.unmaskapp.threads;

public class DeleteMatchThread extends DatabaseUtilsThread implements Runnable{
    private final Long id;

    public DeleteMatchThread(Long id) {
        this.id = id;
    }
    @Override
    public void run() {
        super.deleteMatch(id);
    }
}
