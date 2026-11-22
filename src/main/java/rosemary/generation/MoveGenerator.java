package rosemary.generation;

import java.util.stream.IntStream;
import rosemary.board.*;
import rosemary.types.*;

public class MoveGenerator {

    public static Moves getLegalMoves(BoardState boardState) {
        Moves pseudoMoves = new Moves();
        generatePseudoLegalMoves(boardState, pseudoMoves);

        Moves legalMoves = new Moves(pseudoMoves.size());
        for (int i = 0; i < pseudoMoves.size(); i++) {
            short move = pseudoMoves.getShort(i);
            if (!Utils.kingInCheck(Mover.makeMove(boardState, move))) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    public static void generatePseudoLegalMoves(BoardState boardState, Moves moves) {
        boolean isWhiteTurn = boardState.isWhiteTurn();
        byte[] board = boardState.getBoard();

        for (int coord = 0; coord < 64; coord++) {
            int piece = board[coord];
            if (piece == Pieces.EMPTY || isWhiteTurn != Pieces.isWhite(piece)) continue;

            short origin = (short) coord;
            switch (Pieces.getType(piece)) {
                case Pieces.PAWN -> Pawn.getMoves(origin, boardState, moves);
                case Pieces.BISHOP -> Bishop.getMoves(origin, boardState, moves);
                case Pieces.KNIGHT -> Knight.getMoves(origin, boardState, moves);
                case Pieces.ROOK -> Rook.getMoves(origin, boardState, moves);
                case Pieces.QUEEN -> Queen.getMoves(origin, boardState, moves);
                case Pieces.KING -> King.getMoves(origin, boardState, moves);
            }
        }
    }

    /** @deprecated use {@link #getLegalMoves(BoardState)} to avoid stream overhead. */
    @Deprecated
    public static IntStream nonCacheGetLegalMoves(BoardState boardState) {
        return getLegalMoves(boardState).intStream();
    }
}
