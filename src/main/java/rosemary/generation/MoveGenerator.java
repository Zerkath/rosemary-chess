package rosemary.generation;

import java.util.Iterator;
import java.util.UUID;
import org.cache2k.*;
import rosemary.board.*;
import rosemary.types.*;

public class MoveGenerator {

    private static Cache<UUID, Moves> moveCache =
            new Cache2kBuilder<UUID, Moves>() {}.entryCapacity(5_000_000).build();

    private static Moves getAllMoves(BoardState boardState) {

        Moves moves = new Moves();
        byte[] board = boardState.board;
        boolean isWhiteTurn = boardState.isWhiteTurn;

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
        UUID boardUUID = BoardHasher.hashBoard(boardState);
        Moves moves =
                moveCache.computeIfAbsent(
                        boardUUID,
                        uuid -> {
                            Moves pseudoLegal = getAllMoves(boardState);

                            Iterator<Short> pseudoIterator = pseudoLegal.iterator();
                            short move;
                            while (pseudoIterator.hasNext()) {
                                move = pseudoIterator.next();
                                if (King.kingInCheck(Mover.makeMove(boardState, move))) {
                                    pseudoIterator.remove();
                                }
                            }

                            return pseudoLegal;
                        });

        return moves;
    }

    public static void getAllMoves(short coordinate, BoardState boardState, Moves moves) {
        int piece = boardState.board[coordinate];
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
