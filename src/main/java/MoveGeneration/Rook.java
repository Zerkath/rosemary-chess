package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;

import static MoveGeneration.Commons.pieceMoveNotPossible;

public class Rook {

    public static void getMoves(Coordinate origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;

        boolean isWhite = Pieces.isWhite(board.getCoordinate(origin));

        //Rook moves to left
        for(int i = 1; origin.column - i >= 0; i++) { //Runs as long as destination is within board limits
            if(pieceMoveNotPossible(0, -i, board, moves, origin, isWhite)) break;
        }

        //Rook moves upwards
        for(int i = 1; origin.row - i >= 0; i++) {
            if(pieceMoveNotPossible(-i, 0, board, moves, origin, isWhite)) break;
        }

        //Rook moves to right
        for(int i = 1; origin.column + i <= 7; i++) {
            if(pieceMoveNotPossible(0, i, board, moves, origin, isWhite)) break;
        }

        //Rook moves downwards
        for(int i = 1; origin.row + i <= 7; i++) {
            if(pieceMoveNotPossible(i, 0, board, moves, origin, isWhite)) break;
        }
    }
}
