package rosemary.board;

import java.util.Random;
import java.util.UUID;
import rosemary.types.Pieces;

public class BoardHasher {
    private static long[][] pieceKeys;
    private static long enPassantKey;
    private static long whiteTurnKey;

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

    public static UUID hashBoard(BoardState boardState) {
        long whiteHash = 0;
        long blackHash = 0;
        if (boardState.isWhiteTurn) {
            whiteHash = whiteTurnKey;
            blackHash = -whiteTurnKey;
        } else {
            whiteHash = -whiteTurnKey;
            blackHash = whiteTurnKey;
        }

        for (short square = 0; square < 64; square++) {
            int p = boardState.board[square];
            if (p != Pieces.EMPTY) {
                boolean isWhite = Pieces.isWhite(p);
                int color = isWhite ? 0 : 1;
                int piece = (p & Pieces.m_type) - 1;
                long hasher = pieceKeys[color][(piece * 64 + square)];

                if (isWhite) {
                    whiteHash ^= hasher;
                } else {
                    blackHash ^= hasher;
                }
            }
        }

        whiteHash ^= enPassantKey * boardState.enPassant;
        blackHash ^= enPassantKey * boardState.enPassant;

        return new UUID(whiteHash, blackHash);
    }
}
