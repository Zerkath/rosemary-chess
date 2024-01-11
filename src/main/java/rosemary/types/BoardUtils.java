package rosemary.types;

public class BoardUtils {

    private static final StringBuilder strBuilder = new StringBuilder();

    public static byte[] copy(byte[] board) {
        byte[] copy = new byte[64];
        System.arraycopy(board, 0, copy, 0, 64);
        return copy;
    }

    public static boolean isOpposingColourOrEmpty(
            short destination, int originalPiece, byte[] board) {
        if (Utils.isOutOfBounds(destination)) return false;

        boolean isWhite = Pieces.isWhite(originalPiece);
        int dest = board[destination];
        if (dest == Pieces.EMPTY) return true;
        boolean opponent = Pieces.isWhite(dest);

        return isWhite != opponent;
    }

    /** Valid move in this context means either empty or opposing colour */
    public static boolean isValidMove(short move, byte[] board) {
        return Pieces.EMPTY == board[MoveUtil.getDestination(move)]
                || Pieces.isWhite(board[MoveUtil.getOrigin(move)])
                        != Pieces.isWhite(board[MoveUtil.getDestination(move)]);
    }

    public static boolean pawnCapturePossible(short coordinate, int origin, byte[] board) {
        return board[coordinate] != Pieces.EMPTY
                && isOpposingColourOrEmpty(coordinate, origin, board);
    }

    public static String getString(byte[] board) {
        strBuilder.setLength(0);
        String divider = "=|-----|-----|-----|-----|-----|-----|-----|-----|=\n";
        strBuilder.append("    0     1     2     3     4     5     6     7\n");
        for (int row = 0; row < 8; row++) {
            strBuilder.append(divider).append(row);
            for (int column = 0; column < 8; column++) {
                int piece = board[Utils.getCoordinate(row, column)];
                if (piece != 0) {
                    strBuilder.append("|  ").append(Pieces.getChar(piece));
                } else {
                    strBuilder.append("|   ");
                }
                if (column != 7) strBuilder.append("  ");
                else strBuilder.append("  |");
            }
            strBuilder.append(8 - row);
            strBuilder.append("\n");
        }
        strBuilder.append(divider);
        strBuilder.append("    a     b     c     d     e     f     g     h\n");

        return strBuilder.toString();
    }
}
