package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.Coordinate;
import DataTypes.Moves;

public class Queen implements PieceGenerator {
    Rook rook = new Rook();
    Bishop bishop = new Bishop();

    public void getMoves(Coordinate origin, BoardState boardState, Moves moves) {
        rook.getMoves(origin, boardState, moves);
        bishop.getMoves(origin, boardState, moves);
    }
}
