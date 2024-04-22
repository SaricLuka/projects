package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.SearchMatchesScreenController;
import javafx.application.Platform;
import javafx.util.Duration;


public class UpdateMatchesThread extends Thread {
    private final SearchMatchesScreenController controller;

    public UpdateMatchesThread(SearchMatchesScreenController controller) {
        this.controller = controller;
    }

    @Override
    public void start() {
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(
                        Duration.seconds(5),
                        event -> Platform.runLater(this::updateMatches)
                )
        );
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }

    private void updateMatches() {
        controller.matchSearch();
    }
}
