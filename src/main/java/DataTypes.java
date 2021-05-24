import java.util.ArrayList;
import java.util.LinkedList;

class Coordinate {
    public Coordinate(int column, int row) {
        this.column = (byte)column;
        this.row = (byte)row;
    }
    public Coordinate() {}
    public byte column;
    public byte row;
}
class Move {
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

    public Move() {}
    public Coordinate origin;
    public Coordinate destination;
    char promotion;
}

class Moves extends ArrayList<Move> {}

class MoveSequenceList extends ArrayList<Moves> {}

enum PlayerTurn {
    WHITE,
    BLACK,
}

enum CastlingRights {
    QUEENSIDE,
    KINGSIDE,
    BOTH,
    NONE
}