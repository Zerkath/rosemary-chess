package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.Coordinate;
import DataTypes.Move;
import DataTypes.Moves;

public class Rook {

    MoveGenerationUtils moveUtils = new MoveGenerationUtils();

    public Moves rookMoves(Coordinate origin, BoardState boardState) {

        char [][] board = boardState.board;

        Moves moves = new Moves();
        char orig = moveUtils.getCoordinate(origin, board);
        int row = origin.row;
        int col = origin.column;

        //Rook moves to left
        for(int i = 1; col - i >= 0; i++) { //Runs as long as destination is within board limits
            Coordinate destination = new Coordinate(col - i, row);

            if(moveUtils.isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!moveUtils.locationIsEmpty(destination, board)) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }


        //Rook moves upwards
        for(int i = 1; row - i >= 0; i++) {
            Coordinate destination = new Coordinate(col, row - i);

            if(moveUtils.isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!moveUtils.locationIsEmpty(destination, board)) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }

        //Rook moves to right
        for(int i = 1; col + i <= 7; i++) {
            Coordinate destination = new Coordinate(col + i, row);

            if(moveUtils.isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!moveUtils.locationIsEmpty(destination, board)) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }

        //Rook moves downwards
        for(int i = 1; row + i <= 7; i++) {
            Coordinate destination = new Coordinate(col, row + i);

            if(moveUtils.isOpposingColourOrEmpty(destination, orig, board)) {
                moves.add(new Move(origin, destination));

                if(!moveUtils.locationIsEmpty(destination, board)) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }

        return moves;
    }
}
