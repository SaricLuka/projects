package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Match;

public class EditMatchThread extends DatabaseUtilsThread implements Runnable{
    private final Match newMatch;

    public EditMatchThread(Match newMatch) {
        this.newMatch = newMatch;
    }
    @Override
    public void run() {
        super.editMatch(newMatch);
    }
}
