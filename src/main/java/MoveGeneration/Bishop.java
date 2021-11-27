package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;

import static MoveGeneration.Commons.pieceMoveNotPossible;

public class Bishop {

    public static void getMoves(Coordinate origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;

        boolean isWhite = Pieces.isWhite(board.getCoordinate(origin));

        //Bishop moves down and right
        for(int i = 1; origin.row + i <= 7 && origin.column + i <= 7; i++) { //Runs as long as destination is within board limits
            if(pieceMoveNotPossible(i, i, board, moves, origin, isWhite)) break;
        }

        //Bishop moves down and left
        for(int i = 1; origin.row + i <= 7 && origin.column - i >= 0; i++) {
            if(pieceMoveNotPossible(i, -i, board, moves, origin, isWhite)) break;
        }

        //Bishop moves up and right
        for(int i = 1; origin.row - i >= 0 && origin.column + i <= 7; i++) {
            if(pieceMoveNotPossible(-i, i, board, moves, origin, isWhite)) break;
        }

        //Bishop moves up and left
        for(int i = 1; origin.row - i >= 0 && origin.column - i >= 0; i++) {
            if(pieceMoveNotPossible(-i, -i, board, moves, origin, isWhite)) break;
        }
    }
}
