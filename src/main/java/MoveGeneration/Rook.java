package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;

public class Rook implements PieceGenerator {

    public void getMoves(Coordinate origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;

        int originalPiece = board.getCoordinate(origin);
        boolean isWhite = Pieces.isWhite(originalPiece);
        int row = origin.row;
        int col = origin.column;

        //Rook moves to left
        for(int i = 1; col - i >= 0; i++) { //Runs as long as destination is within board limits
            Coordinate destination = new Coordinate(row, col - i);

            int target = board.getCoordinate(destination);

            if(target == 0) {
                moves.add(origin, destination);
                continue;
            }
            if(isWhite != Pieces.isWhite(target)) moves.add(origin, destination);
            break;
        }


        //Rook moves upwards
        for(int i = 1; row - i >= 0; i++) {
            Coordinate destination = new Coordinate(row - i, col);

            int target = board.getCoordinate(destination);

            if(target == 0) {
                moves.add(origin, destination);
                continue;
            }
            if(isWhite != Pieces.isWhite(target)) moves.add(origin, destination);
            break;
        }

        //Rook moves to right
        for(int i = 1; col + i <= 7; i++) {
            Coordinate destination = new Coordinate(row, col + i);

            int target = board.getCoordinate(destination);

            if(target == 0) {
                moves.add(origin, destination);
                continue;
            }
            if(isWhite != Pieces.isWhite(target)) moves.add(origin, destination);
            break;
        }

        //Rook moves downwards
        for(int i = 1; row + i <= 7; i++) {
            Coordinate destination = new Coordinate(row + i, col);

            int target = board.getCoordinate(destination);

            if(target == 0) {
                moves.add(origin, destination);
                continue;
            }
            if(isWhite != Pieces.isWhite(target)) moves.add(origin, destination);
            break;
        }
    }
}
