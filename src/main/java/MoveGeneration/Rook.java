package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;

public class Rook implements PieceGenerator {

    public Moves getMoves(Coordinate origin, BoardState boardState) {

        Board board = boardState.board;

        Moves moves = new Moves();
        Piece orig = board.getCoordinate(origin);
        int row = origin.row;
        int col = origin.column;

        //Rook moves to left
        for(int i = 1; col - i >= 0; i++) { //Runs as long as destination is within board limits
            Coordinate destination = new Coordinate(row, col - i);

            if(board.isOpposingColourOrEmpty(destination, orig)) {
                moves.add(new Move(origin, destination));

                if(board.getCoordinate(destination) != null) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }


        //Rook moves upwards
        for(int i = 1; row - i >= 0; i++) {
            Coordinate destination = new Coordinate(row - i, col);

            if(board.isOpposingColourOrEmpty(destination, orig)) {
                moves.add(new Move(origin, destination));

                if(board.getCoordinate(destination) != null) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }

        //Rook moves to right
        for(int i = 1; col + i <= 7; i++) {
            Coordinate destination = new Coordinate(row, col + i);

            if(board.isOpposingColourOrEmpty(destination, orig)) {
                moves.add(new Move(origin, destination));

                if(board.getCoordinate(destination) != null) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }

        //Rook moves downwards
        for(int i = 1; row + i <= 7; i++) {
            Coordinate destination = new Coordinate(row + i, col);

            if(board.isOpposingColourOrEmpty(destination, orig)) {
                moves.add(new Move(origin, destination));

                if(board.getCoordinate(destination) != null) { //Checks if move ends in capture to end the loop
                    break;
                }
            } else {
                break;
            }
        }

        return moves;
    }
}
