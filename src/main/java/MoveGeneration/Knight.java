package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.Coordinate;
import DataTypes.Move;
import DataTypes.Moves;

public class Knight implements Piece {

    MoveGenerationUtils moveUtils = new MoveGenerationUtils();

    public Moves getMoves(Coordinate origin, BoardState boardState) {

        char [][] board = boardState.board;

        Moves moves = new Moves();

        char orig = moveUtils.getCoordinate(origin, board);
        int row = origin.row;
        int col = origin.column;

        int [] columns = new int []{col-2, col+2};
        int [] rows = new int []{row-2, row+2};
        for (int d_column: columns) {
            Coordinate destination = new Coordinate(d_column, row + 1);
            if(moveUtils.isOpposingColourOrEmpty(destination, orig, board)) moves.add(new Move(origin, destination));
            destination = new Coordinate(d_column, row - 1);
            if(moveUtils.isOpposingColourOrEmpty(destination, orig, board)) moves.add(new Move(origin, destination));
        }

        for (int d_row: rows) {
            Coordinate destination = new Coordinate(col + 1, d_row);
            if(moveUtils.isOpposingColourOrEmpty(destination, orig, board)) moves.add(new Move(origin, destination));
            destination = new Coordinate(col - 1, d_row);
            if(moveUtils.isOpposingColourOrEmpty(destination, orig, board)) moves.add(new Move(origin, destination));
        }

        return moves;
    }
}
