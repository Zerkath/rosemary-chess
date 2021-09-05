package MoveGeneration;

import BoardRepresentation.BoardState;
import CommonTools.Utils;
import DataTypes.*;

import java.util.Arrays;

public class Pawn implements PieceGenerator {

    private Moves pawnPromotions(Move move, boolean isWhite) {
        Moves moves = new Moves();
        char queen, knight, rook, bishop;

        if(isWhite) {
            queen = 'Q'; knight = 'N'; rook = 'R'; bishop = 'B';
        } else {
            queen = 'q'; knight = 'n'; rook = 'r'; bishop = 'b';
        }
        moves.addAll(
                Arrays.asList(
                        new Move(move, queen),
                        new Move(move, knight),
                        new Move(move, rook),
                        new Move(move, bishop)
                )
        );
        return moves;
    }

    public Moves getMoves(Coordinate origin, BoardState boardState) {

        Board board = boardState.board;

        Moves moves = new Moves();
        Piece orig = board.getCoordinate(origin);
        boolean isWhite = Utils.isWhite(orig);
        boolean promotion = false;
        int row = origin.row;
        int col = origin.column;

        int nextRow = row;
        int doubleJump = row;
        int enPassantRow;

        if(isWhite) {
            nextRow -= 1;
            doubleJump -= 2;
            enPassantRow = 3;
            if(nextRow == 0) {
                promotion = true;
            }
        } else {
            nextRow += 1;
            doubleJump += 2;
            enPassantRow = 4;
            if(nextRow == 7) {
                promotion = true;
            }
        }

        boolean leftEdge = col == 0;
        boolean rightEdge = col == 7;

        if(!board.isCoordinateInBounds(new Coordinate(nextRow, col))) return moves; // if nextRow == -1 / 8

        //forward
        if(board.locationIsEmpty(nextRow, col)) {
            Move nextMove = new Move(origin, new Coordinate(nextRow, col));
            if(promotion) {
                moves.addAll(pawnPromotions(nextMove, isWhite));
            } else {
                moves.add(nextMove);
            }

            if((Utils.isWhite(orig) && row == 6 || (!Utils.isWhite(orig) && row == 1))
                    && board.locationIsEmpty(doubleJump, col)) {
                //if at starting square and nothing in front
                Coordinate destination = new Coordinate(doubleJump, col);
                moves.add(new Move(origin, destination));
            }
        }

        //captures
        if(!leftEdge && board.pawnCapturePossible(new Coordinate(nextRow, col-1), orig)) {
            Move nextMove = new Move(origin, new Coordinate(nextRow, col-1));
            if(promotion) {
                moves.addAll(pawnPromotions(nextMove, isWhite));
            } else {
                moves.add(nextMove);
            }
        }

        if(!rightEdge && board.pawnCapturePossible(new Coordinate(nextRow, col+1), orig)) {
            Move nextMove = new Move(origin, new Coordinate(nextRow, col+1));
            if(promotion) {
                moves.addAll(pawnPromotions(nextMove, isWhite));
            } else {
                moves.add(nextMove);
            }
        }

        if(boardState.enPassant != null && row == enPassantRow) {

            Coordinate destination = boardState.enPassant;
            int distance = origin.column - destination.column;
            if(distance == 1 || distance == -1) {
                moves.add(new Move(origin, destination));
            }
        }
        return moves;
    }
}
