package rosemary.generation;

import it.unimi.dsi.fastutil.longs.*;
import it.unimi.dsi.fastutil.shorts.*;
import rosemary.board.*;
import rosemary.types.*;

public class MoveGenerator {

    private static Long2LongOpenHashMap boardCache = new Long2LongOpenHashMap();
    private static Long2ObjectOpenHashMap<Moves> moveCache = new Long2ObjectOpenHashMap<>();

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
        long hash = ValueHasher.hashBoard(boardState);
        Moves moves;
        if (boardCache.containsKey(hash)) {
            long moveHash = boardCache.get(hash);
            moves = moveCache.get(moveHash);
        } else {
            moves = nonCacheGetLegalMoves(boardState);
            long newMoveHash = ValueHasher.hashMoves(moves);
            moveCache.putIfAbsent(newMoveHash, moves);
            boardCache.putIfAbsent(hash, newMoveHash);
        }
        return moves;
    }

    private static Moves nonCacheGetLegalMoves(BoardState boardState) {
        Moves pseudoLegal = getAllMoves(boardState);

        ShortIterator pseudoIterator = pseudoLegal.iterator();
        Moves legal = new Moves();
        while (pseudoIterator.hasNext()) {
            short move = pseudoIterator.nextShort();
            if (!King.kingInCheck(Mover.makeMove(boardState, move))) {
                legal.add(move);
            }
        }

        return legal;
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
