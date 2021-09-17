package DataTypes;

import java.util.Arrays;

public class Board {

    private final int [] board = new int[64];
    private CastlingRights whiteCastlingRights = CastlingRights.NONE;
    private CastlingRights blackCastlingRights = CastlingRights.NONE;
    private final StringBuilder strBuilder = new StringBuilder();
    private Coordinate whiteKing;
    private Coordinate blackKing;

    public Board() {
        for (int i = 0; i < 64; i++) {
            this.board[i] = 0;
        }
    }

    public Board(Board board) {
        System.arraycopy(board.board, 0, this.board, 0, 64);
        this.whiteCastlingRights = board.whiteCastlingRights;
        this.blackCastlingRights = board.blackCastlingRights;
        this.whiteKing = board.whiteKing;
        this.blackKing = board.blackKing;
    }

    public int [] getBoard() {
        return this.board;
    }

    public int getCoordinate(Coordinate coordinate) {
        return getCoordinate(coordinate.row, coordinate.column);
    }

    public int getCoordinate(int row, int column) {
        return this.board[row*8+column];
    }

    public void clearCoordinate(int row, int column) {
        this.board[row*8+column] = 0;
    }

    public int [] getRow(int row) {
        int startIndex = row*8;
        int endIndex = startIndex+8;
        return Arrays.copyOfRange(board, startIndex, endIndex);
    }

    public void replaceCoordinate(int row, int column, int piece) {
        this.board[row*8+column] = piece;
    }

    public void replaceCoordinate(Coordinate coordinate, int piece) {
        if(piece != 0 && Pieces.getType(piece) == Pieces.KING) {
            replaceKing(coordinate, piece);
        }
        replaceCoordinate(coordinate.row, coordinate.column, piece);
    }

    private void replaceKing(Coordinate coordinate, int piece) {
        if(Pieces.isWhite(piece)) {
            setWhiteKing(coordinate);
        } else {
            setBlackKing(coordinate);
        }
    }

    public void clearCoordinate(Coordinate coordinate) {
        clearCoordinate(coordinate.row, coordinate.column);
    }

    public CastlingRights getWhiteCastling() {
        return whiteCastlingRights;
    }

    public CastlingRights getBlackCastling() {
        return blackCastlingRights;
    }

    public void setWhiteCastlingRights(CastlingRights rights) {
        whiteCastlingRights = rights;
    }

    public void setBlackCastlingRights(CastlingRights rights) {
        blackCastlingRights = rights;
    }

    public void setCastling(CastlingRights rights, boolean white) {
        if(white) {
            setWhiteCastlingRights(rights);
        } else {
            setBlackCastlingRights(rights);
        }
    }

    public Coordinate getWhiteKing() {
        return whiteKing;
    }

    public Coordinate getBlackKing() {
        return blackKing;
    }

    public void setBlackKing(Coordinate coordinate) {
        this.blackKing = coordinate;
    }

    public void setWhiteKing(Coordinate coordinate) {
        this.whiteKing = coordinate;
    }

    public boolean isOpposingColourOrEmpty(Coordinate destination, int originalPiece) {
        if(destination.row < 0 || destination.row > 7) return false;
        if(destination.column < 0 || destination.column > 7) return false;

        boolean isWhite = Pieces.isWhite(originalPiece);
        int dest = getCoordinate(destination);
        if(dest == Pieces.EMPTY) return true;
        boolean opponent = Pieces.isWhite(dest);

        return isWhite != opponent;
    }

    public boolean pawnCapturePossible(Coordinate coordinate, int origin) {
        return getCoordinate(coordinate) != Pieces.EMPTY && isOpposingColourOrEmpty(coordinate, origin);
    }

    public String toString() {
        strBuilder.setLength(0);
        String divider = "=|-----|-----|-----|-----|-----|-----|-----|-----|=\n";
        strBuilder.append("    0     1     2     3     4     5     6     7\n");
        for(int row = 0; row < 8; row++) {
            strBuilder.append(divider).append(row);
            for(int column = 0; column < 8; column++) {
                int piece = this.getCoordinate(row, column);
                if(piece != 0) {
                    strBuilder.append("|  ").append(Pieces.getChar(piece));
                } else {
                    strBuilder.append("|   ");
                }
                if(column != 7) strBuilder.append("  ");
                else strBuilder.append("  |");
            }
            strBuilder.append(8-row);
            strBuilder.append("\n");
        }
        strBuilder.append(divider);
        strBuilder.append("    a     b     c     d     e     f     g     h\n");

        return strBuilder.toString();
    }
}

