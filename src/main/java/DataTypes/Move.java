package DataTypes;

public class Move {
    public Coordinate origin;
    public Coordinate destination;
    public char promotion;

    public Move(Coordinate origin, Coordinate destination) {
        this.origin = origin;
        this.destination = destination;
        this.promotion = '-';
    }

    public Move(Move move, char promotion) {
        this.origin = move.origin;
        this.destination = move.destination;
        this.promotion = promotion;
    }

    public Move(String move) {
        origin = new Coordinate(move.substring(0, 2));
        destination = new Coordinate(move.substring(2, 4));
        promotion = '-';
        if(move.length() == 5) {
            promotion = move.charAt(4);
        }
    }

    public String toString() {
        String sMove = origin.toString() + destination.toString();
        if(promotion != '-') {
            sMove += Character.toLowerCase(promotion);
        }
        return sMove;
    }
}