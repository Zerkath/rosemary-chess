package rosemary.generation;

import java.util.stream.IntStream;
import rosemary.board.BoardState;
import rosemary.types.Pieces;

public enum PieceType implements Piece {
    BISHOP(Pieces.BISHOP, Bishop.INSTANCE),
    ROOK(Pieces.ROOK, Rook.INSTANCE),
    KNIGHT(Pieces.KNIGHT, Knight.INSTANCE),
    QUEEN(Pieces.QUEEN, Queen.INSTANCE),
    PAWN(Pieces.PAWN, Pawn.INSTANCE),
    KING(Pieces.KING, King.INSTANCE);

    private static final PieceType[] BY_TYPE = new PieceType[8];

    static {
        for (PieceType type : values()) {
            BY_TYPE[type.code] = type;
        }
    }

    public final byte code;
    private final Piece implementation;

    PieceType(byte code, Piece implementation) {
        this.code = code;
        this.implementation = implementation;
    }

    public static PieceType forPiece(int piece) {
        return BY_TYPE[Pieces.getType(piece)];
    }

    @Override
    public IntStream moves(short origin, BoardState boardState) {
        return implementation.moves(origin, boardState);
    }
}
