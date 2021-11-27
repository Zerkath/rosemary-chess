package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.Coordinate;
import DataTypes.Moves;

public class Queen {

    public static void getMoves(Coordinate origin, BoardState boardState, Moves moves) {
        Rook.getMoves(origin, boardState, moves);
        Bishop.getMoves(origin, boardState, moves);
    }
}
