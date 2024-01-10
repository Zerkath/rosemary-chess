package rosemary.generation;

import rosemary.board.BoardState;
import rosemary.types.*;

public class Pawn {

    private static void pawnPromotions(short move, boolean isWhite, Moves moves) {
        moves.add(MoveUtil.getMove(move, Pieces.QUEEN, isWhite));
        moves.add(MoveUtil.getMove(move, Pieces.KNIGHT, isWhite));
        moves.add(MoveUtil.getMove(move, Pieces.ROOK, isWhite));
        moves.add(MoveUtil.getMove(move, Pieces.BISHOP, isWhite));
    }

    private static void pawnCaptures(
            int nextRow, boolean promotion, short origin, Moves moves, Board board) {
        int original_piece = board.getCoordinate(origin);
        short col = MoveUtil.getColumn(origin);
        boolean isWhite = Pieces.isWhite(original_piece);
        boolean leftEdge = col == 0;
        boolean rightEdge = col == 7;

        // these destinations are mutates somewhere else
        short destination1 = Utils.getCoordinate(nextRow, col - 1);
        if (!leftEdge && board.pawnCapturePossible(destination1, original_piece)) {
            short nextMove = MoveUtil.getMove(origin, destination1);
            if (promotion) pawnPromotions(nextMove, isWhite, moves);
            else moves.add(nextMove);
        }

        short destination2 = Utils.getCoordinate(nextRow, col + 1);
        if (!rightEdge && board.pawnCapturePossible(destination2, original_piece)) {
            short nextMove = MoveUtil.getMove(origin, destination2);
            if (promotion) pawnPromotions(nextMove, isWhite, moves);
            else moves.add(nextMove);
        }
    }

    public static void getMoves(short origin, BoardState boardState, Moves moves) {

        Board board = boardState.board;

        int original_piece = board.getCoordinate(origin);
        boolean isWhite = Pieces.isWhite(original_piece);
        boolean promotion = false;
        int row = MoveUtil.getRow(origin);
        int col = MoveUtil.getColumn(origin);

        int nextRow = row;
        int doubleJump = row;
        int enPassantRow;

        if (isWhite) {
            nextRow -= 1;
            doubleJump -= 2;
            enPassantRow = 3;
            if (nextRow == 0) promotion = true;
        } else {
            nextRow += 1;
            doubleJump += 2;
            enPassantRow = 4;
            if (nextRow == 7) promotion = true;
        }

        if (nextRow < 0 || nextRow > 7) return; // if nextRow == -1 / 8

        // forward
        if (board.getCoordinate(nextRow, col) == Pieces.EMPTY) {
            short nextMove = MoveUtil.getMove(origin, Utils.getCoordinate(nextRow, col));
            if (promotion) pawnPromotions(nextMove, isWhite, moves);
            else moves.add(nextMove);

            if ((isWhite && row == 6 || (!isWhite && row == 1))
                    && board.getCoordinate(doubleJump, col) == Pieces.EMPTY) {
                // if at starting square and nothing in front
                short destination = Utils.getCoordinate(doubleJump, col);
                moves.add(MoveUtil.getMove(origin, destination));
            }
        }

        pawnCaptures(nextRow, promotion, origin, moves, board);

        if (boardState.enPassant != -1 && row == enPassantRow) {

            short destination = boardState.enPassant;
            int distance = MoveUtil.getColumn(origin) - MoveUtil.getColumn(destination);
            if (distance == 1 || distance == -1) {
                moves.add(MoveUtil.getMove(origin, destination));
            }
        }
    }
}
