package DataTypes;

public class Board {

    private final Row [] board = new Row[8];
    private CastlingRights whiteCastlingRights = CastlingRights.NONE;
    private CastlingRights blackCastlingRights = CastlingRights.NONE;
    private Coordinate whiteKing;
    private Coordinate blackKing;

    public Board() {
        for (int i = 0; i < 8; i++) {
            this.board[i] = new Row();
        }
    }

    public Board(Board board) {
        for (int row = 0; row < 8; row++) {
            this.board[row] = new Row(board.getRow(row));
        }
        this.whiteCastlingRights = board.whiteCastlingRights;
        this.blackCastlingRights = board.blackCastlingRights;
        this.whiteKing = board.whiteKing;
        this.blackKing = board.blackKing;
    }

    public Row getRow(int row) {
        return this.board[row];
    }

    public Piece getCoordinate(Coordinate coordinate) {
        return getRow(coordinate.row).getColumn(coordinate.column);
    }

    public Piece getCoordinate(int row, int column) {
        return getRow(row).getColumn(column);
    }

    public void replaceCoordinate(int row, int column, Piece piece) {
        replaceCoordinate(new Coordinate(row, column), piece);
    }

    public void replaceCoordinate(Coordinate coordinate, Piece piece) {
        if(piece != null && piece.getType() == PieceType.KING) {
            replaceKing(coordinate, piece);
        }
        getRow(coordinate.row).replaceColumn(coordinate.column, piece);
    }

    public void replaceCoordinate(Coordinate coordinate, char piece) {
        replaceCoordinate(coordinate, new Piece(piece));
    }

    private void replaceKing(Coordinate coordinate, Piece piece) {
        if(piece.getColour() == Colour.WHITE) {
            setWhiteKing(coordinate);
        } else {
            setBlackKing(coordinate);
        }
    }

    public void clearCoordinate(Coordinate coordinate) {
        getRow(coordinate.row).clearColumn(coordinate.column);
    }

    public void clearCoordinate(int row, int column) {
        clearCoordinate(new Coordinate(row, column));
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

    public boolean isOpposingColour(Piece origin, Piece target) {
        return origin.getColour() != target.getColour();
    }

    public boolean pawnCapturePossible(Coordinate coordinate, Piece origin) {
        return !isEmpty(this.getCoordinate(coordinate)) && isOpposingColour(coordinate, origin);
    }

    public boolean isCoordinateInBounds(Coordinate coordinate) {
        return (coordinate.column >= 0 &&
                coordinate.row >= 0 &&
                coordinate.column < 8 &&
                coordinate.row < 8
        );
    }

    public boolean isOpposingColour(Coordinate coordinate, Piece origin) {
        Piece target = this.getCoordinate(coordinate);
        return isOpposingColour(origin, target);
    }

    public boolean isOpposingColourOrEmpty(Coordinate coordinate, Piece origin) {
        if(isCoordinateInBounds(coordinate)) {
            Piece destination = this.getCoordinate(coordinate);
            return destination == null || isOpposingColour(origin, destination);

        } else return false;
    }

    public boolean locationIsEmpty(int row, int column) {
        return this.getRow(row).getColumn(column) == null;
    }

    public boolean isEmpty(Piece piece) {
        return piece == null;
    }
}

