package DataTypes;

import java.util.List;

public class Utils {

    /**
     * All possible coordinates, pooling resources
     */
    public static Coordinate[] allCoordinates = new Coordinate[64];

    static {
        for (int row = 0; row < 8; row++) {
            int o_row = row * 8;
            for (int col = 0; col < 8; col++) {
                allCoordinates[o_row + col] = new Coordinate(row, col);
            }
        }
    }

    public static boolean isOutOfBounds(Coordinate coord) {
        return isOutOfBounds(coord.row, coord.column);
    }

    public static boolean isOutOfBounds(int row, int column) {
        if (row < 0 || row > 7)
            return true;
        if (column < 0 || column > 7)
            return true;

        return false;
    }

    public static Coordinate getCoordinate(int row, int column) {
        return allCoordinates[row * 8 + column];
    }

    public static void addToCollection(int row, int column, Coordinate origin, List<Move> moves) {
        if (isOutOfBounds(row, column))
            return;
        moves.add(new Move(origin, getCoordinate(row, column)));
    }

    public static Coordinate getCoordinate(String coordinateString) {
        return getCoordinate(
                toRowInt(coordinateString.charAt(1)),
                toColumnInt(coordinateString.charAt(0)));
    }

    private static int toRowInt(char c) {
        return 8 - Integer.parseInt(String.valueOf(c));
    }

    private static int toColumnInt(char c) {
        return c - 'a';
    }
}
