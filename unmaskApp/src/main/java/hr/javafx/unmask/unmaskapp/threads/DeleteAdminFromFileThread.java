package hr.javafx.unmask.unmaskapp.threads;

public class DeleteAdminFromFileThread extends FileUtilsThread implements Runnable {
    private final Long id;

    public DeleteAdminFromFileThread(Long id) {
        this.id = id;
    }
    @Override
    public void run() {
        super.deleteAdminFromFile(id);
    }
}
