package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.Coordinate;
import DataTypes.Moves;

public class Queen implements PieceGenerator {
    Rook rook = new Rook();
    Bishop bishop = new Bishop();

    public Moves getMoves(Coordinate origin, BoardState boardState) {

        Moves moves = new Moves();

        moves.addAll(rook.getMoves(origin, boardState));
        moves.addAll(bishop.getMoves(origin, boardState));

        return moves;
    }
}
