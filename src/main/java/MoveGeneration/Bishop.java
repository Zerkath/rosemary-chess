package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;

public class Bishop implements PieceGenerator {

    public Moves getMoves(Coordinate origin, BoardState boardState) {

        Board board = boardState.board;

        Moves moves = new Moves();
        int originalPiece = board.getCoordinate(origin);
        boolean isWhite = Pieces.isWhite(originalPiece);
        int row = origin.row;
        int col = origin.column;

        //Bishop moves down and right
        for(int i = 1; row + i <= 7 && col + i <= 7; i++) { //Runs as long as destination is within board limits
            Coordinate destination = new Coordinate(row + i, col + i);
            int target = board.getCoordinate(destination);

            if(target == 0) {
                moves.add(origin, destination);
                continue;
            }
            if(isWhite != Pieces.isWhite(target)) moves.add(origin, destination);
            break;
        }

        //Bishop moves down and left
        for(int i = 1; row + i <= 7 && col - i >= 0; i++) {
            Coordinate destination = new Coordinate(row + i, col - i);
            int target = board.getCoordinate(destination);

            if(target == 0) {
                moves.add(origin, destination);
                continue;
            }
            if(isWhite != Pieces.isWhite(target)) moves.add(origin, destination);
            break;
        }

        //Bishop moves up and right
        for(int i = 1; row - i >= 0 && col + i <= 7; i++) {
            Coordinate destination = new Coordinate(row - i, col + i);
            int target = board.getCoordinate(destination);

            if(target == 0) {
                moves.add(origin, destination);
                continue;
            }
            if(isWhite != Pieces.isWhite(0)) moves.add(origin, destination);
            break;
        }

        //Bishop moves up and left
        for(int i = 1; row - i >= 0 && col - i >= 0; i++) {
            Coordinate destination = new Coordinate(row - i, col - i);
            int target = board.getCoordinate(destination);

            if(target == 0) {
                moves.add(origin, destination);
                continue;
            }
            if(isWhite != Pieces.isWhite(target)) moves.add(origin, destination);
            break;
        }
        return moves;
    }
}
