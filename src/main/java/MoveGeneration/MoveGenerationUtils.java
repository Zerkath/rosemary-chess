package MoveGeneration;

import DataTypes.Coordinate;

public class MoveGenerationUtils {
    public boolean isWhite(char c) {
        return !Character.isLowerCase(c);
    }

    public boolean isEmpty(char c) {
        return c == '-';
    }

    public boolean isOpposingColor(char a, char b) {
        return (isWhite(a) && !isWhite(b)) || (!isWhite(a) && isWhite(b));
    }

    public boolean pawnCapturePossible(Coordinate coordinate, char orig, char[][] board) {
        return !isEmpty(getCoordinate(coordinate, board)) && opposingColourAndInbounds(coordinate, orig, board);
    }

    public boolean isCoordinateInBounds(Coordinate coord) {
        return (coord.column >= 0 && coord.row >= 0 && coord.column < 8 && coord.row < 8);
    }

    public boolean opposingColourAndInbounds(Coordinate coord, char orig, char[][] board) {
        if(isCoordinateInBounds(coord)) {
            char dest = getCoordinate(coord, board);
            return isOpposingColor(orig, dest);

        } else return false;
    }

    public boolean locationIsEmpty(Coordinate coord, char [][] board) {
        return getCoordinate(coord, board) == '-';
    }

    /**
     * Checks also if the coord is in bounds
     */
    public boolean isOpposingColourOrEmpty(Coordinate coord, char orig, char[][] board) {
        if(isCoordinateInBounds(coord)) {
            char dest = getCoordinate(coord, board);
            return isOpposingColor(orig, dest) || locationIsEmpty(coord, board);

        } else return false;
    }

    public char getCoordinate(Coordinate coord, char [][] board) {
        return board[coord.row][coord.column];
    }
}
