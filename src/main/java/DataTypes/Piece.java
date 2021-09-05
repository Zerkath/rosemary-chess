package DataTypes;

public class Piece {

    private Colour colour;
    private final PieceType pieceType;

    public Piece(Colour colour, PieceType pieceType) {
        this.colour = colour;
        this.pieceType = pieceType;
    }

    public Piece(char piece) {
        if(Character.isUpperCase(piece)) {
            colour = Colour.WHITE;
        } else {
            colour = Colour.BLACK;
        }
        piece = Character.toLowerCase(piece);
        switch(piece) {
            case 'p': pieceType = PieceType.PAWN; break;
            case 'r': pieceType = PieceType.ROOK; break;
            case 'q': pieceType = PieceType.QUEEN; break;
            case 'b': pieceType = PieceType.BISHOP; break;
            case 'k': pieceType = PieceType.KING; break;
            case 'n': pieceType = PieceType.KNIGHT; break;
            default: pieceType = null; break;
        }
    }

    public PieceType getType() {
        return pieceType;
    }

    public Colour getColour() {
        return colour;
    }

    public PlayerTurn getTurnColour() {
        return colour == Colour.WHITE ? PlayerTurn.WHITE : PlayerTurn.BLACK;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public char toChar() {
        char piece = ' ';
        switch (pieceType) {
            case PAWN: piece = 'p'; break;
            case ROOK: piece = 'r'; break;
            case QUEEN: piece = 'q'; break;
            case BISHOP: piece = 'b'; break;
            case KING: piece = 'k'; break;
            case KNIGHT: piece = 'n'; break;
        }
        piece = colour == Colour.WHITE ? Character.toUpperCase(piece) : piece;
        return piece;
    }

    public boolean equals(Piece piece) {
        if(piece == null) return false;
        return piece.colour == this.colour && piece.pieceType == this.pieceType;
    }
}
