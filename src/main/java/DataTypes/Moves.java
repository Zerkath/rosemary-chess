package DataTypes;

import java.util.LinkedList;

public class Moves extends LinkedList<Move> {

    public Moves() {
        super();
    }

    /**
     * Helper constructor for testing different positions
     * Can be used to easily add a list of moves for comparisons
     * @param moves moves seperated by " ", example "g1g2 f3g5"
     */
    public Moves(String moves) {
        super();
        this.addAll(moves);
    }

    public String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Move move : this) stringBuilder.append(move.toString()).append(": 1").append('\n');
        return stringBuilder.toString();
    }
    public void add(Coordinate origin, Coordinate destination) {
        this.add(new Move(origin, destination));
    }

    /**
     * Helper function for testing different positions
     * Used to add a "g1g2 f3g5"
     * @param moves moves seperated by " "
     */
    public void addAll(String moves) {
        String[] mvs = moves.split(" ");

        for (String move: mvs) {
            this.add(new Move(move));
        }
    }
}