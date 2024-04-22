package hr.javafx.unmask.unmaskapp.model;

import hr.javafx.unmask.unmaskapp.interfaces.Status;

import java.util.Set;

public class Tournament implements Status {
    private Long id;
    private String name;
    private Set<Team> teams;
    private Set<Match> matches;
    private Team winner;
    private Boolean status;

    public String toString(){
        return name + " " + winner.getName() + " " + status;
    }
    public Tournament(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.teams = builder.teams;
        this.matches = builder.matches;
        this.winner = builder.winner;
        this.status = builder.status;
    }
    public Tournament(Tournament other) {
        this.id = other.id;
        this.name = other.name;
        this.teams = other.teams;
        this.matches = other.matches;
        this.winner = other.winner;
        this.status = other.status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public static class Builder {
        private Long id;
        private String name;
        private Set<Team> teams;
        private Set<Match> matches;
        private Team winner;
        private Boolean status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder teams(Set<Team> teams) {
            this.teams = teams;
            return this;
        }

        public Builder matches(Set<Match> matches) {
            this.matches = matches;
            return this;
        }

        public Builder winner(Team winner) {
            this.winner = winner;
            return this;
        }

        public Builder status(Boolean status) {
            this.status = status;
            return this;
        }

        public Tournament build() {
            return new Tournament(this);
        }
    }
}
