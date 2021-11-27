package DataTypes;

import java.util.Stack;

public class Moves extends Stack<Move> {

    public String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Move move : this) {
            stringBuilder.append(move.toString()).append(": 1").append('\n');
        }
        return stringBuilder.toString();
    }
    public void add(Coordinate origin, Coordinate destination) {
        this.add(new Move(origin, destination));
    }
}