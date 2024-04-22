package hr.javafx.unmask.unmaskapp.model;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private Long id;
    private Player creator;
    private String name;
    private Set<Player> players;
    private Integer matchesWon;
    private Integer matchesLost;
    private Integer tournamentsWon;
    private Integer tournamentsPlayed;

    public Team(Long id, Player creator, String name, Set<Player> players, Integer matchesWon, Integer matchesLost, Integer tournamentsWon, Integer tournamentsPlayed) {
        this.id = id;
        this.creator = creator;
        this.name = name;
        this.players = players;
        this.matchesWon = matchesWon;
        this.matchesLost = matchesLost;
        this.tournamentsWon = tournamentsWon;
        this.tournamentsPlayed = tournamentsPlayed;
    }
    public Team(Team other) {
        this.id = other.id;
        this.creator = new Player(other.creator);
        this.name = other.name;
        this.players = new HashSet<>(other.players);
        this.matchesWon = other.matchesWon;
        this.matchesLost = other.matchesLost;
        this.tournamentsWon = other.tournamentsWon;
        this.tournamentsPlayed = other.tournamentsPlayed;
    }
    public Team() {
        this.id = null;
        this.creator = new Player.Builder().build();
        this.name = null;
        this.players = null;
        this.matchesWon = null;
        this.matchesLost = null;
        this.tournamentsWon = null;
        this.tournamentsPlayed = null;
    }
    public String toString(){
        return getCreator().getId() + " " + getName() + " " + getMatchesWon() + " " + getMatchesLost() + " " + getTournamentsWon() + " " + getTournamentsPlayed();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getCreator() {
        return creator;
    }

    public void setCreator(Player creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Integer getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
    }

    public Integer getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(Integer matchesLost) {
        this.matchesLost = matchesLost;
    }

    public Integer getTournamentsWon() {
        return tournamentsWon;
    }

    public void setTournamentsWon(Integer tournamentsWon) {
        this.tournamentsWon = tournamentsWon;
    }

    public Integer getTournamentsPlayed() {
        return tournamentsPlayed;
    }

    public void setTournamentsPlayed(Integer tournamentsPlayed) {
        this.tournamentsPlayed = tournamentsPlayed;
    }
}
