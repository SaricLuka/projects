package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.UnmaskApplicationController;
import hr.javafx.unmask.unmaskapp.model.Match;
import hr.javafx.unmask.unmaskapp.model.Player;
import hr.javafx.unmask.unmaskapp.model.Team;
import hr.javafx.unmask.unmaskapp.model.Tournament;
import hr.javafx.unmask.unmaskapp.utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public abstract class DatabaseUtilsThread {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    private static Boolean activeConnectionWithDatabase = false;

    /* PLAYER -------------------------------------------------------------------------------------------------------- */
    public synchronized void addPlayer(Player player, Team team) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.addPlayer(player, team);

        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized List<Player> getPlayers() {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        List<Player> players = DatabaseUtils.getPlayers();

        activeConnectionWithDatabase = false;

        notifyAll();
        return players;
    }

    public synchronized void deletePlayer(Long id) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.deletePlayer(id);

        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized void editPlayer(Player newPlayer, Team newTeam) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.editPlayer(newPlayer, newTeam);

        activeConnectionWithDatabase = false;

        notifyAll();
    }



    /* TEAM ---------------------------------------------------------------------------------------------------------- */
    public synchronized void addTeam(Team team) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.addTeam(team);

        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized List<Team> getTeams() {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        List<Team> teams = DatabaseUtils.getTeams();

        activeConnectionWithDatabase = false;

        notifyAll();
        return teams;
    }

    public synchronized void deleteTeam(Long id) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.deleteTeam(id);

        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized void editTeam(Team newTeam) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.editTeam(newTeam);

        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized Set<Player> getTeamPlayers(Long teamId) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        Set<Player> teamPlayers = DatabaseUtils.getTeamPlayers(teamId);

        activeConnectionWithDatabase = false;

        notifyAll();
        return teamPlayers;
    }

    /* MATCH --------------------------------------------------------------------------------------------------------- */
    public synchronized void addMatch(Match match, Long id) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.addMatch(match, id);

        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized List<Match> getMatches() {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        List<Match> matches = DatabaseUtils.getMatches();

        activeConnectionWithDatabase = false;

        notifyAll();
        return matches;
    }

    public synchronized void deleteMatch(Long id) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.deleteMatch(id);

        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized void editMatch(Match newMatch) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.editMatch(newMatch);

        activeConnectionWithDatabase = false;

        notifyAll();
    }

    /* TOURNAMENT ---------------------------------------------------------------------------------------------------- */
    public synchronized void addTournament(Tournament tournament) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.addTournament(tournament);

        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized List<Tournament> getTournaments() {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        List<Tournament> tournaments = DatabaseUtils.getTournaments();

        activeConnectionWithDatabase = false;

        notifyAll();
        return tournaments;
    }

    public synchronized void deleteTournament(Long id) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.deleteTournament(id);

        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized void editTournament(Tournament newTournament) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        DatabaseUtils.editTournament(newTournament);

        activeConnectionWithDatabase = false;

        notifyAll();
    }

    public synchronized Set<Team> getTournamentTeams(Long tournamentId) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        Set<Team> tournamentTeams= DatabaseUtils.getTournamentTeams(tournamentId);

        activeConnectionWithDatabase = false;

        notifyAll();
        return  tournamentTeams;
    }

    public synchronized Set<Match> getTournamentMatches(Long tournamentId) {
        while(activeConnectionWithDatabase){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithDatabase = true;

        Set<Match> tournamentMatches = DatabaseUtils.getTournamentMatches(tournamentId);

        activeConnectionWithDatabase = false;

        notifyAll();
        return tournamentMatches;
    }
}
