package hr.javafx.unmask.unmaskapp.model;

public class Stringer<T, U>{
    private T first;
    private U second;

    public Stringer(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public String getStringFromFirst() {
        return first.toString();
    }

    public String getStringFromSecond() {
        return second.toString();
    }
}
