package hr.javafx.unmask.unmaskapp.model;

import hr.javafx.unmask.unmaskapp.enums.Permission;
import hr.javafx.unmask.unmaskapp.interfaces.Permissions;

public final class Admin extends Person implements Permissions {
    public Admin(Long id, String username, String hashedPassword) {
        super(id, username, hashedPassword);
    }
    public Admin(Admin other) {
        super(other.getId(), other.getUsername(), other.getHashedPassword());
    }
    public Admin() {
        super(null,null,null);
    }
    @Override
    public Integer getPermissions(){
        return Permission.ADMIN.getPermission();
    }

    public String toString(){
        return getUsername();
    }
}
