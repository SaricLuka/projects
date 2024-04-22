package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Changes;

import java.util.List;

public class SerializeChangesListThread extends FileUtilsThread implements Runnable {
    private final Changes<?> changes;

    public SerializeChangesListThread(Changes<?> changes) {
        this.changes = changes;
    }
    @Override
    public void run() {
        super.serializeChangesList(changes);
    }
}
