package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;

import java.util.Arrays;

public class Pawn implements PieceGenerator {

    private Moves pawnPromotions(Move move, boolean isWhite) {
        Moves moves = new Moves();
        int offset = isWhite ? Pieces.WHITE : Pieces.BLACK;

        moves.addAll(
                Arrays.asList(
                        new Move(move, Pieces.QUEEN | offset),
                        new Move(move, Pieces.KNIGHT | offset),
                        new Move(move, Pieces.ROOK | offset),
                        new Move(move, Pieces.BISHOP | offset)
                )
        );
        return moves;
    }

    public void getMoves(Coordinate origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;

        int original_piece = board.getCoordinate(origin);
        boolean isWhite = Pieces.isWhite(original_piece);
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

        if(nextRow < 0 || nextRow > 7) return; // if nextRow == -1 / 8

        boolean leftEdge = col == 0;
        boolean rightEdge = col == 7;

        //forward
        if(board.getCoordinate(nextRow, col) == Pieces.EMPTY) {
            Move nextMove = new Move(origin, new Coordinate(nextRow, col));
            if(promotion) {
                moves.addAll(pawnPromotions(nextMove, isWhite));
            } else {
                moves.add(nextMove);
            }

            if((isWhite && row == 6 || (!isWhite && row == 1))
                    && board.getCoordinate(doubleJump, col) == Pieces.EMPTY) {
                //if at starting square and nothing in front
                Coordinate destination = new Coordinate(doubleJump, col);
                moves.add(new Move(origin, destination));
            }
        }

        //captures
        if(!leftEdge && board.pawnCapturePossible(new Coordinate(nextRow, col-1), original_piece)) {
            Move nextMove = new Move(origin, new Coordinate(nextRow, col-1));
            if(promotion) {
                moves.addAll(pawnPromotions(nextMove, isWhite));
            } else {
                moves.add(nextMove);
            }
        }

        if(!rightEdge && board.pawnCapturePossible(new Coordinate(nextRow, col+1), original_piece)) {
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
    }
}
