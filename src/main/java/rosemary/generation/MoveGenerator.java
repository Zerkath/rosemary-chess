package rosemary.generation;

import java.util.stream.IntStream;

import rosemary.board.*;
import rosemary.types.*;

public class MoveGenerator {

    private static Moves getAllMoves(BoardState boardState) {

        Moves moves = new Moves();
        byte[] board = boardState.getBoard();
        boolean isWhiteTurn = boardState.isWhiteTurn();

        // Iterates over board to play moves, dest denotes the piece currently being looked at
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                short coord = Utils.getCoordinate(row, column);
                int dest = board[coord];
                if (dest == 0) continue;
                boolean destIsWhite = Pieces.isWhite(dest);
                if (isWhiteTurn == destIsWhite) { // white turn and white or black turn and black
                    getAllMoves(coord, boardState, moves);
                }
            }
        }
        return moves;
    }

    public static Moves getLegalMoves(BoardState boardState) {
        Moves moves = new Moves(0);
        nonCacheGetLegalMoves(boardState).forEach(move -> moves.add((short)move));
        return moves;
    }

    public static IntStream nonCacheGetLegalMoves(BoardState boardState) {
        return getAllMoves(boardState)
          .intStream()
          .filter(move -> !King.kingInCheck(Mover.makeMove(boardState, (short) move)));
    }

    public static void getAllMoves(short coordinate, BoardState boardState, Moves moves) {
        int piece = boardState.getBoard()[coordinate];
        if (piece == 0) return;
        switch (Pieces.getType(piece)) {
            case Pieces.PAWN -> Pawn.getMoves(coordinate, boardState, moves);
            case Pieces.BISHOP -> Bishop.getMoves(coordinate, boardState, moves);
            case Pieces.KNIGHT -> Knight.getMoves(coordinate, boardState, moves);
            case Pieces.ROOK -> Rook.getMoves(coordinate, boardState, moves);
            case Pieces.QUEEN -> Queen.getMoves(coordinate, boardState, moves);
            case Pieces.KING -> King.getMoves(coordinate, boardState, moves);
        }
    }
}
