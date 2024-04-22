package hr.javafx.unmask.unmaskapp.threads;

public class DeletePlayerFromFileThread  extends FileUtilsThread implements Runnable {
    private final Long id;

    public DeletePlayerFromFileThread(Long id) {
        this.id = id;
    }
    @Override
    public void run() {
        super.deletePlayerFromFile(id);
    }
}
