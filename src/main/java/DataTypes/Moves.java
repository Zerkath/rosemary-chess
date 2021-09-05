package DataTypes;

import CommonTools.Utils;

import java.util.LinkedList;

public class Moves extends LinkedList<Move> {
    public String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Move move : this) {
            stringBuilder.append(Utils.parseCommand(move));
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}