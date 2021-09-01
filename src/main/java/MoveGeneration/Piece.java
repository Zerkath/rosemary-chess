package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.Coordinate;
import DataTypes.Moves;

public interface Piece {
    Moves getMoves(Coordinate origin, BoardState boardState);
}
