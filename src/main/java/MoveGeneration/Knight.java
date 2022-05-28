package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;
import java.util.HashMap;

public class Knight {

    static HashMap<Coordinate, Moves> knightMoves = new HashMap<>();
    static {
        for (Coordinate origin: Utils.allCoordinates) {
            int row = origin.row;
            int col = origin.column;
            
            int [] columns = new int []{col - 2, col + 2};
            int [] rows = new int []{row - 2, row + 2};
            Moves moves = new Moves();
            for (int d_column: columns) {
                Coordinate destination = new Coordinate(row + 1, d_column);
                
                if (!Utils.isOutOfBounds(destination)) moves.add(new Move(origin, destination));
                destination = new Coordinate(row - 1, d_column);
                if (!Utils.isOutOfBounds(destination)) moves.add(new Move(origin, destination));
            }

            for (int d_row: rows) {
                Coordinate destination = new Coordinate(d_row, col + 1);
                if (!Utils.isOutOfBounds(destination)) moves.add(new Move(origin, destination));
                destination = new Coordinate(d_row, col - 1);
                if (!Utils.isOutOfBounds(destination)) moves.add(new Move(origin, destination));
            }
            knightMoves.put(origin, moves);
        } 
    }

    public static void getMoves(Coordinate origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;

        int originalPiece = board.getCoordinate(origin);

        Moves n_moves = knightMoves.get(origin);

        for(Move move: n_moves) {
           if(board.isValidMove(move)) moves.add(move); 
        }
    }
}
