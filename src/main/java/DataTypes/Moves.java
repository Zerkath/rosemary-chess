package DataTypes;

import java.util.LinkedList;

public class Moves extends LinkedList<Move> {
    public String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Move move : this) {
            stringBuilder.append(move.toString()).append(": 1");
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}