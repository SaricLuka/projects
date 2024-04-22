package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.model.Match;

public class AddMatchThread extends DatabaseUtilsThread implements Runnable{
    private final Match match;
    private final Long id;

    public AddMatchThread(Match match, Long id) {
        this.match = match;
        this.id = id;
    }
    @Override
    public void run() {
        super.addMatch(match, id);
    }
}
