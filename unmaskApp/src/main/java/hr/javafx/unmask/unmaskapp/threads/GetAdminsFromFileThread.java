package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Admin;

import java.util.List;

public class GetAdminsFromFileThread  extends FileUtilsThread implements Runnable {
    private List<Admin> admins;

    @Override
    public void run() {
        admins = super.getAdminsFromFile();
    }

    public List<Admin> getAdminsFromFileThread() {
        return admins;
    }
}
