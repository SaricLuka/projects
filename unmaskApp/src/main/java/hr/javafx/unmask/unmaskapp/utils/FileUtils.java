package hr.javafx.unmask.unmaskapp.utils;

import hr.javafx.unmask.unmaskapp.model.Admin;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Person;
import hr.javafx.unmask.unmaskapp.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static String ADMINS_FILE_NAME = "src/dat/admins.txt";
    private static final String PLAYERS_FILE_NAME = "src/dat/players.txt";
    private static final String CHANGES_FILE_NAME ="src/dat/changes.ser";

    public static List<Person> getPeopleFromFiles() {
        List<Person> people = new ArrayList<>();
        File adminsFile = new File(ADMINS_FILE_NAME);
        File playersFile = new File(PLAYERS_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(adminsFile))) {
            String line;
            while (Optional.ofNullable(line = reader.readLine()).isPresent()) {
                Long id = Long.valueOf(line);
                String username = reader.readLine();
                String hashedPassword = reader.readLine();
                people.add(new Admin(id, username, hashedPassword));
            }
        } catch (IOException ex) {
            logger.error("Error while reading file " + ADMINS_FILE_NAME, ex);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(playersFile))) {
            String line;
            while (Optional.ofNullable(line = reader.readLine()).isPresent()) {
                Long id = Long.valueOf(line);
                String username = reader.readLine();
                String hashedPassword = reader.readLine();
                people.add(new Player.Builder()
                        .id(id)
                        .username(username)
                        .hashedPassword(hashedPassword)
                        .build());
            }
        } catch (IOException ex) {
            logger.error("Error while reading file " + PLAYERS_FILE_NAME, ex);
        }

        return people;
    }
    /*ADMIN-----------------------------------------------------------------------------------------------------------*/
    public static void addAdminsToFile(List<Admin> admins){
        File adminsFile = new File(ADMINS_FILE_NAME);

        try(PrintWriter pw = new PrintWriter(adminsFile)){
            for (Admin admin : admins) {
                pw.println(admin.getId());
                pw.println(admin.getUsername());
                pw.println(admin.getHashedPassword());
            }
        }
        catch(IOException ex){
            logger.error("Error while writing to file " + ADMINS_FILE_NAME, ex);
        }
    }

    public static List<Admin> getAdminsFromFile() {
        List<Admin> admins = new ArrayList<>();
        File adminsFile = new File(ADMINS_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(adminsFile))) {
            String line;
            while (Optional.ofNullable(line = reader.readLine()).isPresent()) {
                Long id = Long.valueOf(line);
                String username = reader.readLine();
                String hashedPassword = reader.readLine();
                admins.add(new Admin(id, username, hashedPassword));
            }
        } catch (IOException ex) {
            logger.error("Error while reading file " + ADMINS_FILE_NAME, ex);
        }

        return admins;
    }
    public static void deleteAdminFromFile(Long id){
        List<Admin> admins =getAdminsFromFile();
        admins.removeIf(a -> a.getId().equals(id));
        addAdminsToFile(admins);
    }
    public static void editAdminFromFile(Admin newAdmin){
        List<Admin> admins = getAdminsFromFile().stream()
                .map(admin -> admin.getId().equals(newAdmin.getId()) ? newAdmin : admin)
                .collect(Collectors.toList());
        addAdminsToFile(admins);
    }

    /*PLAYER----------------------------------------------------------------------------------------------------------*/
    public static void addPlayersToFile(List<Player> players){
        File playersFile = new File(PLAYERS_FILE_NAME);

        try(PrintWriter pw = new PrintWriter(playersFile)){
            for (Player player : players) {
                pw.println(player.getId());
                pw.println(player.getUsername());
                pw.println(player.getHashedPassword());
            }
        }
        catch(IOException ex){
            logger.error("Error while writing to file " + PLAYERS_FILE_NAME, ex);
        }
    }
    public static List<Player> getPlayersFromFile() {
        List<Player> players = new ArrayList<>();
        File playersFile = new File(PLAYERS_FILE_NAME);

        try (BufferedReader reader = new BufferedReader(new FileReader(playersFile))) {
            String line;
            while (Optional.ofNullable(line = reader.readLine()).isPresent()) {
                Long id = Long.valueOf(line);
                String username = reader.readLine();
                String hashedPassword = reader.readLine();
                players.add(new Player.Builder()
                        .id(id)
                        .username(username)
                        .hashedPassword(hashedPassword)
                        .build());
            }
        } catch (IOException ex) {
            logger.error("Error while reading file " + PLAYERS_FILE_NAME, ex);
        }

        return players;
    }
    public static void deletePlayerFromFile(Long id){
        List<Player> players = getPlayersFromFile();
        players.removeIf(a -> a.getId().equals(id));
        addPlayersToFile(players);
    }
    public static void editPlayerFromFile(Player newPlayer){
        List<Player> players = getPlayersFromFile().stream()
                .map(player -> player.getId().equals(newPlayer.getId()) ? newPlayer : player)
                .collect(Collectors.toList());
        addPlayersToFile(players);
    }

    /*CHANGES---------------------------------------------------------------------------------------------------------*/
    public static void serializeChangesList(Changes<?> changes) {
        List<Changes<?>> changesList = FileUtils.deserializeChangesList();
        if (changesList == null) {
            changesList = new ArrayList<>();
        }
        changesList.add(changes);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(CHANGES_FILE_NAME))) {
            outputStream.writeObject(changesList);
        } catch (IOException e) {
            logger.error("Error while serializing", e);
        }
    }
    public static List<Changes<?>> deserializeChangesList() {
        List<Changes<?>> deserializedChangesList = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(CHANGES_FILE_NAME))) {
            deserializedChangesList = (List<Changes<?>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error while deserializing", e);
        }
        return deserializedChangesList;
    }
}