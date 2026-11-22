package rosemary.generation;

import java.util.stream.IntStream;
import rosemary.board.BoardState;
import rosemary.types.BoardUtils;
import rosemary.types.MoveUtil;
import rosemary.types.Pieces;
import rosemary.types.Utils;

final class Pawn implements Piece {

    static final Pawn INSTANCE = new Pawn();

    private Pawn() {}

    private static IntStream pawnPromotions(short move, boolean isWhite) {
        return IntStream.of(
                MoveUtil.getMove(move, Pieces.QUEEN, isWhite),
                MoveUtil.getMove(move, Pieces.KNIGHT, isWhite),
                MoveUtil.getMove(move, Pieces.ROOK, isWhite),
                MoveUtil.getMove(move, Pieces.BISHOP, isWhite));
    }

    private static IntStream pawnCapture(
            int nextRow,
            boolean promotion,
            short origin,
            boolean isWhite,
            int column,
            int originalPiece,
            byte[] board) {
        if (column < 0 || column > 7) return IntStream.empty();

        short destination = Utils.getCoordinate(nextRow, column);
        if (!BoardUtils.pawnCapturePossible(destination, originalPiece, board))
            return IntStream.empty();

        short nextMove = MoveUtil.getMove(origin, destination);
        return promotion ? pawnPromotions(nextMove, isWhite) : IntStream.of(nextMove);
    }

    private static IntStream pawnCaptures(
            int nextRow, boolean promotion, short origin, byte[] board) {
        int originalPiece = board[origin];
        short col = MoveUtil.getColumn(origin);
        boolean isWhite = Pieces.isWhite(originalPiece);

        return IntStream.concat(
                pawnCapture(nextRow, promotion, origin, isWhite, col - 1, originalPiece, board),
                pawnCapture(nextRow, promotion, origin, isWhite, col + 1, originalPiece, board));
    }

    @Override
    public IntStream moves(short origin, BoardState boardState) {
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

        if (nextRow < 0 || nextRow > 7) return IntStream.empty(); // if nextRow == -1 / 8

        IntStream forwardMoves = IntStream.empty();

        // forward
        if (board[Utils.getCoordinate(nextRow, col)] == Pieces.EMPTY) {
            short nextMove = MoveUtil.getMove(origin, Utils.getCoordinate(nextRow, col));
            forwardMoves = promotion ? pawnPromotions(nextMove, isWhite) : IntStream.of(nextMove);

            if ((isWhite && row == 6 || (!isWhite && row == 1))
                    && board[Utils.getCoordinate(doubleJump, col)] == Pieces.EMPTY) {
                // if at starting square and nothing in front
                short destination = Utils.getCoordinate(doubleJump, col);
                forwardMoves =
                        IntStream.concat(
                                forwardMoves, IntStream.of(MoveUtil.getMove(origin, destination)));
            }
        }

        IntStream captures = pawnCaptures(nextRow, promotion, origin, board);

        IntStream enPassantMoves = IntStream.empty();
        if (boardState.getEnPassant() != -1 && row == enPassantRow) {
            short destination = boardState.getEnPassant();
            int distance = MoveUtil.getColumn(origin) - MoveUtil.getColumn(destination);
            if (distance == 1 || distance == -1) {
                enPassantMoves = IntStream.of(MoveUtil.getMove(origin, destination));
            }
        }

        return IntStream.concat(IntStream.concat(forwardMoves, captures), enPassantMoves);
    }
}
