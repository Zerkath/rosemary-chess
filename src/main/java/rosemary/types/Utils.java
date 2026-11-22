package rosemary.types;

import rosemary.board.BoardState;

public class Utils {

    public static short coordinateMask = 0b111;
    public static short moveMask = 0b111111;

    public static boolean isOutOfBounds(short coord) {
        return coord < 0 || coord > 64;
    }

    public static byte getCoordinate(int row, int column) {
        return (byte) ((row << 3) | column);
    }

    public static void addToCollection(int row, int column, int origin_row, int origin_col, Moves moves) {
        if (row < 0 || row > 7 || column < 0 || column > 7) return;
        moves.add(MoveUtil.getMove(getCoordinate(origin_row, origin_col), getCoordinate(row, column)));
    }

    public static byte getCoordinate(String coordinateString) {
        return getCoordinate(toRowInt(coordinateString.charAt(1)), toColumnInt(coordinateString.charAt(0)));
    }

    public static String moveToString(short move) {
        int origin = (move >> 6);
        int destination = (move & Utils.moveMask);

        return coordinateToString((short) origin) + "" + coordinateToString((short) destination);
    }

    public static String coordinateToString(short coordinate) {
        int row = (coordinate >> 3);
        int column = (coordinate & coordinateMask);

        return toColumnChar(column) + "" + toRowChar(row);
    }

    static char toColumnChar(int i) {
        return (char) (i + 'a');
    }

    static int toColumnInt(char c) {
        return c - 'a';
    }

    static char toRowChar(int i) {
        return Character.forDigit(8 - i, 10);
    }

    static int toRowInt(char c) {
        return 8 - Integer.parseInt(String.valueOf(c));
    }

    private static final int[][] ROOK_DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static final int[][] BISHOP_DIRECTIONS = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    private static final int[][] KNIGHT_OFFSETS = {
        {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}
    };

    /**
     * returns true if the king isn't in check
     *
     * @return true if the state is legal
     */
    public static boolean kingInCheck(BoardState boardState) {
        // The king that might be in check is the one whose turn just ended.
        boolean kingIsWhite = !boardState.isWhiteTurn();
        short origin = kingIsWhite ? boardState.getWhiteKing() : boardState.getBlackKing();
        if (origin == -1) return false;

        byte[] board = boardState.getBoard();
        int row = MoveUtil.getRow(origin);
        int col = MoveUtil.getColumn(origin);
        int opponentColour = kingIsWhite ? Pieces.BLACK : Pieces.WHITE;

        if (isAttackedBySlidingPiece(board, row, col, opponentColour, Pieces.ROOK, ROOK_DIRECTIONS))
            return true;
        if (isAttackedBySlidingPiece(board, row, col, opponentColour, Pieces.BISHOP, BISHOP_DIRECTIONS))
            return true;

        int opponentKnight = opponentColour | Pieces.KNIGHT;
        for (int[] offset : KNIGHT_OFFSETS) {
            int knightRow = row + offset[0];
            int knightCol = col + offset[1];
            if (knightRow < 0 || knightRow > 7 || knightCol < 0 || knightCol > 7) continue;
            if (board[Utils.getCoordinate(knightRow, knightCol)] == opponentKnight) return true;
        }

        int opponentPawn = opponentColour | Pieces.PAWN;
        int pawnRow = row + (kingIsWhite ? -1 : 1);
        if (pawnRow >= 0 && pawnRow <= 7) {
            if (col > 0 && board[Utils.getCoordinate(pawnRow, col - 1)] == opponentPawn) return true;
            if (col < 7 && board[Utils.getCoordinate(pawnRow, col + 1)] == opponentPawn) return true;
        }

        short opponentKing = kingIsWhite ? boardState.getBlackKing() : boardState.getWhiteKing();
        return opponentKing != -1 && distanceWithinBoundary(origin, opponentKing, 1);
    }

    private static boolean isAttackedBySlidingPiece(
            byte[] board, int row, int col, int opponentColour, int type, int[][] directions) {
        int attacker = opponentColour | type;
        int opponentQueen = opponentColour | Pieces.QUEEN;

        for (int[] direction : directions) {
            int pieceRow = row + direction[0];
            int pieceCol = col + direction[1];
            while (pieceRow >= 0 && pieceRow <= 7 && pieceCol >= 0 && pieceCol <= 7) {
                int piece = board[Utils.getCoordinate(pieceRow, pieceCol)];
                if (piece != Pieces.EMPTY) {
                    if (piece == attacker || piece == opponentQueen) return true;
                    break;
                }
                pieceRow += direction[0];
                pieceCol += direction[1];
            }
        }

        return false;
    }

    private static boolean distanceWithinBoundary(short a, short b, int boundary) {
        int absCol = Math.abs(MoveUtil.getColumn(a) - MoveUtil.getColumn(b));
        int absRow = Math.abs(MoveUtil.getRow(a) - MoveUtil.getRow(b));

        return absCol <= boundary && absRow <= boundary;
    }
}
