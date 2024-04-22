package hr.javafx.unmask.unmaskapp.interfaces;

import hr.javafx.unmask.unmaskapp.model.Admin;
import hr.javafx.unmask.unmaskapp.model.Player;

public sealed interface Permissions permits Admin, Player {
    default Integer getPermissions(){
        return 2;
    }
}
