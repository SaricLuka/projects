package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Admin;

import java.util.List;

public class AddAdminsToFileThread extends FileUtilsThread implements Runnable {
    private final List<Admin> admins;

    public AddAdminsToFileThread(List<Admin> admins) {
        this.admins = admins;
    }
    @Override
    public void run() {
        super.addAdminsToFile(admins);
    }
}
