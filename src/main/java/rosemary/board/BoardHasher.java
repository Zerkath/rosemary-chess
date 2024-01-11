package rosemary.board;

import java.util.Random;
import rosemary.types.Pieces;

public class BoardHasher {
    private static long[][] pieceKeys;
    private static long enPassantKey;
    private static long depthKey;

    // Initialize Zobrist keys
    static {
        pieceKeys = new long[2][6 * 64]; // 2 colors, 6 piece types, 64 squares
        Random random = new Random(888); // set seed for reproducibility, of benchmarks

        for (int color = 0; color < 2; color++) {
            for (int piece = 0; piece < 6; piece++) {
                for (int square = 0; square < 64; square++) {
                    pieceKeys[color][piece * square] = random.nextLong();
                }
            }
        }

        enPassantKey = random.nextLong();
        depthKey = random.nextLong();
    }

    public static long calculateHash(BoardState boardState, int depth) {
        long hash = 0;

        for (short square = 0; square < 64; square++) {
            int p = boardState.board[square];
            if (p != Pieces.EMPTY) {
                int color = Pieces.isWhite(p) ? 1 : 0;
                int piece = p & Pieces.m_type - 1;
                hash ^= pieceKeys[color][(piece * square)];
            }
        }

        if (boardState.enPassant != -1) {
            hash ^= enPassantKey * boardState.enPassant;
        }

        hash ^= depthKey * depth;

        return hash;
    }
}
