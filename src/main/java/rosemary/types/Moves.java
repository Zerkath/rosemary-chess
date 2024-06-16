package rosemary.types;

import it.unimi.dsi.fastutil.shorts.*;

public class Moves extends ShortArrayList {

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
        ShortIterator iter = this.iterator();
        while (iter.hasNext()) {
            stringBuilder
                    .append(MoveUtil.moveToString(iter.nextShort()))
                    .append(": 1")
                    .append('\n');
        }
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
