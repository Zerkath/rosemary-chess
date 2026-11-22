package rosemary.generation;

import rosemary.board.BoardState;
import rosemary.types.BoardUtils;
import rosemary.types.MoveUtil;
import rosemary.types.Moves;
import rosemary.types.Pieces;
import rosemary.types.Utils;

final class Pawn {

    private Pawn() {}

    private static void pawnPromotions(short move, boolean isWhite, Moves moves) {
        moves.add(MoveUtil.getMove(move, Pieces.QUEEN, isWhite));
        moves.add(MoveUtil.getMove(move, Pieces.KNIGHT, isWhite));
        moves.add(MoveUtil.getMove(move, Pieces.ROOK, isWhite));
        moves.add(MoveUtil.getMove(move, Pieces.BISHOP, isWhite));
    }

    private static void pawnCapture(
            int nextRow,
            boolean promotion,
            short origin,
            boolean isWhite,
            int column,
            int originalPiece,
            byte[] board,
            Moves moves) {
        if (column < 0 || column > 7) return;

        short destination = Utils.getCoordinate(nextRow, column);
        if (!BoardUtils.pawnCapturePossible(destination, originalPiece, board)) return;

        short nextMove = MoveUtil.getMove(origin, destination);
        if (promotion) pawnPromotions(nextMove, isWhite, moves);
        else moves.add(nextMove);
    }

    private static void pawnCaptures(
            int nextRow, boolean promotion, short origin, byte[] board, Moves moves) {
        int originalPiece = board[origin];
        short col = MoveUtil.getColumn(origin);
        boolean isWhite = Pieces.isWhite(originalPiece);

        pawnCapture(nextRow, promotion, origin, isWhite, col - 1, originalPiece, board, moves);
        pawnCapture(nextRow, promotion, origin, isWhite, col + 1, originalPiece, board, moves);
    }

    static void getMoves(short origin, BoardState boardState, Moves moves) {
        byte[] board = boardState.getBoard();
        int originalPiece = board[origin];
        boolean isWhite = Pieces.isWhite(originalPiece);
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
        if (board[Utils.getCoordinate(nextRow, col)] == Pieces.EMPTY) {
            short nextMove = MoveUtil.getMove(origin, Utils.getCoordinate(nextRow, col));
            if (promotion) pawnPromotions(nextMove, isWhite, moves);
            else moves.add(nextMove);

            if ((isWhite && row == 6 || (!isWhite && row == 1))
                    && board[Utils.getCoordinate(doubleJump, col)] == Pieces.EMPTY) {
                // if at starting square and nothing in front
                short destination = Utils.getCoordinate(doubleJump, col);
                moves.add(MoveUtil.getMove(origin, destination));
            }
        }

        pawnCaptures(nextRow, promotion, origin, board, moves);

        if (boardState.getEnPassant() != -1 && row == enPassantRow) {
            short destination = boardState.getEnPassant();
            int distance = MoveUtil.getColumn(origin) - MoveUtil.getColumn(destination);
            if (distance == 1 || distance == -1) {
                moves.add(MoveUtil.getMove(origin, destination));
            }
        }
    }
}
