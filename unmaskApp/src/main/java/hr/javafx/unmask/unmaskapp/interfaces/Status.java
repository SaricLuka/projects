package hr.javafx.unmask.unmaskapp.interfaces;

import hr.javafx.unmask.unmaskapp.exceptions.UnableToStatusOngoing;

public interface Status {
    default Boolean statusEnd(Boolean status) throws UnableToStatusOngoing{
            if (!status) {
                throw new UnableToStatusOngoing();
            }
        return false;
    }
}