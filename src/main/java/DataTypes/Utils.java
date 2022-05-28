package DataTypes;

public class Utils {

    public static Coordinate[] allCoordinates = new Coordinate[64];

    static {
        for(int row = 0; row < 8; row++) {
            int o_row = row * 8;
            for(int col = 0; col < 8; col++) {
                allCoordinates[o_row + col] = new Coordinate(row, col); 
            }
        }
    }

    public static boolean isOutOfBounds(Coordinate coord) {
        if(coord.row < 0 || coord.row > 7) return true;
        if(coord.column < 0 || coord.column > 7) return true;

        return false;
    }
}

