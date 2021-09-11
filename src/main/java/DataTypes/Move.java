package DataTypes;

public class Move {
    public Coordinate origin;
    public Coordinate destination;
    public Piece promotion;

    //todo rework promotion into a piece instead of char
    public Move(Coordinate origin, Coordinate destination) {
        this.origin = origin;
        this.destination = destination;
        this.promotion = null;
    }

    public Move(Move move, char promotion) {
        this.origin = move.origin;
        this.destination = move.destination;
        this.promotion = new Piece(promotion);
    }

    public Move(String move) {
        origin = new Coordinate(move.substring(0, 2));
        destination = new Coordinate(move.substring(2, 4));
        promotion = null;
        if(move.length() == 5) {
            promotion = new Piece(move.charAt(4));
        }
    }

    public String toString() {
        String sMove = origin.toString() + destination.toString();
        if(promotion != null) {
            sMove += promotion.toChar();
        }
        return sMove;
    }
}