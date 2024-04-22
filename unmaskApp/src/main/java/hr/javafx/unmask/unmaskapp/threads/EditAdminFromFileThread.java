package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Admin;

public class EditAdminFromFileThread extends FileUtilsThread implements Runnable {
    private final Admin newAdmin;

    public EditAdminFromFileThread(Admin newAdmin) {
        this.newAdmin = newAdmin;
    }
    @Override
    public void run() {
        super.editAdminFromFile(newAdmin);
    }
}
