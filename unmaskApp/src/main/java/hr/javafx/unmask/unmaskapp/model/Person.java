package hr.javafx.unmask.unmaskapp.model;

abstract public class Person {
    private Long id;
    private String username;
    private String hashedPassword;
    public Person(Long id, String username, String hashedPassword) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
