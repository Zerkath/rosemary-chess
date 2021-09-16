package DataTypes;

public class Pieces {
    static public final int EMPTY   = 0b00000;
    static public final int PAWN    = 0b00001;
    static public final int KNIGHT  = 0b00010;
    static public final int BISHOP  = 0b00011;
    static public final int ROOK    = 0b00100;
    static public final int QUEEN   = 0b00101;
    static public final int KING    = 0b00110;
    static public final int WHITE   = 0b10000;
    static public final int BLACK   = 0b01000;

    static public final int m_type  = 0b00111;
    static public final int m_white = 0b10000;
    static public final int m_black = 0b01000;
    static public final int m_colour= m_white | m_black;

    static public char getChar(int piece) {

        char result = ' ';
        switch (getType(piece)) {
            case PAWN: result = 'p'; break;
            case KNIGHT: result = 'n'; break;
            case BISHOP: result = 'b'; break;
            case ROOK: result = 'r'; break;
            case QUEEN: result = 'q'; break;
            case KING: result = 'k'; break;
        }
        result = isWhite(piece) ? Character.toUpperCase(result) : result;
        return result;
    }

    static public int getNum(char piece) {
        int result = Character.isUpperCase(piece) ? WHITE : BLACK;
        piece = Character.toLowerCase(piece);
        switch(piece) {
            case 'p': return result | PAWN;
            case 'n': return result | KNIGHT;
            case 'b': return result | BISHOP;
            case 'r': return result | ROOK;
            case 'q': return result | QUEEN;
            case 'k': return result | KING;
            default: return 0;
        }
    }

    static public int getType(int piece) {
        return piece & m_type;
    }

    static public int getColour(int piece) {
        return piece & m_colour;
    }

    static public boolean isWhite(int piece) {
        return getColour(piece) == WHITE;
    }
}
