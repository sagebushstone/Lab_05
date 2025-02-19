import java.util.ArrayList;

public interface Strategy {
    public ArrayList<Object> determineMove(int userPlay);
}
