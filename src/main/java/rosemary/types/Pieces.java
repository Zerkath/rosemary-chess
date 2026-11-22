package rosemary.types;

public class Pieces {
    public static final byte EMPTY = 0b00000;
    public static final byte PAWN = 0b00001;
    public static final byte KNIGHT = 0b00010;
    public static final byte BISHOP = 0b00011;
    public static final byte ROOK = 0b00100;
    public static final byte QUEEN = 0b00101;
    public static final byte KING = 0b00110;
    public static final byte WHITE = 0b10000;
    public static final byte BLACK = 0b01000;

    /**
     * Can be used to get the type of piece
     *
     * usage: (piece & Pieces.TYPE_MASK)
     */
    public static final byte TYPE_MASK = 0b00111;

    public static final byte MASK_WHITE = 0b10000;
    public static final byte MASK_BLACK = 0b01000;

    /**
     * Can be used to get the colour of the piece
     *
     * usage: (piece & Pieces.MASK_COLOUR)
     */
    public static final byte MASK_COLOUR = MASK_WHITE | MASK_BLACK;

    public static char getPromotion(short header) {
        char result =
                switch (header & MoveUtil.atomicMask) {
                    case 0b111 -> 'q';
                    case 0b110 -> 'r';
                    case 0b101 -> 'b';
                    case 0b100 -> 'n';
                    default -> '\0';
                };
        return (header & 0b1000) != 0 ? Character.toUpperCase(result) : result;
    }

    public static byte getPromotionNum(short header) {
        int result =
                switch (header & MoveUtil.atomicMask) {
                    case 0b111 -> QUEEN;
                    case 0b110 -> ROOK;
                    case 0b101 -> BISHOP;
                    case 0b100 -> KNIGHT;
                    default -> '\0';
                };
        return (byte) ((header & 0b1000) != 0 ? result | WHITE : result | BLACK);
    }

    public static char getChar(int piece) {

        char result =
                switch ((TYPE_MASK & piece)) {
                    case PAWN -> 'p';
                    case KNIGHT -> 'n';
                    case BISHOP -> 'b';
                    case ROOK -> 'r';
                    case QUEEN -> 'q';
                    case KING -> 'k';
                    default -> '\0';
                };
        result = isWhite(piece) ? Character.toUpperCase(result) : result;
        return result;
    }

    public static byte getNum(char piece) {
        byte result = Character.isUpperCase(piece) ? WHITE : BLACK;
        piece = Character.toLowerCase(piece);
        return switch (piece) {
            case 'p' -> (byte) (result | PAWN);
            case 'n' -> (byte) (result | KNIGHT);
            case 'b' -> (byte) (result | BISHOP);
            case 'r' -> (byte) (result | ROOK);
            case 'q' -> (byte) (result | QUEEN);
            case 'k' -> (byte) (result | KING);
            default -> 0;
        };
    }

    public static boolean isWhite(int piece) {
        return (piece & MASK_COLOUR) == WHITE;
    }
}
