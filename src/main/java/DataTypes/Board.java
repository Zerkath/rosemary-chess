package DataTypes;

public class Board {

    private final Row [] board;
    private CastlingRights whiteCastlingRights = CastlingRights.NONE;
    private CastlingRights blackCastlingRights = CastlingRights.NONE;
    private Coordinate whiteKing;
    private Coordinate blackKing;

    public Board() {
        this.board = new Row[8];
    }

    public Board(Board board) {
        this.board = board.board;
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

    public void replaceCoordinate(Coordinate coordinate, Piece piece) {
        replaceKing(coordinate, getRow(coordinate.row).replaceColumn(coordinate.column, piece));
    }

    public void replaceCoordinate(Coordinate coordinate, char piece) {
        replaceKing(coordinate, getRow(coordinate.row).replaceColumn(coordinate.column, piece));
    }

    public void replaceKing(Coordinate coordinate, Piece piece) {
        if(piece.getType() == PieceType.KING) {
            if(piece.getColour() == Colour.WHITE) {
                whiteKing = coordinate;
            } else {
                blackKing = coordinate;
            }
        }
    }

    public void clearCoordinate(Coordinate coordinate) {
        getRow(coordinate.row).clearColumn(coordinate.column);
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

    public void setBlackKing(Coordinate blackKing) {
        this.blackKing = blackKing;
    }

    public void setWhiteKing(Coordinate whiteKing) {
        this.whiteKing = whiteKing;
    }

    public boolean isOpposingColor(Piece origin, Piece target) {
        return origin.getType() != target.getType();
    }

    public boolean pawnCapturePossible(Coordinate coordinate, Piece origin) {
        return !isEmpty(this.getCoordinate(coordinate)) && opposingColourAndInbounds(coordinate, origin);
    }

    public boolean isCoordinateInBounds(Coordinate coordinate) {
        return (coordinate.column >= 0 &&
                coordinate.row >= 0 &&
                coordinate.column < 8 &&
                coordinate.row < 8
        );
    }

    public boolean opposingColourAndInbounds(Coordinate coordinate, Piece origin) {
        if(isCoordinateInBounds(coordinate)) {
            Piece target = this.getCoordinate(coordinate);
            return isOpposingColor(origin, target);

        } else return false;
    }

    public boolean isOpposingColourOrEmpty(Coordinate coordinate, Piece origin) {
        if(isCoordinateInBounds(coordinate)) {
            Piece destination = this.getCoordinate(coordinate);
            return destination == null || isOpposingColor(origin, destination);

        } else return false;
    }

    public boolean isEmpty(Piece piece) {
        return piece == null;
    }
}

