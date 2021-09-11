package DataTypes;

public class Piece {

    private Colour colour;
    private PieceType pieceType;

    public Piece(Colour colour, PieceType pieceType) {
        this.colour = colour;
        this.pieceType = pieceType;
    }

    public Piece(char piece) {
        colour = Character.isUpperCase(piece) ? Colour.WHITE : Colour.BLACK;

        piece = Character.toLowerCase(piece);
        switch(piece) {
            case 'p': pieceType = PieceType.PAWN; break;
            case 'r': pieceType = PieceType.ROOK; break;
            case 'q': pieceType = PieceType.QUEEN; break;
            case 'b': pieceType = PieceType.BISHOP; break;
            case 'k': pieceType = PieceType.KING; break;
            case 'n': pieceType = PieceType.KNIGHT; break;
        }
    }

    public char toChar() {
        char piece = ' ';
        switch (pieceType) {
            case KNIGHT: piece = 'n'; break;
            case BISHOP: piece = 'b'; break;
            case QUEEN: piece = 'q'; break;
            case PAWN: piece = 'p'; break;
            case ROOK: piece = 'r'; break;
            case KING: piece = 'k'; break;
        }
        if(colour == Colour.WHITE) piece = Character.toUpperCase(piece);
        return piece;
    }

    public PieceType getType() {
        return pieceType;
    }

    public Colour getColour() {
        return colour;
    }

    public boolean isWhite() {
        return colour == Colour.WHITE;
    }

    public PlayerTurn getTurnColour() {
        return colour == Colour.WHITE ? PlayerTurn.WHITE : PlayerTurn.BLACK;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public boolean equals(Piece piece) {
        return piece != null && piece.colour == this.colour && piece.pieceType == this.pieceType;
    }

    /**
     * returns a hash value for a piece
     * white 10, black 0
     * pawn 1
     * knight 2
     * bishop 3
     * rook 4
     * queen 5
     * king 0
     */
    @Override
    public int hashCode() {
        int hash = isWhite() ? 10 : 0;
        switch (pieceType) {
            case PAWN: hash += 1; break;
            case KNIGHT: hash += 2; break;
            case BISHOP: hash += 3; break;
            case ROOK: hash += 4; break;
            case QUEEN: hash += 5; break;
        }
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (this.getClass() != object.getClass()) return false;
        return equals((Piece)object);
    }
}
