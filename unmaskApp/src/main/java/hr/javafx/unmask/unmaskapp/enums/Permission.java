package hr.javafx.unmask.unmaskapp.enums;

public enum Permission {
    ADMIN(0),
    PLAYER(1),
    GUEST(2);

    private final Integer permission;

    Permission(Integer premission) {
        this.permission = premission;
    }
    public Integer getPermission(){
        return permission;
    }
}
