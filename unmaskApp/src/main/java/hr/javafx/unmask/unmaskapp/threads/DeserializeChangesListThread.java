package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Changes;

import java.util.List;

public class DeserializeChangesListThread extends FileUtilsThread implements Runnable {
    private List<Changes<?>> changes;

    @Override
    public void run() {
        changes = super.deserializeChangesList();
    }

    public List<Changes<?>> getChanges() {
        return changes;
    }
}
