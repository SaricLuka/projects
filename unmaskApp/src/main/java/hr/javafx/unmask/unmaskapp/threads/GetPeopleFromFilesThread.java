package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Person;

import java.util.List;

public class GetPeopleFromFilesThread  extends FileUtilsThread implements Runnable {
    private List<Person> people;

    @Override
    public void run() {
        people = super.getPeopleFromFiles();
    }

    public List<Person> getPeopleFromFilesThread() {
        return people;
    }
}
