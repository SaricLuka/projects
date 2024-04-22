package hr.javafx.unmask.unmaskapp.model;

import hr.javafx.unmask.unmaskapp.interfaces.Status;

public class Match implements Status {
    private Long id;
    private Team teamOne;
    private Team teamTwo;
    private Integer scoreTeamOne;
    private Integer scoreTeamTwo;
    private Team winner;
    private Boolean status;

    private Match(Builder builder) {
        this.id = builder.id;
        this.teamOne = builder.teamOne;
        this.teamTwo = builder.teamTwo;
        this.scoreTeamOne = builder.scoreTeamOne;
        this.scoreTeamTwo = builder.scoreTeamTwo;
        this.winner = builder.winner;
        this.status = builder.status;
    }
    public Match(Match other) {
        this.id = other.id;
        this.teamOne = other.teamOne;
        this.teamTwo = other.teamTwo;
        this.scoreTeamOne = other.scoreTeamOne;
        this.scoreTeamTwo = other.scoreTeamTwo;
        this.winner = other.winner;
        this.status = other.status;
    }

    public String toString(){
        return teamOne.getName() + " " + teamTwo.getName() + " " + scoreTeamOne + " " + scoreTeamTwo + " " + winner.getName() + " " + status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTeamOne(Team teamOne) {
        this.teamOne = teamOne;
    }

    public void setTeamTwo(Team teamTwo) {
        this.teamTwo = teamTwo;
    }

    public void setScoreTeamOne(Integer scoreTeamOne) {
        this.scoreTeamOne = scoreTeamOne;
    }

    public void setScoreTeamTwo(Integer scoreTeamTwo) {
        this.scoreTeamTwo = scoreTeamTwo;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    public Long getId() {
        return id;
    }

    public Team getTeamOne() {
        return teamOne;
    }

    public Team getTeamTwo() {
        return teamTwo;
    }

    public Integer getScoreTeamOne() {
        return scoreTeamOne;
    }

    public Integer getScoreTeamTwo() {
        return scoreTeamTwo;
    }

    public Team getWinner() {
        return winner;
    }

    public Boolean getStatus() {
        return status;
    }

    public static class Builder {
        private Long id;
        private Team teamOne;
        private Team teamTwo;
        private Integer scoreTeamOne;
        private Integer scoreTeamTwo;
        private Team winner;
        private Boolean status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder teamOne(Team teamOne) {
            this.teamOne = teamOne;
            return this;
        }

        public Builder teamTwo(Team teamTwo) {
            this.teamTwo = teamTwo;
            return this;
        }

        public Builder scoreTeamOne(Integer scoreTeamOne) {
            this.scoreTeamOne = scoreTeamOne;
            return this;
        }

        public Builder scoreTeamTwo(Integer scoreTeamTwo) {
            this.scoreTeamTwo = scoreTeamTwo;
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

        public Match build() {
            return new Match(this);
        }
    }
}