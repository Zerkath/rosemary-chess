package com.github.zerkath.rosemary.MoveGeneration;

import com.github.zerkath.rosemary.BoardRepresentation.BoardState;
import com.github.zerkath.rosemary.DataTypes.*;
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
                Utils.addToCollection(row + 1, d_column, origin, moves);
                Utils.addToCollection(row - 1, d_column, origin, moves);
            }

            for (int d_row: rows) {
                Utils.addToCollection(d_row, col + 1, origin, moves);
                Utils.addToCollection(d_row, col - 1, origin, moves);
            }
            knightMoves.put(origin, moves);
        } 
    }

    public static void getMoves(Coordinate origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;
        Moves n_moves = knightMoves.get(origin);

        for(Move move: n_moves) {
           if(board.isValidMove(move)) moves.add(move); 
        }
    }
}
