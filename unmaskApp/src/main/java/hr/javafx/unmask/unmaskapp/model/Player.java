package hr.javafx.unmask.unmaskapp.model;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.interfaces.Permissions;

public final class Player extends Person implements Permissions {
    private String teamName;

    private Player(Long id, String username, String hashedPassword, String teamName) {
        super(id, username, hashedPassword);
        this.teamName = teamName;
    }
    public Player(Player other) {
        super(other.getId(), other.getUsername(), other.getHashedPassword());
        this.teamName = other.teamName;
    }

    @Override
    public Integer getPermissions() {
        return Permission.PLAYER.getPermission();
    }

    public String toString(){
        return getUsername() + " " + getTeamName();
    }

    // Getter for team
    public String getTeamName() {
        return teamName;
    }

    public static class Builder {
        private Long id;
        private String username;
        private String hashedPassword;
        private String teamName;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder hashedPassword(String hashedPassword) {
            this.hashedPassword = hashedPassword;
            return this;
        }

        public Builder teamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Player build() {
            return new Player(id, username, hashedPassword, teamName);
        }
    }

}
