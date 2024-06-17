package rosemary.board;

import it.unimi.dsi.fastutil.shorts.*;
import java.util.*;
import rosemary.types.Moves;
import rosemary.types.Pieces;

public class ValueHasher {
    private static long[][] pieceKeys;
    private static long enPassantKey;
    private static long whiteTurnKey;
    private static final long polynomialHashKey = 31;

    // Initialize Zobrist keys
    static {
        pieceKeys = new long[2][6 * 64]; // 2 colors, 6 piece types, 64 squares
        Random random = new Random(100); // set seed for reproducibility, of benchmarks

        for (int color = 0; color < 2; color++) {
            for (int piece = 0; piece < 6; piece++) {
                for (int square = 0; square < 64; square++) {
                    pieceKeys[color][piece * 64 + square] = random.nextLong();
                }
            }
        }

        enPassantKey = random.nextLong();
        whiteTurnKey = random.nextLong();
    }

    public static long hashBoard(BoardState boardState) {
        long hash = 0;

        if (boardState.isWhiteTurn()) {
            hash = whiteTurnKey;
        } else {
            hash = -whiteTurnKey;
        }

        for (short square = 0; square < 64; square++) {
            int p = boardState.getBoard()[square];
            if (p != Pieces.EMPTY) {
                boolean isWhite = Pieces.isWhite(p);
                int color = isWhite ? 0 : 1;
                int piece = (p & Pieces.m_type) - 1;
                long hasher = pieceKeys[color][(piece * 64 + square)];
                hash ^= hasher;
            }
        }

        hash ^= enPassantKey * boardState.getEnPassant();
        return hash;
    }

    public static long hashMoves(Moves moves) {
        long hash = 0;
        moves.sort(null);

        ShortIterator iter = moves.iterator();
        while (iter.hasNext()) {
            hash = hash * polynomialHashKey + iter.nextShort();
        }
        return hash;
    }
}
