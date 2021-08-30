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
    public Move(Coordinate origin, Coordinate destination, char promotion) {
        this.origin = origin;
        this.destination = destination;
        this.promotion = promotion;
    }

    public Move(Move move, char promotion) {
        this.origin = move.origin;
        this.destination = move.destination;
        this.promotion = promotion;
    }

    public Move() {}
}