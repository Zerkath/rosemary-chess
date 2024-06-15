package rosemary.types;

import java.util.ArrayList;

public class Moves extends ArrayList<Short> {

    public Moves() {
        super(32);
    }

    /**
     * Helper constructor for testing different positions Can be used to easily add a list of moves
     * for comparisons
     *
     * @param moves moves seperated by " ", example "g1g2 f3g5"
     */
    public Moves(String moves) {
        super();
        this.addAll(moves);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (short move : this)
            stringBuilder.append(MoveUtil.moveToString(move)).append(": 1").append('\n');
        return stringBuilder.toString();
    }

    public void add(short origin, short destination) {
        this.add(MoveUtil.getMove(origin, destination));
    }

    /**
     * Helper function for testing different positions Used to add a "g1g2 f3g5"
     *
     * @param moves moves seperated by " "
     */
    public void addAll(String moves) {
        String[] mvs = moves.split(" ");

        for (String move : mvs) {
            this.add(MoveUtil.getMove(move));
        }
    }
}
