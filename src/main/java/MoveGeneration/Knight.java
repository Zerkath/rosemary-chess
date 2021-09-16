package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;

public class Knight implements PieceGenerator {

    public Moves getMoves(Coordinate origin, BoardState boardState) {

        Board board = boardState.board;

        Moves moves = new Moves();

        int originalPiece = board.getCoordinate(origin);
        int row = origin.row;
        int col = origin.column;

        int [] columns = new int []{col-2, col+2};
        int [] rows = new int []{row-2, row+2};
        for (int d_column: columns) {
            Coordinate destination = new Coordinate(row + 1, d_column);
            if(board.isOpposingColourOrEmpty(destination, originalPiece)) moves.add(new Move(origin, destination));
            destination = new Coordinate(row - 1, d_column);
            if(board.isOpposingColourOrEmpty(destination, originalPiece)) moves.add(new Move(origin, destination));
        }

        for (int d_row: rows) {
            Coordinate destination = new Coordinate(d_row, col + 1);
            if(board.isOpposingColourOrEmpty(destination, originalPiece)) moves.add(new Move(origin, destination));
            destination = new Coordinate(d_row, col - 1);
            if(board.isOpposingColourOrEmpty(destination, originalPiece)) moves.add(new Move(origin, destination));
        }

        return moves;
    }
}
