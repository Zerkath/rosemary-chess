package rosemary.generation;

import java.util.stream.IntStream;
import rosemary.board.*;
import rosemary.types.*;

public class MoveGenerator {

    private static IntStream getAllMoves(BoardState boardState) {
        boolean isWhiteTurn = boardState.isWhiteTurn();
        byte[] board = boardState.getBoard();

        return IntStream.range(0, 64)
                .flatMap(
                        coord -> {
                            int piece = board[coord];
                            if (piece == Pieces.EMPTY || isWhiteTurn != Pieces.isWhite(piece)) {
                                return IntStream.empty();
                            }

                            return PieceType.forPiece(piece).moves((short) coord, boardState);
                        });
    }

    public static IntStream nonCacheGetLegalMoves(BoardState boardState) {
        return getAllMoves(boardState)
                .filter(move -> !Utils.kingInCheck(Mover.makeMove(boardState, (short) move)));
    }
}
