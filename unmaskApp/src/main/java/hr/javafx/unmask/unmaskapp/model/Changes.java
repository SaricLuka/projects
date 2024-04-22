package hr.javafx.unmask.unmaskapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class  Changes<T> implements Serializable {
    private LocalDateTime dateTime;
    private String entity;
    private String newValue;
    private String oldValue;
    private String personUsername;

    public Changes(LocalDateTime dateTime, T newEntity, T oldEntity, String personUsername) {
        this.dateTime = dateTime;
        this.entity = newEntity.getClass().getSimpleName();
        Stringer<?, ?> strings = new Stringer<>(newEntity, oldEntity);
        this.newValue = strings.getStringFromFirst();
        this.oldValue = strings.getStringFromSecond();
        this.personUsername = personUsername;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getPersonUsername() {
        return personUsername;
    }

    public void setPersonUsername(String personUsername) {
        this.personUsername = personUsername;
    }
}
