package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.Coordinate;
import DataTypes.Moves;

public interface PieceGenerator {
    void getMoves(Coordinate origin, BoardState boardState, Moves moves);
}
