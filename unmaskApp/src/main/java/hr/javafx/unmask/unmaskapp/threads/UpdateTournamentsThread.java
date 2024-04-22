package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.SearchTournamentsScreenController;
import javafx.application.Platform;
import javafx.util.Duration;

public class UpdateTournamentsThread extends Thread {
    private final SearchTournamentsScreenController controller;

    public UpdateTournamentsThread(SearchTournamentsScreenController controller) {
        this.controller = controller;
    }

    @Override
    public void start() {
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        Duration.seconds(5),
                        event -> Platform.runLater(this::updateTournaments)
                )
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }

    private void updateTournaments() {
        controller.tournamentSearch();
    }
}
