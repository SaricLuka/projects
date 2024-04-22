package hr.javafx.unmask.unmaskapp.threads;

import hr.javafx.unmask.unmaskapp.UnmaskApplicationController;
import hr.javafx.unmask.unmaskapp.model.Admin;
import hr.javafx.unmask.unmaskapp.model.Changes;
import hr.javafx.unmask.unmaskapp.model.Person;
import hr.javafx.unmask.unmaskapp.model.Player;
import hr.javafx.unmask.unmaskapp.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class FileUtilsThread {
    final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
    private static Boolean activeConnectionWithAdminsFile = false;
    private static Boolean activeConnectionWithPlayersFile = false;
    private static Boolean activeConnectionWithChangesFile = false;

    public synchronized List<Person> getPeopleFromFiles() {
        while(activeConnectionWithAdminsFile || activeConnectionWithPlayersFile){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithAdminsFile = true;
        activeConnectionWithPlayersFile = true;

        List<Person> people = FileUtils.getPeopleFromFiles();

        activeConnectionWithAdminsFile = false;
        activeConnectionWithPlayersFile = false;

        notifyAll();
        return people;
    }

    /* ADMIN ----------------------------------------------------------------------------------------------------------- */
    public synchronized void addAdminsToFile(List<Admin> admins) {
        while(activeConnectionWithAdminsFile){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithAdminsFile = true;

        FileUtils.addAdminsToFile(admins);

        activeConnectionWithAdminsFile = false;

        notifyAll();
    }

    public synchronized List<Admin> getAdminsFromFile() {
        while(activeConnectionWithAdminsFile){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithAdminsFile = true;

        List<Admin> admins = FileUtils.getAdminsFromFile();

        activeConnectionWithAdminsFile = false;

        notifyAll();
        return admins;
    }

    public synchronized void deleteAdminFromFile(Long id) {
        while(activeConnectionWithAdminsFile){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithAdminsFile = true;

        FileUtils.deleteAdminFromFile(id);

        activeConnectionWithAdminsFile = false;

        notifyAll();
    }

    public synchronized void editAdminFromFile(Admin newAdmin) {
        while(activeConnectionWithAdminsFile){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithAdminsFile = true;

        FileUtils.editAdminFromFile(newAdmin);

        activeConnectionWithAdminsFile = false;

        notifyAll();
    }

    /* PLAYER ---------------------------------------------------------------------------------------------------------- */
    public synchronized void addPlayersToFile(List<Player> players) {
        while(activeConnectionWithPlayersFile){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithPlayersFile = true;

        FileUtils.addPlayersToFile(players);

        activeConnectionWithPlayersFile = false;

        notifyAll();
    }

    public synchronized List<Player> getPlayersFromFile() {
        while(activeConnectionWithPlayersFile){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithPlayersFile = true;

        List<Player> players = FileUtils.getPlayersFromFile();

        activeConnectionWithPlayersFile = false;

        notifyAll();
        return players;
    }

    public synchronized void deletePlayerFromFile(Long id) {
        while(activeConnectionWithPlayersFile){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithPlayersFile = true;

        FileUtils.deletePlayerFromFile(id);

        activeConnectionWithPlayersFile = false;

        notifyAll();
    }

    public synchronized void editPlayerFromFile(Player newPlayer) {
        while(activeConnectionWithPlayersFile){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithPlayersFile = true;

        FileUtils.editPlayerFromFile(newPlayer);

        activeConnectionWithPlayersFile = false;

        notifyAll();
    }

    /* CHANGES --------------------------------------------------------------------------------------------------------- */
    public synchronized void serializeChangesList(Changes<?> changes) {
        while(activeConnectionWithChangesFile){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithChangesFile = true;

        FileUtils.serializeChangesList(changes);

        activeConnectionWithChangesFile = false;

        notifyAll();
    }

    public synchronized List<Changes<?>> deserializeChangesList() {
        while(activeConnectionWithChangesFile){
            try{
                wait();
            }catch(InterruptedException e){
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        activeConnectionWithChangesFile = true;

        List<Changes<?>> changes = FileUtils.deserializeChangesList();

        activeConnectionWithChangesFile = false;

        notifyAll();
        return changes;
    }

}