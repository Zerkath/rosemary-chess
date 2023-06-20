package MoveGeneration;

import BoardRepresentation.BoardState;
import DataTypes.*;

public class Pawn {

    private static void pawnPromotions(Move move, boolean isWhite, Moves moves) {
        int offset = isWhite ? Pieces.WHITE : Pieces.BLACK;
        moves.add(new Move(move, Pieces.QUEEN | offset));
        moves.add(new Move(move, Pieces.KNIGHT | offset));
        moves.add(new Move(move, Pieces.ROOK | offset));
        moves.add(new Move(move, Pieces.BISHOP | offset));
    }

    private static void pawnCaptures(int nextRow, boolean promotion, Coordinate origin, Moves moves, Board board) {
        int original_piece = board.getCoordinate(origin);
        int col = origin.column;
        boolean isWhite = Pieces.isWhite(original_piece);
        boolean leftEdge = col == 0;
        boolean rightEdge = col == 7;

        // these destinations are mutates somewhere else
        Coordinate destination1 = new Coordinate(nextRow, col - 1);
        if (!leftEdge && board.pawnCapturePossible(destination1, original_piece)) {
            Move nextMove = new Move(origin, destination1);
            if (promotion)
                pawnPromotions(nextMove, isWhite, moves);
            else
                moves.add(nextMove);
        }

        Coordinate destination2 = new Coordinate(nextRow, col + 1);
        if (!rightEdge && board.pawnCapturePossible(destination2, original_piece)) {
            Move nextMove = new Move(origin, destination2);
            if (promotion)
                pawnPromotions(nextMove, isWhite, moves);
            else
                moves.add(nextMove);
        }
    }

    public static void getMoves(Coordinate origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;

        int original_piece = board.getCoordinate(origin);
        boolean isWhite = Pieces.isWhite(original_piece);
        boolean promotion = false;
        int row = origin.row;
        int col = origin.column;

        int nextRow = row;
        int doubleJump = row;
        int enPassantRow;

        if (isWhite) {
            nextRow -= 1;
            doubleJump -= 2;
            enPassantRow = 3;
            if (nextRow == 0)
                promotion = true;
        } else {
            nextRow += 1;
            doubleJump += 2;
            enPassantRow = 4;
            if (nextRow == 7)
                promotion = true;
        }

        if (nextRow < 0 || nextRow > 7)
            return; // if nextRow == -1 / 8

        // forward
        if (board.getCoordinate(nextRow, col) == Pieces.EMPTY) {
            Move nextMove = new Move(origin, Utils.getCoordinate(nextRow, col));
            if (promotion)
                pawnPromotions(nextMove, isWhite, moves);
            else
                moves.add(nextMove);

            if ((isWhite && row == 6 || (!isWhite && row == 1))
                    && board.getCoordinate(doubleJump, col) == Pieces.EMPTY) {
                // if at starting square and nothing in front
                Coordinate destination = Utils.getCoordinate(doubleJump, col);
                moves.add(new Move(origin, destination));
            }
        }

        pawnCaptures(nextRow, promotion, origin, moves, board);

        if (boardState.enPassant != null && row == enPassantRow) {

            Coordinate destination = boardState.enPassant;
            int distance = origin.column - destination.column;
            if (distance == 1 || distance == -1) {
                moves.add(new Move(origin, destination));
            }
        }
    }
}
