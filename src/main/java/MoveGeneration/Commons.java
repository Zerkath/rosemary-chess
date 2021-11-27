package MoveGeneration;

import DataTypes.Board;
import DataTypes.Coordinate;
import DataTypes.Moves;
import DataTypes.Pieces;

public class Commons {

    static boolean pieceMoveNotPossible(int rowOffset, int colOffset, Board board, Moves moves, Coordinate origin, boolean isWhite) {
        Coordinate destination = new Coordinate(origin.row + rowOffset, origin.column + colOffset);
        int target = board.getCoordinate(destination);

        if(target == 0) {
            moves.add(origin, destination);
            return false;
        }
        if(isWhite != Pieces.isWhite(target)) moves.add(origin, destination);
        return true;
    }
}