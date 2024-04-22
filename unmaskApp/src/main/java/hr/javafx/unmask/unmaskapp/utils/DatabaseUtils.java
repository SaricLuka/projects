package hr.javafx.unmask.unmaskapp.utils;

import hr.javafx.unmask.unmaskapp.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseUtils {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
    private static final String DATABASE_FILE = "src/conf/database.properties";

    private static synchronized Connection connectToDatabase() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(DATABASE_FILE));
        String databaseUrl= properties.getProperty("databaseUrl");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        Connection connection = DriverManager.getConnection(databaseUrl, username, password);
        return connection;
    }

    /*PLAYER----------------------------------------------------------------------------------------------------------*/
    public static void addPlayer(Player player, Team team){
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "INSERT INTO PLAYER(USERNAME, HASHED_PASSWORD) VALUES(?, ?);";

            try (PreparedStatement prepStmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
                prepStmt.setString(1, player.getUsername());
                prepStmt.setString(2, player.getHashedPassword());

                int affectedRows = prepStmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = prepStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            Long playerId = generatedKeys.getLong("ID");
                            player.setId(playerId);
                        }
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            logger.error("Error while saving to database. " + ex);
        }
        try(Connection connection = connectToDatabase()){
            String sqlQuery = "INSERT INTO TEAM_PLAYER(TEAM_ID, PLAYER_ID) VALUES(?, ?);";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, team.getId());
            prepStmt.setLong(2, player.getId());
            prepStmt.executeUpdate();

        }
        catch(SQLException | IOException ex){
            logger.error("Error while saving to database. " + ex);
        }
    }
    public static List<Player> getPlayers(){

        List<Player> players = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM PLAYER";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while(rs.next()){
                Long id = rs.getLong("ID");
                String username = rs.getString("USERNAME");
                String hashedPassword = rs.getString("HASHED_PASSWORD");
                String teamName = getPlayerTeamName(id);
                Player player = new Player.Builder()
                        .id(id)
                        .username(username)
                        .hashedPassword(hashedPassword)
                        .teamName(teamName)
                        .build();
                players.add(player);

            }
        }
        catch(SQLException | IOException ex){
            logger.error("Error while reading database. " + ex.getMessage());
        }
        return players;
    }
    public static void deletePlayer(Long id){
        List<Team> teams = getTeams();
        Map<Long, Long> teamIds = teams.stream()
                .filter(team -> team.getCreator().getId() == id )
                .collect(Collectors.toMap(Team::getId, player -> id));
        teamIds.forEach((teamId, playerId) -> {
            deleteTeam(teamId);
        });
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "DELETE FROM TEAM_PLAYER WHERE PLAYER_ID = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "DELETE FROM PLAYER WHERE ID = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
    }
    public static void editPlayer(Player newPlayer, Team newTeam){
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "UPDATE PLAYER SET USERNAME = ? WHERE ID = ?";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setString(1, newPlayer.getUsername());
            prepStmt.setLong(2, newPlayer.getId());

            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "DELETE FROM TEAM_PLAYER WHERE PLAYER_ID = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, newPlayer.getId());
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
        if(newTeam != null){
            try(Connection connection = connectToDatabase()){
                String sqlQuery = "INSERT INTO TEAM_PLAYER(TEAM_ID, PLAYER_ID) VALUES(?, ?);";
                PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
                prepStmt.setLong(1, newTeam.getId());
                prepStmt.setLong(2, newPlayer.getId());
                prepStmt.executeUpdate();

            }
            catch(SQLException | IOException ex){
                logger.error("Error while saving to database. " + ex);
            }
        }
    }
    public static String getPlayerTeamName(Long playerId){

        String teamName = null;

        try(Connection connection = connectToDatabase()){

            String sqlQuery = "SELECT * FROM TEAM_PLAYER TP, TEAM T WHERE TP.PLAYER_ID = ? AND TP.TEAM_ID = T.ID;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setInt(1, Math.toIntExact(playerId));
            ResultSet rs = prepStmt.executeQuery();
            while(rs.next()){
                String name = rs.getString("NAME");
                teamName = name;
            }
        }
        catch(SQLException | IOException ex){
            logger.error("Error while reading database. " + ex.getMessage());
        }

        return teamName;
    }

    /*TEAM------------------------------------------------------------------------------------------------------------*/
    public static void addTeam(Team team){
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "INSERT INTO TEAM(CREATOR_ID, NAME, MATCHES_WON, MATCHES_LOST, TOURNAMENTS_WON, TOURNAMENTS_PLAYED) VALUES(?, ?, 0, 0, 0, 0);";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, team.getCreator().getId());
            prepStmt.setString(2, team.getName());
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while saving to database. " + ex);
        }
    }
    public static List<Team> getTeams(){

        List<Team> teams = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM TEAM";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while(rs.next()){
                Long id = rs.getLong("ID");
                Long creatorId = rs.getLong("CREATOR_ID");
                String name = rs.getString("NAME");
                Integer matchesWon = rs.getInt("MATCHES_WON");
                Integer matchesLost = rs.getInt("MATCHES_LOST");
                Integer tournamentsWon = rs.getInt("TOURNAMENTS_WON");
                Integer tournamentsPlayed = rs.getInt("TOURNAMENTS_PLAYED");
                List<Player> player = getPlayers().stream()
                        .filter(p ->p.getId().equals(creatorId))
                        .toList();
                Set<Player> players = getTeamPlayers(id);

                Team team = new Team (id, player.getFirst(), name, players, matchesWon, matchesLost, tournamentsWon, tournamentsPlayed);
                teams.add(team);
            }
        }
        catch(SQLException | IOException ex){
            logger.error("Error while reading database. " + ex.getMessage());
        }
        return teams;
    }
    public static void deleteTeam(Long id){
        List<Match> matches = getMatches();
        Map<Long, Long> matchIds = matches.stream()
                .filter(match -> match.getTeamOne().getId() == id || match.getTeamTwo().getId() == id)
                .collect(Collectors.toMap(Match::getId, match -> id));
        matchIds.forEach((matchId, teamId) -> {
            deleteMatch(matchId);
        });
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "DELETE FROM TOURNAMENT_TEAM WHERE TEAM_ID = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "DELETE FROM TEAM_PLAYER WHERE TEAM_ID = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "DELETE FROM TEAM WHERE ID = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
    }
    public static void editTeam(Team newTeam){
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "UPDATE TEAM SET NAME = ?, MATCHES_WON = ?, MATCHES_LOST = ?, TOURNAMENTS_WON = ?, TOURNAMENTS_PLAYED = ? WHERE ID = ?";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setString(1, newTeam.getName());
            prepStmt.setInt(2, newTeam.getMatchesWon());
            prepStmt.setInt(3, newTeam.getMatchesLost());
            prepStmt.setInt(4, newTeam.getTournamentsWon());
            prepStmt.setInt(5, newTeam.getTournamentsPlayed());
            prepStmt.setLong(6, newTeam.getId());
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
    }
    public static Set<Player> getTeamPlayers(Long teamId){

        Set<Player> players = new HashSet<>();

        try(Connection connection = connectToDatabase()){

            String sqlQuery = "SELECT * FROM TEAM_PLAYER TP, PLAYER P WHERE TP.TEAM_ID = ? AND TP.PLAYER_ID = P.ID;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setInt(1, Math.toIntExact(teamId));
            ResultSet rs = prepStmt.executeQuery();

            while(rs.next()){
                Long id = rs.getLong("ID");
                String username = rs.getString("USERNAME");
                String hashedPassword = rs.getString("HASHED_PASSWORD");

                Player player = new Player.Builder()
                        .id(id)
                        .username(username)
                        .hashedPassword(hashedPassword)
                        .build();
                players.add(player);

            }
        }
        catch(SQLException | IOException ex){
            logger.error("Error while reading database. " + ex.getMessage());
        }

        return players;
    }

    /*MATCH-----------------------------------------------------------------------------------------------------------*/
    public static void addMatch(Match match, Long id){
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "INSERT INTO MATCH(TEAM_ONE_ID, TEAM_TWO_ID, SCORE_TEAM_ONE,SCORE_TEAM_TWO, WINNER_ID, STATUS) VALUES(?, ?, 0, 0, NULL, TRUE);";

            try (PreparedStatement prepStmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
                prepStmt.setLong(1, match.getTeamOne().getId());
                prepStmt.setLong(2, match.getTeamTwo().getId());

                int affectedRows = prepStmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = prepStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            Long matchId = generatedKeys.getLong("ID");
                            match.setId(matchId);

                        }
                    }
                }
            }
        } catch (SQLException | IOException ex) {
            logger.error("Error while saving to database. " + ex);
        }
        try(Connection connection = connectToDatabase()){
            String sqlQuery = "INSERT INTO TOURNAMENT_MATCH(TOURNAMENT_ID, MATCH_ID) VALUES(?, ?);";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, id);
            prepStmt.setLong(2, match.getId());
            prepStmt.executeUpdate();
        }
        catch(SQLException | IOException ex){
            logger.error("Error while saving to database. " + ex);
        }
    }
    public static List<Match> getMatches(){
        List<Match> matches = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM MATCH";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while(rs.next()){
                Long id = rs.getLong("ID");
                Long teamOneId = rs.getLong("TEAM_ONE_ID");
                Long teamTwoId = rs.getLong("TEAM_TWO_ID");
                Integer scoreTeamOne = rs.getInt("SCORE_TEAM_ONE");
                Integer scoreTeamTwo = rs.getInt("SCORE_TEAM_TWO");
                Long winnerId = rs.getLong("WINNER_ID");
                Boolean status = rs.getBoolean("STATUS");
                List<Team> teams = getTeams();
                List<Team> teamOne = teams.stream()
                        .filter(t -> t.getId().equals(teamOneId))
                        .toList();
                List<Team> teamTwo = teams.stream()
                        .filter(t -> t.getId().equals(teamTwoId))
                        .toList();
                Team winner;
                if(winnerId == 0){
                    winner = null;
                }else{
                    List<Team> winners = teams.stream()
                            .filter(t -> t.getId().equals(winnerId))
                            .toList();
                    winner = winners.get(0);
                }

                Match match = new Match.Builder()
                        .id(id)
                        .teamOne(teamOne.get(0))
                        .teamTwo(teamTwo.get(0))
                        .scoreTeamOne(scoreTeamOne)
                        .scoreTeamTwo(scoreTeamTwo)
                        .winner(winner)
                        .status(status)
                        .build();
                matches.add(match);

            }
        }
        catch(SQLException | IOException ex){
            logger.error("Error while reading database. " + ex.getMessage());
        }
        return matches;
    }

    public static void deleteMatch(Long id){
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "DELETE FROM TOURNAMENT_MATCH WHERE MATCH_ID = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "DELETE FROM MATCH WHERE ID = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
    }
    public static void editMatch(Match newMatch){
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "UPDATE MATCH SET SCORE_TEAM_ONE = ?, SCORE_TEAM_TWO = ?, WINNER_ID = ?, STATUS = ? WHERE ID = ?";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setInt(1, newMatch.getScoreTeamOne());
            prepStmt.setInt(2, newMatch.getScoreTeamTwo());
            Long winnerId = (newMatch.getWinner() != null) ? newMatch.getWinner().getId() : null;
            prepStmt.setObject(3, winnerId, Types.BIGINT);
            prepStmt.setBoolean(4, newMatch.getStatus());
            prepStmt.setLong(5, newMatch.getId());
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
    }

    /*TOURNAMENT------------------------------------------------------------------------------------------------------*/
    public static void addTournament(Tournament tournament){
        try (Connection connection = connectToDatabase()) {
        String sqlQuery = "INSERT INTO TOURNAMENT(NAME, WINNER_ID, STATUS) VALUES(?, NULL, TRUE);";
            try (PreparedStatement prepStmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
                prepStmt.setString(1, tournament.getName());

                int affectedRows = prepStmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = prepStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            Long tournamentId = generatedKeys.getLong("ID");
                            tournament.setId(tournamentId);
                        }
                    }
                }
            }
        }catch (SQLException | IOException ex) {
            logger.error("Error while saving to database. " + ex);
        }
        try(Connection connection = connectToDatabase()){
            String sqlQuery = "INSERT INTO TOURNAMENT_TEAM(TOURNAMENT_ID, TEAM_ID) VALUES(?, ?);";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            Long tournamentId = tournament.getId();

            List<Team> teams = new ArrayList<>(tournament.getTeams());
            for (Team team : teams) {
                prepStmt.setLong(1, tournamentId);
                prepStmt.setLong(2, team.getId());
                prepStmt.executeUpdate();
            }
        }
        catch(SQLException | IOException ex){
            logger.error("Error while saving to database. " + ex);
        }

    }
    public static List<Tournament> getTournaments(){

        List<Tournament> tournaments = new ArrayList<>();

        try(Connection connection = connectToDatabase()){
            String sqlQuery = "SELECT * FROM TOURNAMENT";
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            ResultSet rs = stmt.getResultSet();

            while(rs.next()){
                Long id = rs.getLong("ID");
                String name = rs.getString("NAME");
                Long winnerId = rs.getLong("WINNER_ID");
                Boolean status = rs.getBoolean("STATUS");

                Set<Team> teams = getTournamentTeams(id);
                Set<Match> matches = getTournamentMatches(id);
                Team winner;
                if(winnerId == 0){
                    winner = null;
                }else{
                    List<Team> winners = teams.stream()
                            .filter(t -> t.getId().equals(winnerId))
                            .toList();
                    winner = winners.get(0);
                }
                Tournament tournament = new Tournament.Builder()
                        .id(id)
                        .name(name)
                        .teams(teams)
                        .matches(matches)
                        .winner(winner)
                        .status(status)
                        .build();
                tournaments.add(tournament);
            }
        }
        catch(SQLException | IOException ex){
            logger.error("Error while reading database. " + ex.getMessage());
        }
        return tournaments;
    }
    public static void deleteTournament(Long id){
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "DELETE FROM TOURNAMENT_TEAM WHERE TOURNAMENT_ID = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "DELETE FROM TOURNAMENT_MATCH WHERE TOURNAMENT_ID = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "DELETE FROM TOURNAMENT WHERE ID = ?;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setLong(1, id);
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
    }
    public static void editTournament(Tournament newTournament){
        try (Connection connection = connectToDatabase()) {
            String sqlQuery = "UPDATE TOURNAMENT SET NAME = ?, WINNER_ID = ?, STATUS = ? WHERE ID = ?";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setString(1, newTournament.getName());
            Long winnerId = (newTournament.getWinner() != null) ? newTournament.getWinner().getId() : null;
            prepStmt.setObject(2, winnerId, Types.BIGINT);
            prepStmt.setBoolean(3, newTournament.getStatus());
            prepStmt.setLong(4, newTournament.getId());
            prepStmt.executeUpdate();

        } catch (SQLException | IOException ex) {
            logger.error("Error while deleting from database." + ex);
        }
    }
    public static Set<Team> getTournamentTeams(Long tournamentId) {

        Set<Team> tournamentTeams = new HashSet<>();

        try(Connection connection = connectToDatabase()){

            String sqlQuery = "SELECT * FROM TOURNAMENT_TEAM TT, TEAM T WHERE TT.TOURNAMENT_ID = ? AND TT.TEAM_ID = T.ID;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setInt(1, Math.toIntExact(tournamentId));
            ResultSet rs = prepStmt.executeQuery();

            while(rs.next()){
                Long id = rs.getLong("ID");
                Long creatorId = rs.getLong("CREATOR_ID");
                String name = rs.getString("NAME");
                Integer matchesWon = rs.getInt("MATCHES_WON");
                Integer matchesLost = rs.getInt("MATCHES_LOST");
                Integer tournamentsWon = rs.getInt("TOURNAMENTS_WON");
                Integer tournamentsPlayed = rs.getInt("TOURNAMENTS_PLAYED");
                List<Player> player = getPlayers().stream()
                        .filter(p ->p.getId().equals(creatorId))
                        .toList();
                Set<Player> players = getTeamPlayers(id);

                Team team = new Team (id, player.getFirst(), name, players, matchesWon, matchesLost, tournamentsWon, tournamentsPlayed);
                tournamentTeams.add(team);
            }
        }
        catch(SQLException | IOException ex){
            logger.error("Error while reading database. " + ex.getMessage());
        }

        return tournamentTeams;
    }
    public static Set<Match> getTournamentMatches(Long tournamentId) {

        Set<Match> tournamentMatches = new HashSet<>();

        try(Connection connection = connectToDatabase()){

            String sqlQuery = "SELECT * FROM TOURNAMENT_MATCH TM, MATCH M WHERE TM.TOURNAMENT_ID = ? AND TM.MATCH_ID = M.ID;";
            PreparedStatement prepStmt = connection.prepareStatement(sqlQuery);
            prepStmt.setInt(1, Math.toIntExact(tournamentId));
            ResultSet rs = prepStmt.executeQuery();

            while(rs.next()){
                Long id = rs.getLong("ID");
                Long teamOneId = rs.getLong("TEAM_ONE_ID");
                Long teamTwoId = rs.getLong("TEAM_TWO_ID");
                Integer scoreTeamOne = rs.getInt("SCORE_TEAM_ONE");
                Integer scoreTeamTwo = rs.getInt("SCORE_TEAM_TWO");
                Long winnerId = rs.getLong("WINNER_ID");
                Boolean status = rs.getBoolean("STATUS");
                List<Team> teams = getTeams();
                List<Team> teamOne = teams.stream()
                        .filter(t -> t.getId().equals(teamOneId))
                        .toList();
                List<Team> teamTwo = teams.stream()
                        .filter(t -> t.getId().equals(teamTwoId))
                        .toList();
                Team winner;
                if(winnerId == 0){
                    winner = null;
                }else{
                    List<Team> winners = teams.stream()
                            .filter(t -> t.getId().equals(winnerId))
                            .toList();
                    winner = winners.get(0);
                }

                Match match = new Match.Builder()
                        .id(id)
                        .teamOne(teamOne.get(0))
                        .teamTwo(teamTwo.get(0))
                        .scoreTeamOne(scoreTeamOne)
                        .scoreTeamTwo(scoreTeamTwo)
                        .winner(winner)
                        .status(status)
                        .build();
                tournamentMatches.add(match);
            }
        }
        catch(SQLException | IOException ex){
            logger.error("Error while reading database. " + ex.getMessage());
        }

        return tournamentMatches;
    }

}
