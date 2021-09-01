package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.Coordinate;
import DataTypes.Moves;

public class Queen {
    Rook rook = new Rook();
    Bishop bishop = new Bishop();

    public Moves queenMoves(Coordinate origin, BoardState boardState) {

        Moves moves = new Moves();

        moves.addAll(rook.rookMoves(origin, boardState));
        moves.addAll(bishop.getMoves(origin, boardState));

        return moves;
    }
}
