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
    }

    public Move() {}
    public Coordinate origin;
    public Coordinate destination;
}

class Moves extends LinkedList<Move>{}

class MoveSequenceList extends LinkedList<Moves> {}

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