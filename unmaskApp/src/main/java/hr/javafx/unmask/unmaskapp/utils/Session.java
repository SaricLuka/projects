package hr.javafx.unmask.unmaskapp.utils;

import hr.javafx.unmask.unmaskapp.UnmaskApplicationController;
import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.exceptions.IncorrectPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final class Session {

    private static Session instance;
    private static String userName;
    private static Integer privilege;

    private Session(String userName, Integer privilege) {
        this.userName = userName;
        this.privilege = privilege;
    }

    public static Session getInstance(String userName, Integer privilege) {
        if(instance == null) {
            instance = new Session(userName, privilege);
        }
        return instance;
    }
    public static Session setGuest(){
        final Logger logger = LoggerFactory.getLogger(UnmaskApplicationController.class);
        final String GUEST_FILE = "src/conf/guest.properties";
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(GUEST_FILE));
        } catch (IOException e) {
            logger.error("Error while while logging in guest." + e.getMessage());
            throw new IncorrectPropertyException("Error while while logging in guest." + e.getMessage());
        }

        String username = properties.getProperty("username");

        return Session.getInstance(username, Permission.valueOf(username.toUpperCase()).getPermission());
    }


    public String getUserName() {
        return userName;
    }

    public Integer getPrivileges() {
        return privilege;
    }

    public static void cleanUserSession() {
        instance = null;
        userName = null;
        privilege = null;
    }
}
