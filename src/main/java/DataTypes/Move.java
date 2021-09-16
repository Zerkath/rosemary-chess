package DataTypes;

public class Move {
    public Coordinate origin;
    public Coordinate destination;
    public int promotion;

    public Move(Coordinate origin, Coordinate destination) {
        this.origin = origin;
        this.destination = destination;
        this.promotion = 0;
    }

    public Move(Move move, int promotion) {
        this.origin = move.origin;
        this.destination = move.destination;
        this.promotion = promotion;
    }

    public Move(String move) {
        origin = new Coordinate(move.substring(0, 2));
        destination = new Coordinate(move.substring(2, 4));
        promotion = 0;
        if(move.length() == 5) {
            promotion = Pieces.getNum(move.charAt(4));
        }
    }

    public String toString() {
        String sMove = origin.toString() + destination.toString();
        if(promotion != 0) {
            sMove += Pieces.getChar(promotion);
        }
        return sMove;
    }
}